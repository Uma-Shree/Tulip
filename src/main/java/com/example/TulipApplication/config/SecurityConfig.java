package com.example.TulipApplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for React interaction
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to products and categories
                        .requestMatchers("/api/auth/**", "/api/products/**").permitAll()

                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        // Allow public access to login/register (we will build these next)
                        .requestMatchers("/api/supplier/**").hasAnyAuthority("SUPPLIER", "CUSTOMER")
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // This tool encrypts passwords so we don't save plain text in the DB
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}