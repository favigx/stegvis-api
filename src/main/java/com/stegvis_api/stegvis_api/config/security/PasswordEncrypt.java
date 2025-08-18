package com.stegvis_api.stegvis_api.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncrypt {

    @Value("${stegvis.password.pepper}")
    private String PEPPER;

    @Bean
    public PasswordEncoder passwordEncoder() {
        int saltLength = 16;
        int hashLength = 32;
        int parallelism = 2;
        int memory = 65536;
        int iterations = 3;

        return new Argon2PasswordEncoder(
                saltLength, hashLength, parallelism, memory, iterations) {

            @Override
            public String encode(CharSequence rawPassword) {
                return super.encode(rawPassword + PEPPER);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return super.matches(rawPassword + PEPPER, encodedPassword);
            }
        };
    }
}