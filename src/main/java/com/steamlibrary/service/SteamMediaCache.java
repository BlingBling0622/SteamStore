package com.steamlibrary.service;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Caches Steam media (screenshots, trailers, backgrounds) to a JSON file.
 * Once fetched, data survives app restarts — no more network dependency.
 */
@Service
@Slf4j
public class SteamMediaCache {

    private static final String CACHE_FILE = "steam-media-cache.json";
    private static final Path EXTERNAL_CACHE = Paths.get("steam-media-cache.json");

    private final Map<String, CachedMedia> cache = new LinkedHashMap<>();
    private boolean dirty = false;

    public SteamMediaCache() {
        load();
    }

    /** Check if we already have cached data for this app ID */
    public boolean has(String appId) {
        return cache.containsKey(appId);
    }

    /** Get cached media for an app ID */
    public CachedMedia get(String appId) {
        return cache.get(appId);
    }

    /** Store media in the cache (marks as dirty, will be saved to disk) */
    public void put(String appId, CachedMedia media) {
        cache.put(appId, media);
        dirty = true;
    }

    /** Persist the cache to disk */
    public synchronized void save() {
        if (!dirty) return;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.writeString(EXTERNAL_CACHE, gson.toJson(new ArrayList<>(cache.values())));
            log.info("Saved {} entries to {}", cache.size(), EXTERNAL_CACHE);
            dirty = false;
        } catch (IOException e) {
            log.error("Failed to save Steam media cache: {}", e.getMessage());
        }
    }

    /** Load cache from disk, preferring external (updatable) over classpath (JAR-bundled) */
    private void load() {
        String json = null;
        String source = null;

        // 1) External file — can be updated at runtime
        try {
            if (Files.exists(EXTERNAL_CACHE)) {
                json = Files.readString(EXTERNAL_CACHE);
                source = "external (" + EXTERNAL_CACHE + ")";
            }
        } catch (IOException e) { /* ignore */ }

        // 2) Classpath — bundled in JAR, never lost
        if (json == null) {
            try {
                ClassPathResource resource = new ClassPathResource(CACHE_FILE);
                if (resource.exists()) {
                    json = new String(resource.getInputStream().readAllBytes());
                    source = "classpath (bundled in JAR)";
                }
            } catch (IOException e) { /* ignore */ }
        }

        if (json != null) {
            try {
                CachedMedia[] entries = new Gson().fromJson(json, CachedMedia[].class);
                for (CachedMedia entry : entries) {
                    cache.put(entry.appId, entry);
                }
                log.info("📦 Loaded {} cached Steam media entries from {}", cache.size(), source);
            } catch (Exception e) {
                log.warn("Failed to parse cache: {}", e.getMessage());
            }
        } else {
            log.info("No Steam media cache found — will fetch from API");
        }
    }

    /** A single cached game's media data */
    public static class CachedMedia {
        public String appId;
        public String backgroundUrl;
        public List<String> screenshots = new ArrayList<>();
        public List<String> trailers = new ArrayList<>();
        public String youtubeVideoId; // comma-separated YouTube IDs
        public Integer reviewPositive;
        public Integer reviewNegative;
        public Integer reviewTotal;
        public Integer reviewScore;
        public String aboutTheGame;

        public static CachedMedia from(String appId, SteamAssetService.SteamMedia media) {
            CachedMedia cm = new CachedMedia();
            cm.appId = appId;
            cm.backgroundUrl = media.backgroundUrl();
            cm.screenshots = media.screenshotUrls();
            cm.trailers = media.trailerWebmUrls();
            cm.aboutTheGame = media.aboutTheGame();
            return cm;
        }
    }
}
