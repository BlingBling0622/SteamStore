package com.steamlibrary.service;

import jakarta.servlet.http.HttpServletRequest;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.ParameterList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
public class SteamOpenIdService {

    private final ConsumerManager manager;

    @Value("${app.base.url}")
    private String appBaseUrl;

    @Value("${steam.openid.url}")
    private String steamOpenIdUrl;

    public SteamOpenIdService() {
        // Initialize SSL context to trust all certificates
        // NOTE: This is for development only. In production, use proper SSL certificates.
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create a hostname verifier that accepts all hostnames
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            // Log but don't fail - will try without SSL override
            System.err.println("Warning: Could not initialize SSL override: " + e.getMessage());
        }

        this.manager = new ConsumerManager();
        this.manager.setMaxAssocAttempts(0); // Disable association for better compatibility
    }

    /**
     * Generate Steam OpenID login URL
     */
    public String getLoginUrl() {
        try {
            String returnUrl = appBaseUrl + "/steam/callback";

            List discoveries = manager.discover(steamOpenIdUrl);
            DiscoveryInformation discovered = manager.associate(discoveries);

            AuthRequest authReq = manager.authenticate(discovered, returnUrl);

            return authReq.getDestinationUrl(true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Steam login URL: " + e.getMessage());
        }
    }

    /**
     * Verify Steam OpenID response and extract Steam ID
     */
    public String verifySteamLogin(HttpServletRequest request) {
        try {
            ParameterList parameterList = new ParameterList(request.getParameterMap());

            String receivingURL = appBaseUrl + "/steam/callback";
            String queryString = request.getQueryString();
            if (queryString != null && queryString.length() > 0) {
                receivingURL += "?" + queryString;
            }

            VerificationResult verification = manager.verify(
                    receivingURL,
                    parameterList,
                    null
            );

            Identifier verified = verification.getVerifiedId();
            if (verified != null) {
                String steamId = extractSteamId(verified.getIdentifier());
                return steamId;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify Steam login: " + e.getMessage());
        }

        throw new RuntimeException("Steam login verification failed");
    }

    /**
     * Extract Steam ID from OpenID identifier
     */
    private String extractSteamId(String identifier) {
        // Steam OpenID format: https://steamcommunity.com/openid/id/{steamid64}
        String[] parts = identifier.split("/");
        return parts[parts.length - 1];
    }
}
