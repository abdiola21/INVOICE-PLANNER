package com.invoice.planner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.invoice.planner.repository.TokenRepository;
import com.invoice.planner.service.TokenCleanupService;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    // Configuration pour activer les tâches planifiées
    @Bean
    public TokenCleanupService tokenCleanupService(TokenRepository tokenRepository) {
        return new TokenCleanupService(tokenRepository);
    }
}