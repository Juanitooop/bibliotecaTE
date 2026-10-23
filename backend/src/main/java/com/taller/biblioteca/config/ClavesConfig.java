package com.taller.biblioteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ClavesConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Conservamos las claves como hash, aunque este avance no tenga login.
        return new BCryptPasswordEncoder();
    }
}
