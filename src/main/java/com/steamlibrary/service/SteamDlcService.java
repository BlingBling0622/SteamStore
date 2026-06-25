package com.steamlibrary.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.steamlibrary.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.*;

/**
 * Fetches real DLC data from Steam API for popular games.
 */
@Service
@Slf4j
public class SteamDlcService {

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(8))
            .build();

    // Popular games to fetch DLCs for
    private static final List<Long> POPULAR_APP_IDS = List.of(
            1245620L, // Elden Ring
            1091500L, // Cyberpunk 2077
            292030L,  // Witcher 3
            489830L,  // Skyrim
            1174180L, // RDR2
            1086940L, // Baldur's Gate 3
            271590L,  // GTA V
            730L      // CS2
    );

    public List<Product> fetchDlcs() {
        List<Product> dlcs = new ArrayList<>();
        for (Long appId : POPULAR_APP_IDS) {
            try {
                List<Product> gameDlcs = fetchDlcsForGame(appId);
                dlcs.addAll(gameDlcs);
                if (!gameDlcs.isEmpty()) {
                    log.info("Fetched {} DLCs for app {}", gameDlcs.size(), appId);
                }
            } catch (Exception e) {
                log.debug("Could not fetch DLCs for {}: {}", appId, e.getMessage());
            }
        }
        return dlcs;
    }

    private List<Product> fetchDlcsForGame(Long appId) throws Exception {
        // First get the DLC list from the game's appdetails
        String url = "http://store.steampowered.com/api/appdetails?appids=" + appId + "&cc=us";
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url))
                .timeout(Duration.ofSeconds(8))
                .header("User-Agent", "Mozilla/5.0")
                .GET().build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) return List.of();

        JsonObject root = JsonParser.parseString(resp.body()).getAsJsonObject();
        JsonObject data = root.getAsJsonObject(String.valueOf(appId)).getAsJsonObject("data");
        if (data == null || !data.has("dlc")) return List.of();

        JsonArray dlcArray = data.getAsJsonArray("dlc");
        List<Product> dlcs = new ArrayList<>();

        // Fetch details for each DLC (up to 20)
        for (int i = 0; i < Math.min(dlcArray.size(), 20); i++) {
            Long dlcAppId = dlcArray.get(i).getAsLong();
            try {
                Product dlc = fetchDlcDetail(dlcAppId, appId);
                if (dlc != null) dlcs.add(dlc);
            } catch (Exception e) {
                log.debug("Skip DLC {}: {}", dlcAppId, e.getMessage());
            }
        }
        return dlcs;
    }

    private Product fetchDlcDetail(Long dlcAppId, Long parentAppId) throws Exception {
        String url = "http://store.steampowered.com/api/appdetails?appids=" + dlcAppId + "&cc=us";
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url))
                .timeout(Duration.ofSeconds(8))
                .header("User-Agent", "Mozilla/5.0")
                .GET().build();
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) return null;

        JsonObject root = JsonParser.parseString(resp.body()).getAsJsonObject();
        JsonObject data = root.getAsJsonObject(String.valueOf(dlcAppId)).getAsJsonObject("data");
        if (data == null || !data.has("name")) return null;

        Product p = new Product();
        p.setName(data.get("name").getAsString());
        p.setDescription(data.has("short_description") ? data.get("short_description").getAsString() : "DLC for the base game");
        p.setPrice(data.has("price_overview") ? data.getAsJsonObject("price_overview").get("final").getAsDouble() / 100.0 : 9.99);
        p.setDiscountPercent(data.has("price_overview") && data.getAsJsonObject("price_overview").has("discount_percent")
                ? data.getAsJsonObject("price_overview").get("discount_percent").getAsInt() : 0);
        p.setHeaderImageUrl("https://cdn.akamai.steamstatic.com/steam/apps/" + dlcAppId + "/header.jpg");
        p.setCapsuleImageUrl("https://cdn.akamai.steamstatic.com/steam/apps/" + dlcAppId + "/capsule_616x353.jpg");
        p.setIsDlc(true);
        p.setParentGameId(parentAppId);
        p.setDeveloper(data.has("developers") && data.getAsJsonArray("developers").size() > 0
                ? data.getAsJsonArray("developers").get(0).getAsString() : "");
        p.setPublisher(data.has("publishers") && data.getAsJsonArray("publishers").size() > 0
                ? data.getAsJsonArray("publishers").get(0).getAsString() : "");
        p.setTags("DLC");
        p.setFeatured(false);
        p.setAboutTheGame(data.has("short_description") ? data.get("short_description").getAsString() : "");
        return p;
    }
}
