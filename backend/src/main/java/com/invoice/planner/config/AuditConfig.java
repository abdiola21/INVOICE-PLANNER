package com.invoice.planner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {
    

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> java.util.Optional.of(getCurrentUser());
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "SYSTEM";
        }
        return authentication.getName();
    }
    // @Bean
    // public AuditorAware<String> auditorProvider() {
    //     // Pour l'instant, on retourne un utilisateur par défaut
    //     return () -> java.util.Optional.of("SYSTEM");
    //     // Plus tard, vous pourrez le remplacer par l'utilisateur connecté 
    //     //return () -> java.util.Optional.of(getCurrentUser());
    // }

    // private String getCurrentUser() {
    //     // Implémentez la logique pour obtenir l'utilisateur connecté
    //     return "USER_CONNECTE"; // Remplacez par la logique réelle
    // }
} 