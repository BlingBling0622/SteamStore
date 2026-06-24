package com.steamlibrary.config;

import lombok.extern.slf4j.Slf4j;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenID authentication components.
 */
@Configuration
@Slf4j
public class OpenIdConfig {

    /**
     * Creates and configures the ConsumerManager for OpenID authentication.
     * This bean is used by the SteamOpenIdService to handle Steam authentication.
     *
     * @return Configured ConsumerManager instance
     * @throws ConsumerException if ConsumerManager initialization fails
     */
    @Bean
    public ConsumerManager consumerManager() throws ConsumerException {
        log.info("Initializing OpenID ConsumerManager");

        ConsumerManager manager = new ConsumerManager();

        // Configure the consumer manager for better reliability
        manager.setMaxAssocAttempts(0); // Don't use association (stateless mode)
        manager.setImmediateAuth(false); // Allow user interaction

        log.info("OpenID ConsumerManager initialized successfully");
        return manager;
    }
}
