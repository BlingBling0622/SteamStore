package com.steamlibrary.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Service that fetches real screenshots, trailers, and background images
 * from Steam's store API. Falls back gracefully when Steam is unreachable.
 */
@Service
@Slf4j
public class SteamAssetService {

    private static final int TIMEOUT_SECONDS = 8;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    /** Holds the real media URLs extracted from Steam's API for a single game. */
    public record SteamMedia(
            String backgroundUrl,
            List<String> screenshotUrls,
            List<String> trailerWebmUrls,
            String aboutTheGame,
            String detailedDescription,
            Integer reviewPositive,
            Integer reviewNegative,
            Integer reviewTotal,
            Integer reviewScore
    ) {}

    /**
     * Fetch real media for a game from Steam's store API.
     * Uses Steam China API (accessible from China), falls back to international.
     *
     * @param appId the Steam application ID (e.g. 1245620 for Elden Ring)
     * @return SteamMedia with real URLs, or null if all APIs are unreachable
     */
    public SteamMedia fetchMedia(String appId) {
        // Try HTTP first (bypasses SNI filtering that blocks HTTPS)
        SteamMedia media = tryFetch("http://store.steampowered.com/api/appdetails?appids=" + appId, appId);
        if (media != null) {
            return media;
        }

        // HTTPS fallback
        media = tryFetch("https://store.steampowered.com/api/appdetails?appids=" + appId, appId);
        if (media != null) {
            return media;
        }

        // Steam China as last resort
        media = tryFetch("https://store.steamchina.com/api/appdetails?appids=" + appId, appId);
        if (media != null) {
            return media;
        }

        return null;
    }

    private SteamMedia tryFetch(String url, String appId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return null;
            }

            return parseResponse(response.body(), appId);

        } catch (IOException e) {
            // Network unreachable — expected on blocked networks
            log.debug("Steam API unreachable ({}): {}", url, e.getMessage());
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (Exception e) {
            log.warn("Unexpected error fetching Steam data for app {}: {}", appId, e.getMessage());
            return null;
        }
    }

    private SteamMedia parseResponse(String json, String appId) {
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonObject appData = root.getAsJsonObject(appId);

            if (appData == null || !appData.has("success") || !appData.get("success").getAsBoolean()) {
                return null;
            }

            JsonObject data = appData.getAsJsonObject("data");

            // Background image
            String background = data.has("background_raw")
                    ? data.get("background_raw").getAsString()
                    : data.has("background")
                            ? data.get("background").getAsString()
                            : null;

            // Screenshots
            List<String> screenshots = new ArrayList<>();
            if (data.has("screenshots")) {
                JsonArray ssArray = data.getAsJsonArray("screenshots");
                for (JsonElement ss : ssArray) {
                    JsonObject ssObj = ss.getAsJsonObject();
                    if (ssObj.has("path_full")) {
                        screenshots.add(ssObj.get("path_full").getAsString());
                    } else if (ssObj.has("path_thumbnail")) {
                        screenshots.add(ssObj.get("path_thumbnail").getAsString());
                    }
                }
            }

            // Trailer / movie WebM URLs
            List<String> trailers = new ArrayList<>();
            if (data.has("movies")) {
                JsonArray movies = data.getAsJsonArray("movies");
                for (JsonElement m : movies) {
                    JsonObject movie = m.getAsJsonObject();
                    if (movie.has("webm")) {
                        JsonObject webm = movie.getAsJsonObject("webm");
                        // Prefer 480p, fall back to max quality
                        if (webm.has("480")) {
                            trailers.add(webm.get("480").getAsString());
                        } else if (webm.has("max")) {
                            trailers.add(webm.get("max").getAsString());
                        }
                    }
                    if (movie.has("mp4")) {
                        JsonObject mp4 = movie.getAsJsonObject("mp4");
                        if (mp4.has("480") && trailers.size() < 2) {
                            trailers.add(mp4.get("480").getAsString());
                        }
                    }
                }
            }

            // Description
            String aboutTheGame = data.has("about_the_game")
                    ? data.get("about_the_game").getAsString()
                    : null;

            String detailedDesc = data.has("detailed_description")
                    ? data.get("detailed_description").getAsString()
                    : null;

            // Review data
            Integer reviewPositive = null, reviewNegative = null, reviewTotal = null, reviewScore = null;
            if (data.has("recommendations")) {
                JsonObject rec = data.getAsJsonObject("recommendations");
                if (rec.has("total")) reviewTotal = rec.get("total").getAsInt();
            }
            if (data.has("metacritic")) {
                JsonObject mc = data.getAsJsonObject("metacritic");
                if (mc.has("score")) reviewScore = mc.get("score").getAsInt();
            }

            return new SteamMedia(background, screenshots, trailers, aboutTheGame, detailedDesc,
                    reviewPositive, reviewNegative, reviewTotal, reviewScore);

        } catch (Exception e) {
            log.warn("Failed to parse Steam API response for app {}: {}", appId, e.getMessage());
            return null;
        }
    }
}
