package com.steamlibrary.service;

import com.google.gson.*;
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
import java.util.*;

/**
 * Fetches real customer reviews from the Steam store reviews endpoint
 * (https://store.steampowered.com/appreviews/{appId}).
 *
 * The appreviews response already carries each author's persona name,
 * profile url and avatar hash inside its "author" object, so no extra
 * GetPlayerSummaries call (and no API key) is required.
 */
@Service
@Slf4j
public class SteamReviewService {

    private static final String AVATAR_BASE = "https://avatars.steamstatic.com/";

    /** Browser-like UA — Steam is happier with it and it avoids some edge blocking. */
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Safari/537.36";

    /**
     * Base URL of the Steam appreviews endpoint. Point this at a reverse proxy / mirror
     * (e.g. a Cloudflare Worker or nginx on an overseas VPS) when store.steampowered.com
     * is unreachable from the server's network. Must end with "/".
     */
    @Value("${steam.reviews.base-url:https://store.steampowered.com/appreviews/}")
    private String reviewsBaseUrl;

    /** Optional explicit proxy "host:port". When blank the OS system proxy is used (see main()). */
    @Value("${steam.http.proxy:}")
    private String httpProxy;

    private HttpClient client;

    private HttpClient client() {
        if (client != null) return client;
        HttpClient.Builder b = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15));
        String proxy = httpProxy == null ? "" : httpProxy.trim();
        if (!proxy.isEmpty()) {
            String host = proxy;
            int port = 7897;
            int colon = proxy.lastIndexOf(':');
            if (colon > 0) {
                host = proxy.substring(0, colon);
                try { port = Integer.parseInt(proxy.substring(colon + 1)); } catch (NumberFormatException ignore) {}
            }
            log.info("Steam HTTP client using explicit proxy {}:{}", host, port);
            b.proxy(ProxySelector.of(new InetSocketAddress(host, port)));
        } else {
            // No explicit proxy configured: fall back to the OS system proxy (WinINet on Windows,
            // e.g. a local Clash/Mihomo proxy). Set here — not only in main() — so it still takes
            // effect after a Spring DevTools hot restart that doesn't re-run main().
            System.setProperty("java.net.useSystemProxies", "true");
            log.info("Steam HTTP client using OS system proxy (java.net.useSystemProxies=true)");
        }
        return client = b.build();
    }

    /**
     * Fetch one page of reviews for a Steam app.
     *
     * @param appId       Steam application id (e.g. 1245620)
     * @param numPerPage  page size (capped to 1..20)
     * @param filter      "recent" | "updated" | "all"
     * @param reviewType  "all" | "positive" | "negative"
     * @param language    Steam language code, e.g. "english" (null/blank -> "english")
     * @param cursor      pagination cursor from a previous response; null/"*" for first page
     */
    public Map<String, Object> fetchReviews(int appId, int numPerPage, String filter,
                                            String reviewType, String language, String cursor) {
        int count = Math.max(1, Math.min(20, numPerPage));
        String safeFilter = (filter == null || filter.isBlank()) ? "recent" : filter;
        String safeReviewType = (reviewType == null || reviewType.isBlank()) ? "all" : reviewType;
        // "all" or blank → pass empty to Steam, which returns reviews in every language.
        // Anything else (e.g. "english", "schinese,tchinese") is forwarded verbatim — Steam
        // appreviews accepts a single code or a comma-separated list.
        String safeLang = (language == null || language.isBlank() || "all".equalsIgnoreCase(language.trim()))
                ? "" : language.trim();
        String safeCursor = (cursor == null || cursor.isBlank()) ? "*" : cursor;

        String url = reviewsBaseUrl + appId
                + "?json=1"
                + "&language=" + URLEncoder.encode(safeLang, StandardCharsets.UTF_8)
                + "&filter=" + safeFilter
                + "&review_type=" + safeReviewType
                + "&purchase_type=all"
                + "&num_per_page=" + count
                + "&cursor=" + URLEncoder.encode(safeCursor, StandardCharsets.UTF_8);
        log.info("Steam reviews request: app={} filter={} type={} cursor={}", appId, safeFilter, safeReviewType, safeCursor);

        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(15))
                    .header("User-Agent", USER_AGENT)
                    .header("Accept", "application/json, text/plain, */*")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .GET()
                    .build();
            HttpResponse<String> resp = client().send(req, HttpResponse.BodyHandlers.ofString());
            int status = resp.statusCode();
            String body = resp.body() == null ? "" : resp.body();

            if (status != 200) {
                log.warn("Steam appreviews HTTP {} for app {} (body len={}): {}", status, appId, body.length(), snippet(body, 300));
                return errorResult("Steam returned HTTP " + status);
            }

            // Make sure we actually got JSON — Steam sometimes serves an HTML age-gate / region page.
            String trimmed = body.trim();
            if (!trimmed.startsWith("{") && !trimmed.startsWith("[")) {
                log.warn("Steam appreviews returned non-JSON for app {} (len={}): {}", appId, body.length(), snippet(body, 300));
                return errorResult("Steam returned a non-JSON response (possibly an age-gate or region page).");
            }

            JsonObject json = JsonParser.parseString(body).getAsJsonObject();

            // Steam reports {"success":0,...} when the app has no review endpoint / is invalid.
            int success = jsonInt(json, "success", 1);
            if (success == 0) {
                log.warn("Steam appreviews success=0 for app {}: {}", appId, snippet(body, 300));
                return errorResult("Steam reported success=0 for this app.");
            }

            // ---- Summary (full totals only on the first page) ----
            Map<String, Object> summary = new LinkedHashMap<>();
            if (json.has("query_summary") && json.get("query_summary").isJsonObject()) {
                JsonObject qs = json.getAsJsonObject("query_summary");
                summary.put("score_desc", jsonStr(qs, "review_score_desc", ""));
                summary.put("score", jsonInt(qs, "review_score", -1));
                summary.put("total_positive", jsonInt(qs, "total_positive", 0));
                summary.put("total_negative", jsonInt(qs, "total_negative", 0));
                summary.put("total_reviews", jsonInt(qs, "total_reviews", 0));
            }
            String scoreDesc = (String) summary.getOrDefault("score_desc", "");
            Object totalObj = summary.get("total_reviews");
            int totalReviews = totalObj instanceof Number ? ((Number) totalObj).intValue() : 0;
            log.info("Steam reviews OK for app {}: score='{}' total={} ", appId, scoreDesc, totalReviews);

            // ---- Cursor for the next page ----
            String nextCursor = jsonStr(json, "cursor", null);

            // ---- Reviews ----
            JsonArray arr = (json.has("reviews") && json.get("reviews").isJsonArray())
                    ? json.getAsJsonArray("reviews") : new JsonArray();
            List<Map<String, Object>> reviews = new ArrayList<>();
            for (int i = 0; i < arr.size(); i++) {
                reviews.add(parseReview(arr.get(i).getAsJsonObject()));
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("summary", summary);
            result.put("total", totalReviews); // backward-compat
            result.put("reviews", reviews);
            result.put("cursor", nextCursor);
            result.put("has_more", !reviews.isEmpty() && nextCursor != null && !nextCursor.equals(safeCursor));
            result.put("error", null);
            return result;
        } catch (java.io.IOException e) {
            // Covers ConnectException, SocketTimeoutException, HttpConnectTimeoutException, etc.
            log.warn("Steam appreviews network/IO error for app {}: {} {}", appId, e.getClass().getSimpleName(), e.getMessage());
            return errorResult("Network error reaching Steam (" + e.getClass().getSimpleName()
                    + "). The server may be unable to reach store.steampowered.com (a broken IPv6 path is a common cause).");
        } catch (Exception e) {
            log.warn("Steam appreviews failed for app {}: {} {}", appId, e.getClass().getSimpleName(), e.getMessage(), e);
            return errorResult("Failed to load reviews: " + e.getMessage());
        }
    }

    /** Backward-compatible overload. */
    public Map<String, Object> fetchReviews(int appId, int count) {
        return fetchReviews(appId, count, "recent", "all", "english", "*");
    }

    private Map<String, Object> parseReview(JsonObject r) {
        JsonObject author = (r.has("author") && r.get("author").isJsonObject())
                ? r.getAsJsonObject("author") : new JsonObject();

        String personaName = jsonStr(author, "personaname", null);
        String profileUrl = jsonStr(author, "profile_url", null);
        String avatarHash = jsonStr(author, "avatar", null);
        String avatarUrl = (avatarHash != null && avatarHash.length() > 4)
                ? AVATAR_BASE + avatarHash + "_medium.jpg" : null;
        String steamId = jsonStr(author, "steamid", null);

        Map<String, Object> review = new LinkedHashMap<>();
        review.put("author", personaName != null && !personaName.isEmpty() ? personaName : "Steam User");
        review.put("avatar", avatarUrl);
        review.put("profile_url", profileUrl);
        review.put("steamid", steamId);
        review.put("content", jsonStr(r, "review", ""));
        review.put("voted_up", jsonBool(r, "voted_up", false));
        review.put("created", jsonLong(r, "timestamp_created", 0L));
        review.put("updated", jsonLong(r, "timestamp_updated", 0L));
        review.put("hours", jsonDouble(author, "playtime_forever", 0.0) / 60.0);
        review.put("hours_at_review", jsonDouble(author, "playtime_at_review", 0.0) / 60.0);
        review.put("helpful", jsonInt(r, "votes_up", 0));
        review.put("funny", jsonInt(r, "votes_funny", 0));
        review.put("comments", jsonInt(r, "comment_count", 0));
        review.put("early_access", jsonBool(r, "written_during_early_access", false));
        review.put("received_for_free", jsonBool(r, "received_for_free", false));
        review.put("steam_purchase", jsonBool(r, "steam_purchase", false));
        review.put("language", jsonStr(r, "language", ""));
        return review;
    }

    /** Result shape when the call itself failed — keeps reviews empty but exposes a reason. */
    private Map<String, Object> errorResult(String reason) {
        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("score_desc", "");
        summary.put("score", -1);
        summary.put("total_positive", 0);
        summary.put("total_negative", 0);
        summary.put("total_reviews", 0);
        result.put("summary", summary);
        result.put("total", 0);
        result.put("reviews", List.of());
        result.put("cursor", null);
        result.put("has_more", false);
        result.put("error", reason);
        return result;
    }

    private static String snippet(String s, int max) {
        if (s == null) return "";
        String oneLine = s.replaceAll("\\s+", " ").trim();
        return oneLine.length() <= max ? oneLine : oneLine.substring(0, max) + "...";
    }

    // ---- Gson helpers (the API mixes primitives/objects/nulls) ----
    private String jsonStr(JsonObject o, String key, String def) {
        try {
            JsonElement el = o.get(key);
            if (el == null || el.isJsonNull()) return def;
            return el.isJsonPrimitive() ? el.getAsString() : def;
        } catch (Exception e) { return def; }
    }
    private int jsonInt(JsonObject o, String key, int def) {
        try {
            if (o == null) return def;
            JsonElement el = o.get(key);
            if (el == null || el.isJsonNull()) return def;
            return el.isJsonPrimitive() ? el.getAsInt() : def;
        } catch (Exception e) { return def; }
    }
    private long jsonLong(JsonObject o, String key, long def) {
        try {
            JsonElement el = o.get(key);
            if (el == null || el.isJsonNull()) return def;
            return el.isJsonPrimitive() ? el.getAsLong() : def;
        } catch (Exception e) { return def; }
    }
    private double jsonDouble(JsonObject o, String key, double def) {
        try {
            JsonElement el = o.get(key);
            if (el == null || el.isJsonNull()) return def;
            return el.isJsonPrimitive() ? el.getAsDouble() : def;
        } catch (Exception e) { return def; }
    }
    private boolean jsonBool(JsonObject o, String key, boolean def) {
        try {
            JsonElement el = o.get(key);
            if (el == null || el.isJsonNull()) return def;
            return el.isJsonPrimitive() ? el.getAsBoolean() : def;
        } catch (Exception e) { return def; }
    }
}
