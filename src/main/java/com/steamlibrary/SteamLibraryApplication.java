package com.steamlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SteamLibraryApplication {

    public static void main(String[] args) {
        // Use the OS-level proxy settings for outbound HTTP. On Windows this reads the
        // WinINet/Internet Options proxy (e.g. a local Clash/Mihomo proxy on 127.0.0.1:7897
        // commonly used to reach Steam in regions where store.steampowered.com is blocked).
        // Without this, Java's HttpClient connects directly and the Steam reviews / API
        // integrations time out. Override per-client with the steam.http.proxy property.
        System.setProperty("java.net.useSystemProxies", "true");

        SpringApplication.run(SteamLibraryApplication.class, args);
    }
}
