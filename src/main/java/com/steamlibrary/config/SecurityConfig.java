package com.steamlibrary.config;

import com.steamlibrary.model.User;
import com.steamlibrary.service.CustomUserDetailsService;
import com.steamlibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider) throws Exception {
        http
            .authenticationProvider(authenticationProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/store", "/store/**", "/register", "/login", "/css/**", "/js/**", "/images/**", "/debug/**").permitAll()
                .requestMatchers("/auth/steam/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/friends/**").authenticated()
                .requestMatchers("/messages/**").authenticated()
                .requestMatchers("/groups/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    try {
                        User u = userService.findByUsername(authentication.getName());
                        if (u != null) userService.updateLastSeenAt(u.getId());
                    } catch (Exception ignored) {}
                    response.sendRedirect("/dashboard");
                })
                .permitAll()
            )
            .logout(logout -> logout
                .addLogoutHandler((request, response, authentication) -> {
                    try {
                        if (authentication != null) {
                            User u = userService.findByUsername(authentication.getName());
                            if (u != null) userService.clearLastSeenAt(u.getId());
                        }
                    } catch (Exception ignored) {}
                })
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/store/**", "/debug/**", "/dashboard/favorite", "/cart/**", "/checkout", "/friends/**", "/messages/**", "/groups/**")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
