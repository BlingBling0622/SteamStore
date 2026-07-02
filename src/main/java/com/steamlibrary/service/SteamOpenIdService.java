package com.steamlibrary.service;

import com.steamlibrary.model.User;
import com.steamlibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for handling Steam OpenID authentication.
 * Manages the OpenID authentication flow with Steam and user creation/linking.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SteamOpenIdService {

    private static final String STEAM_OPENID_URL = "https://steamcommunity.com/openid/login";
    private static final Pattern STEAM_ID_PATTERN = Pattern.compile("https://steamcommunity\\.com/openid/id/(\\d+)");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConsumerManager consumerManager;

    @Value("${app.base.url:http://localhost:8080}")
    private String appBaseUrl;

    @Value("${steam.openid.enabled:true}")
    private boolean steamOpenIdEnabled;

    /**
     * Initiates the Steam OpenID authentication process.
     *
     * @return The authentication URL to redirect the user to
     * @throws SteamAuthenticationException if OpenID request fails
     */
    public String initiateAuthentication() {
        if (!steamOpenIdEnabled) {
            log.warn("Steam OpenID is disabled");
            throw new SteamAuthenticationException("Steam OpenID authentication is currently disabled");
        }

        try {
            log.info("Initiating Steam OpenID authentication");

            // Discover Steam's OpenID endpoint
            List<?> discoveries = consumerManager.discover(STEAM_OPENID_URL);
            DiscoveryInformation discovered = consumerManager.associate(discoveries);

            // Store discovery information in session (in production, use session management)
            // For now, we'll rely on the stateless verification

            // Build the authentication request
            String returnUrl = appBaseUrl + "/auth/steam/callback";
            AuthRequest authRequest = consumerManager.authenticate(discovered, returnUrl);

            String authUrl = authRequest.getDestinationUrl(true);
            log.info("Steam OpenID authentication URL generated successfully");

            return authUrl;

        } catch (Exception e) {
            log.error("Failed to initiate Steam OpenID authentication", e);
            throw new SteamAuthenticationException("Failed to initiate Steam authentication: " + e.getMessage(), e);
        }
    }

    /**
     * Verifies the Steam OpenID response and authenticates/registers the user.
     *
     * @param receivingUrl The full URL that received the callback
     * @param parameterList The OpenID parameters from the callback
     * @return The authenticated or newly created User
     * @throws SteamAuthenticationException if verification fails
     */
    @Transactional
    public User verifyAndAuthenticateUser(String receivingUrl, ParameterList parameterList) {
        if (!steamOpenIdEnabled) {
            throw new SteamAuthenticationException("Steam OpenID authentication is currently disabled");
        }

        try {
            log.info("Verifying Steam OpenID response");

            // Verify the response
            VerificationResult verification = consumerManager.verify(
                receivingUrl,
                parameterList,
                null // Discovery information from session - null for stateless verification
            );

            // Get the verified identifier
            Identifier verified = verification.getVerifiedId();
            if (verified == null) {
                log.warn("Steam OpenID verification failed - no verified identifier");
                throw new SteamAuthenticationException("Steam authentication verification failed");
            }

            // Extract Steam ID from the identifier
            String steamId = extractSteamId(verified.getIdentifier());
            if (steamId == null) {
                log.error("Failed to extract Steam ID from identifier: {}", verified.getIdentifier());
                throw new SteamAuthenticationException("Invalid Steam ID format");
            }

            log.info("Steam OpenID verification successful for Steam ID: {}", steamId);

            // Find or create user
            User user = findOrCreateUserBySteamId(steamId);

            // Authenticate the user in Spring Security context
            authenticateUser(user);

            return user;

        } catch (MessageException e) {
            log.error("Failed to verify Steam OpenID response - message exception", e);
            throw new SteamAuthenticationException("Failed to verify Steam authentication: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during Steam OpenID verification", e);
            throw new SteamAuthenticationException("Unexpected error during Steam authentication", e);
        }
    }

    /**
     * Extracts the Steam ID from the OpenID identifier URL.
     *
     * @param identifier The OpenID identifier
     * @return The Steam ID, or null if extraction fails
     */
    private String extractSteamId(String identifier) {
        Matcher matcher = STEAM_ID_PATTERN.matcher(identifier);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Finds an existing user by Steam ID or creates a new one.
     *
     * @param steamId The Steam ID
     * @return The found or newly created User
     */
    @Transactional
    public User findOrCreateUserBySteamId(String steamId) {
        log.debug("Looking up user with Steam ID: {}", steamId);

        // Try to find existing user by Steam ID
        // Note: This assumes the User model has a steamId field
        // If not, you'll need to add it to the User entity
        Optional<User> existingUser = userRepository.findBySteamId(steamId);

        if (existingUser.isPresent()) {
            log.info("Found existing user for Steam ID: {}", steamId);
            return existingUser.get();
        }

        // Create new user with Steam ID
        log.info("Creating new user for Steam ID: {}", steamId);
        User newUser = new User();
        newUser.setUsername("steam_" + steamId); // Temporary username
        newUser.setEmail(steamId + "@steam.local"); // Placeholder email
        newUser.setPassword(passwordEncoder.encode(generateRandomPassword())); // Random password
        newUser.setSteamId(steamId);

        User savedUser = userRepository.save(newUser);
        log.info("Successfully created new user with ID: {} for Steam ID: {}", savedUser.getId(), steamId);

        return savedUser;
    }

    /**
     * Links an existing user account with a Steam ID.
     *
     * @param userId The user ID to link
     * @param steamId The Steam ID to link
     * @return The updated User
     * @throws SteamAuthenticationException if user not found or Steam ID already linked
     */
    @Transactional
    public User linkSteamIdToUser(Long userId, String steamId) {
        log.info("Linking Steam ID {} to user {}", steamId, userId);

        // Check if Steam ID is already linked to another user
        Optional<User> existingUser = userRepository.findBySteamId(steamId);
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
            throw new SteamAuthenticationException("This Steam account is already linked to another user");
        }

        // Find the user to link
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new SteamAuthenticationException("User not found"));

        // Link the Steam ID
        user.setSteamId(steamId);
        User updatedUser = userRepository.save(user);

        log.info("Successfully linked Steam ID {} to user {}", steamId, userId);
        return updatedUser;
    }

    /**
     * Unlinking a Steam ID from a user account.
     *
     * @param userId The user ID to unlink from
     * @return The updated User
     * @throws SteamAuthenticationException if user not found
     */
    @Transactional
    public User unlinkSteamIdFromUser(Long userId) {
        log.info("Unlinking Steam ID from user {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new SteamAuthenticationException("User not found"));

        user.setSteamId(null);
        User updatedUser = userRepository.save(user);

        log.info("Successfully unlinked Steam ID from user {}", userId);
        return updatedUser;
    }

    /**
     * Authenticates a user in the Spring Security context.
     *
     * @param user The user to authenticate
     */
    private void authenticateUser(User user) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            user.getUsername(),
            null,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Mark the user online immediately after a successful Steam authentication
        userRepository.updateLastSeenAt(user.getId(), java.time.LocalDateTime.now());
        log.info("User {} authenticated in security context", user.getUsername());
    }

    /**
     * Generates a random password for Steam-authenticated users.
     *
     * @return A random password string
     */
    private String generateRandomPassword() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * Custom exception for Steam authentication errors.
     */
    public static class SteamAuthenticationException extends RuntimeException {
        public SteamAuthenticationException(String message) {
            super(message);
        }

        public SteamAuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
