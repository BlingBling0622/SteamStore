package com.steamlibrary.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.steamlibrary.exception.SteamAccountAlreadyLinkedException;
import com.steamlibrary.exception.SteamAccountNotFoundException;
import com.steamlibrary.exception.SteamApiException;
import com.steamlibrary.model.Game;
import com.steamlibrary.model.SteamAccount;
import com.steamlibrary.model.User;
import com.steamlibrary.repository.GameRepository;
import com.steamlibrary.repository.SteamAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SteamService {

    private final SteamAccountRepository steamAccountRepository;
    private final GameRepository gameRepository;

    @Value("${steam.api.key}")
    private String steamApiKey;

    private static final String STEAM_API_BASE_URL = "https://api.steampowered.com";
    private static final int REQUEST_TIMEOUT_SECONDS = 30;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
            .build();

    /**
     * Link a Steam account to a user
     */
    @Transactional
    public SteamAccount linkSteamAccount(User user, String steamId) {
        log.info("Attempting to link Steam account {} to user {}", steamId, user.getUsername());

        // Check if user already has a linked Steam account
        if (steamAccountRepository.existsByUser(user)) {
            log.warn("User {} already has a Steam account linked", user.getUsername());
            throw new SteamAccountAlreadyLinkedException("You already have a Steam account linked. Please unlink it first.");
        }

        // Check if this Steam ID is already linked to another user
        Optional<SteamAccount> existingAccount = steamAccountRepository.findBySteamId(steamId);
        if (existingAccount.isPresent()) {
            log.warn("Steam ID {} is already linked to another user", steamId);
            throw new SteamAccountAlreadyLinkedException("This Steam account is already linked to another user.");
        }

        // Fetch Steam profile information
        JsonObject playerData = fetchSteamProfile(steamId);

        // Create and save Steam account
        SteamAccount steamAccount = new SteamAccount();
        steamAccount.setUser(user);
        steamAccount.setSteamId(steamId);
        steamAccount.setPersonaName(playerData.has("personaname") ? playerData.get("personaname").getAsString() : "Unknown");
        steamAccount.setProfileUrl(playerData.has("profileurl") ? playerData.get("profileurl").getAsString() : null);
        steamAccount.setAvatarUrl(playerData.has("avatarfull") ? playerData.get("avatarfull").getAsString() : null);

        SteamAccount saved = steamAccountRepository.save(steamAccount);
        log.info("Successfully linked Steam account  to user {}", steamId, user.getUsername());

        return saved;
    }

    /**
     * Unlink Steam account from a user
     */
    @Transactional
    public void unlinkSteamAccount(User user) {
        log.info("Attempting to unlink Steam account for user {}", user.getUsername());

        SteamAccount steamAccount = steamAccountRepository.findByUser(user)
                .orElseThrow(() -> new SteamAccountNotFoundException("No Steam account linked to this user"));

        // Delete all games associated with this Steam account
        gameRepository.deleteBySteamAccount(steamAccount);
        log.debug("Deleted games for Steam account {}", steamAccount.getSteamId());

        // Delete Steam account
        steamAccountRepository.delete(steamAccount);
        log.info("Successfully unlinked Steam account for user {}", user.getUsername());
    }

    /**
     * Get Steam account for a user
     */
    @Transactional(readOnly = true)
    public Optional<SteamAccount> getSteamAccount(User user) {
        return steamAccountRepository.findByUser(user);
    }

    /**
     * Check if user has a linked Steam account
     */
    @Transactional(readOnly = true)
    public boolean hasSteamAccount(User user) {
        return steamAccountRepository.existsByUser(user);
    }

    /**
     * Sync Steam library for a user
     */
    @Transactional
    public int syncSteamLibrary(User user) {
        log.info("Starting Steam library sync for user {}", user.getUsername());

        SteamAccount steamAccount = steamAccountRepository.findByUser(user)
                .orElseThrow(() -> new SteamAccountNotFoundException("No Steam account linked to this user"));

        // Fetch owned games from Steam API
        List<JsonObject> ownedGames = fetchOwnedGames(steamAccount.getSteamId());
        log.info("Fetched {} games from Steam API for user {}", ownedGames.size(), user.getUsername());

        // Save games in a temporary list before deleting old ones
        List<Game> newGames = new ArrayList<>();
        for (JsonObject gameData : ownedGames) {
            Game game = new Game();
            game.setSteamAccount(steamAccount);
            game.setAppId(gameData.get("appid").getAsString());
            game.setName(gameData.has("name") ? gameData.get("name").getAsString() : "Unknown Game");
            game.setPlaytimeForever(gameData.has("playtime_forever") ? gameData.get("playtime_forever").getAsInt() : 0);
            game.setPlaytimeWindowsForever(gameData.has("playtime_windows_forever") ? gameData.get("playtime_windows_forever").getAsInt() : 0);
            game.setPlaytimeMacForever(gameData.has("playtime_mac_forever") ? gameData.get("playtime_mac_forever").getAsInt() : 0);
            game.setPlaytimeLinuxForever(gameData.has("playtime_linux_forever") ? gameData.get("playtime_linux_forever").getAsInt() : 0);
            game.setImgIconUrl(gameData.has("img_icon_url") ? gameData.get("img_icon_url").getAsString() : null);
            game.setImgLogoUrl(gameData.has("img_logo_url") ? gameData.get("img_logo_url").getAsString() : null);

            newGames.add(game);
        }

        // Delete old games and save new ones atomically
        gameRepository.deleteBySteamAccount(steamAccount);
        gameRepository.saveAll(newGames);

        // Update last synced timestamp
        steamAccount.setLastSynced(LocalDateTime.now());
        steamAccountRepository.save(steamAccount);

        log.info("Successfully synced {} games for user {}", newGames.size(), user.getUsername());
        return newGames.size();
    }

    /**
     * Get all games for a user's Steam account
     */
    @Transactional(readOnly = true)
    public List<Game> getGames(User user) {
        SteamAccount steamAccount = steamAccountRepository.findByUser(user)
                .orElseThrow(() -> new SteamAccountNotFoundException("No Steam account linked to this user"));

        return gameRepository.findBySteamAccount(steamAccount);
    }

    /**
     * Get game count for a user's Steam account
     */
    @Transactional(readOnly = true)
    public long getGameCount(User user) {
        SteamAccount steamAccount = steamAccountRepository.findByUser(user)
                .orElseThrow(() -> new SteamAccountNotFoundException("No Steam account linked to this user"));

        return gameRepository.countBySteamAccount(steamAccount);
    }

    /**
     * Fetch Steam profile information from Steam API
     */
    private JsonObject fetchSteamProfile(String steamId) {
        String url = String.format("%s/ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=%s",
                STEAM_API_BASE_URL, steamApiKey, steamId);

        log.debug("Fetching Steam profile for Steam ID: {}", steamId);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("Steam API returned status code {} for profile fetch", response.statusCode());
                throw new SteamApiException("Failed to fetch Steam profile. Steam API returned status: " + response.statusCode());
            }

            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject responseObj = jsonResponse.getAsJsonObject("response");
            JsonArray players = responseObj.getAsJsonArray("players");

            if (players == null || players.size() == 0) {
                log.error("No player data found for Steam ID: {}", steamId);
                throw new SteamApiException("Invalid Steam ID or profile is private");
            }

            JsonObject playerData = players.get(0).getAsJsonObject();
            log.debug("Successfully fetched Steam profile for: {}", playerData.get("personaname").getAsString());

            return playerData;

        } catch (IOException e) {
            log.error("Network error while fetching Steam profile for Steam ID {}: {}", steamId, e.getMessage(), e);
            throw new SteamApiException("Network error while communicating with Steam API. Please try again later.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Request interrupted while fetching Steam profile for Steam ID {}: {}", steamId, e.getMessage(), e);
            throw new SteamApiException("Request was interrupted. Please try again.", e);
        } catch (Exception e) {
            log.error("Unexpected error while fetching Steam profile for Steam ID {}: {}", steamId, e.getMessage(), e);
            throw new SteamApiException("Failed to fetch Steam profile: " + e.getMessage(), e);
        }
    }

    /**
     * Fetch owned games from Steam API
     */
    private List<JsonObject> fetchOwnedGames(String steamId) {
        String url = String.format("%s/IPlayerService/GetOwnedGames/v0001/?key=%s&steamid=%s&include_appinfo=true&include_played_free_games=true&format=json",
                STEAM_API_BASE_URL, steamApiKey, steamId);

        log.debug("Fetching owned games for Steam ID: {}", steamId);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(REQUEST_TIMEOUT_SECONDS))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("Steam API returned status code {} for owned games fetch", response.statusCode());
                throw new SteamApiException("Failed to fetch owned games. Steam API returned status: " + response.statusCode());
            }

            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject responseObj = jsonResponse.getAsJsonObject("response");

            if (!responseObj.has("games")) {
                log.warn("No games found for Steam ID {} - profile may be private or user has no games", steamId);
                return new ArrayList<>();
            }

            JsonArray gamesArray = responseObj.getAsJsonArray("games");
            List<JsonObject> gamesList = new ArrayList<>();

            for (JsonElement element : gamesArray) {
                gamesList.add(element.getAsJsonObject());
            }

            log.debug("Successfully fetched {} games for Steam ID: {}", gamesList.size(), steamId);
            return gamesList;

        } catch (IOException e) {
            log.error("Network error while fetching owned games for Steam ID {}: {}", steamId, e.getMessage(), e);
            throw new SteamApiException("Network error while communicating with Steam API. Please try again later.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Request interrupted while fetching owned games for Steam ID {}: {}", steamId, e.getMessage(), e);
            throw new SteamApiException("Request was interrupted. Please try again.", e);
        } catch (Exception e) {
            log.error("Unexpected error while fetching owned games for Steam ID {}: {}", steamId, e.getMessage(), e);
            throw new SteamApiException("Failed to fetch owned games: " + e.getMessage(), e);
        }
    }
}
