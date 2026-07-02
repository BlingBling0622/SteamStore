package com.steamlibrary.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Searches Giphy for GIFs via the public Giphy API v1.
 *
 * <p>Reuses the same outbound HTTP proxy as the Steam integration
 * ({@code steam.http.proxy}) so it works from networks where api.giphy.com is
 * not directly reachable. The returned GIF URLs (media.giphy.com) are loaded
 * directly by the browser when rendering a message.</p>
 */
@Service
@Slf4j
public class GiphyService {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Safari/537.36";

    private static final String SEARCH_URL = "https://api.giphy.com/v1/gifs/search";

    /**
     * Giphy API key. The public beta key {@code dc6zaTOxFJmzC} is used by
     * default; set {@code giphy.api.key} in application.properties to use your
     * own (recommended for production — register at developers.giphy.com).
     */
    @Value("${giphy.api.key:dc6zaTOxFJmzC}")
    private String apiKey;

    /** Same proxy used for Steam outbound calls. */
    @Value("${steam.http.proxy:}")
    private String httpProxy;

    private HttpClient client;

    private HttpClient client() {
        if (client != null) return client;
        HttpClient.Builder b = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10));
        String proxy = httpProxy == null ? "" : httpProxy.trim();
        if (!proxy.isEmpty()) {
            String host = proxy;
            int port = 7897;
            int colon = proxy.lastIndexOf(':');
            if (colon > 0) {
                host = proxy.substring(0, colon);
                try { port = Integer.parseInt(proxy.substring(colon + 1)); } catch (NumberFormatException ignore) {}
            }
            log.info("Giphy HTTP client using proxy {}:{}", host, port);
            b.proxy(ProxySelector.of(new InetSocketAddress(host, port)));
        }
        return client = b.build();
    }

    /**
     * Search Giphy and return a list of GIFs.
     *
     * @param query search term
     * @param limit max results (capped to 1..24)
     * @return list of maps, each with {@code id}, {@code url} (200px tall GIF to send)
     *         and {@code preview} (100px tall GIF for the picker thumbnail)
     */
    public List<Map<String, Object>> search(String query, int limit) {
        return search(query, limit, 0);
    }

    /**
     * Search Giphy with pagination support.
     *
     * @param offset 0-based offset for paging — the "load more" button increments
     *               this by {@code limit} to fetch the next page of results.
     */
    public List<Map<String, Object>> search(String query, int limit, int offset) {
        int count = Math.max(1, Math.min(24, limit));
        String q = query == null ? "" : query.trim();
        if (q.isEmpty()) return List.of();

        try {
            String url = SEARCH_URL
                    + "?api_key=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8)
                    + "&q=" + URLEncoder.encode(q, StandardCharsets.UTF_8)
                    + "&limit=" + count
                    + "&offset=" + Math.max(0, offset)
                    + "&rating=pg";
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("User-Agent", USER_AGENT)
                    .header("Accept", "application/json")
                    .GET().build();
            HttpResponse<String> resp = client().send(req, HttpResponse.BodyHandlers.ofString());

            if (resp.statusCode() != 200) {
                log.warn("Giphy search HTTP {} for q='{}' (body: {})", resp.statusCode(), q, snippet(resp.body(), 200));
                return List.of();
            }

            String body = resp.body() == null ? "" : resp.body();
            if (!body.trim().startsWith("{")) {
                log.warn("Giphy search returned non-JSON for q='{}': {}", q, snippet(body, 200));
                return List.of();
            }

            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            JsonArray data = (json.has("data") && json.get("data").isJsonArray())
                    ? json.getAsJsonArray("data") : new JsonArray();

            List<Map<String, Object>> out = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                JsonObject g = data.get(i).getAsJsonObject();
                String gifUrl = imgUrl(g, "fixed_height", "downsized");
                String previewUrl = imgUrl(g, "fixed_height_small", "fixed_height", "downsized");
                if (gifUrl.isEmpty()) gifUrl = previewUrl;
                if (gifUrl.isEmpty()) continue;

                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", jsonStr(g, "id", ""));
                m.put("url", gifUrl);
                m.put("preview", previewUrl.isEmpty() ? gifUrl : previewUrl);
                out.add(m);
            }
            log.info("Giphy search q='{}' → {} results", q, out.size());
            return out;
        } catch (java.io.IOException e) {
            log.warn("Giphy search network error for q='{}': {} {}", q, e.getClass().getSimpleName(), e.getMessage());
            return List.of();
        } catch (Exception e) {
            log.warn("Giphy search failed for q='{}': {} {}", q, e.getClass().getSimpleName(), e.getMessage(), e);
            return List.of();
        }
    }

    /** Resolve a GIF URL from the nested {@code images} object, trying keys in order. */
    private String imgUrl(JsonObject gif, String... imageKeys) {
        try {
            if (!gif.has("images") || !gif.get("images").isJsonObject()) return "";
            JsonObject imgs = gif.getAsJsonObject("images");
            for (String key : imageKeys) {
                if (imgs.has(key) && imgs.get(key).isJsonObject()) {
                    JsonObject img = imgs.getAsJsonObject(key);
                    String u = jsonStr(img, "url", "");
                    if (!u.isEmpty()) return u;
                }
            }
        } catch (Exception ignore) {}
        return "";
    }

    private static String snippet(String s, int max) {
        if (s == null) return "";
        String one = s.replaceAll("\\s+", " ").trim();
        return one.length() <= max ? one : one.substring(0, max) + "...";
    }

    private String jsonStr(JsonObject o, String key, String def) {
        try {
            JsonElement el = o.get(key);
            if (el == null || el.isJsonNull()) return def;
            return el.isJsonPrimitive() ? el.getAsString() : def;
        } catch (Exception e) { return def; }
    }
}
