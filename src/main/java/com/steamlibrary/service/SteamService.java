package com.steamlibrary.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.steamlibrary.dto.SteamGameDto;
import com.steamlibrary.dto.SteamProfileDto;
import com.steamlibrary.model.SteamAccount;
import com.steamlibrary.model.SteamGame;
import com.steamlibrary.model.User;
import com.steamlibrary.repository.SteamAccountRepository;
import com.steamlibrary.repository.SteamGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SteamService {

    private final SteamAccountRepository steamAccountRepository;
    private final SteamGameRepository steamGameRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${steam.api.key}")
    private String steamApiKey;

    @Value("${steam.api.base.url}")
    private String steamApiBaseUrl;

    /**
     * Link Steam account to user
     */
    @Transactional
    public SteamAccount linkSteamAccount(User user, String steamId) {
        // Check if Steam account already linked to another user
        Optional<SteamAccount> existingAccount = steamAccountRepository.findBySteamId(steamId);
        if (existingAccount.isPresent()) {
            throw new RuntimeException("Steam account already linked to another user");
        }

        // Fetch Steam profile
        SteamProfileDto profile = getSteamProfile(steamId);

        // Create or update Steam account
        SteamAccount steamAccount = steamAccountRepository.findByUserId(user.getId())
                .orElse(new SteamAccount());

        steamAccount.setSteamId(steamId);
        steamAccount.setPersonaName(profile.getPersonaName());
        steamAccount.setProfileUrl(profile.getProfileUrl());
        steamAccount.setAvatarUrl(profile.getAvatarUrl());
        steamAccount.setUser(user);

        return steamAccountRepository.save(steamAccount);
    }

    /**
     * Get Steam profile information
     */
    public SteamProfileDto getSteamProfile(String steamId) {
        try {
            String url = String.format("%s/ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=%s",
                    steamApiBaseUrl, steamApiKey, steamId);

            String response = restTemplate.getForObject(url, String.class);
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            JsonArray players = jsonResponse.getAsJsonObject("response")
                    .getAsJsonArray("players");

            if (players.size() > 0) {
                JsonObject player = players.get(0).getAsJsonObject();

                SteamProfileDto profile = new SteamProfileDto();
                profile.setSteamId(steamId);
                profile.setPersonaName(player.get("personaname").getAsString());
                profile.setProfileUrl(player.get("profileurl").getAsString());
                profile.setAvatarUrl(player.get("avatarfull").getAsString());

                return profile;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Steam profile: " + e.getMessage());
        }

        throw new RuntimeException("Steam profile not found");
    }

    /**
     * Sync user's Steam library
     */
    @Transactional
    public void syncSteamLibrary(String steamId) {
        try {
            List<SteamGameDto> games = getOwnedGames(steamId);

            // Delete old games
            steamGameRepository.deleteBySteamId(steamId);

            // Save new games
            for (SteamGameDto gameDto : games) {
                SteamGame game = new SteamGame();
                game.setSteamId(steamId);
                game.setAppId(gameDto.getAppId());
                game.setGameName(gameDto.getName());
                game.setPlaytimeForever(gameDto.getPlaytimeForever());
                game.setPlaytime2weeks(gameDto.getPlaytime2weeks());
                game.setImgIconUrl(gameDto.getImgIconUrl());
                game.setImgLogoUrl(gameDto.getImgLogoUrl());
                steamGameRepository.save(game);
            }

            // Update last synced time
            SteamAccount account = steamAccountRepository.findBySteamId(steamId)
                    .orElseThrow(() -> new RuntimeException("Steam account not found"));
            account.setLastSynced(LocalDateTime.now());
            steamAccountRepository.save(account);

        } catch (Exception e) {
            throw new RuntimeException("Failed to sync Steam library: " + e.getMessage());
        }
    }

    /**
     * Get owned games from Steam API
     */
    public List<SteamGameDto> getOwnedGames(String steamId) {
        List<SteamGameDto> games = new ArrayList<>();

        try {
            String url = String.format(
                    "%s/IPlayerService/GetOwnedGames/v0001/?key=%s&steamid=%s&include_appinfo=1&include_played_free_games=1",
                    steamApiBaseUrl, steamApiKey, steamId);

            String response = restTemplate.getForObject(url, String.class);
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            JsonObject responseObj = jsonResponse.getAsJsonObject("response");

            if (responseObj.has("games")) {
                JsonArray gamesArray = responseObj.getAsJsonArray("games");

                for (int i = 0; i < gamesArray.size(); i++) {
                    JsonObject gameObj = gamesArray.get(i).getAsJsonObject();

                    SteamGameDto game = new SteamGameDto();
                    game.setAppId(gameObj.get("appid").getAsLong());
                    game.setName(gameObj.get("name").getAsString());
                    game.setPlaytimeForever(gameObj.get("playtime_forever").getAsInt());

                    if (gameObj.has("playtime_2weeks")) {
                        game.setPlaytime2weeks(gameObj.get("playtime_2weeks").getAsInt());
                    }

                    String iconHash = gameObj.get("img_icon_url").getAsString();
                    String logoHash = gameObj.get("img_logo_url").getAsString();

                    game.setImgIconUrl(String.format("https://cdn.cloudflare.steamstatic.com/steamcommunity/public/images/apps/%d/%s.jpg",
                            game.getAppId(), iconHash));
                    game.setImgLogoUrl(String.format("https://cdn.cloudflare.steamstatic.com/steamcommunity/public/images/apps/%d/%s.jpg",
                            game.getAppId(), logoHash));

                    games.add(game);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch owned games: " + e.getMessage());
        }

        return games;
    }

    /**
     * Get user's Steam library from database
     */
    public List<SteamGame> getUserLibrary(String steamId) {
        return steamGameRepository.findBySteamId(steamId);
    }

    /**
     * Get Steam account by user
     */
    public Optional<SteamAccount> getSteamAccountByUser(User user) {
        return steamAccountRepository.findByUserId(user.getId());
    }
}
