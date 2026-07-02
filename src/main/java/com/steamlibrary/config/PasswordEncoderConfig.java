package com.steamlibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Standalone configuration for the {@link PasswordEncoder} bean.
 *
 * <p>Previously this bean lived inside {@link SecurityConfig}. That caused a circular
 * dependency: {@code SecurityConfig} → {@code UserService} (injected for login/logout
 * activity tracking) → {@code PasswordEncoder} (the @Bean defined in SecurityConfig) →
 * back to {@code SecurityConfig}. Moving the bean here breaks the cycle while still
 * letting {@link SecurityConfig#authenticationProvider} receive the encoder via
 * parameter injection.</p>
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
