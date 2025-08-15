package com.invoice.planner.service;

import com.invoice.planner.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service responsable du nettoyage périodique des tokens expirés
 */
@Service
public class TokenCleanupService {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenCleanupService.class);
    
    private final TokenRepository tokenRepository;
    
    public TokenCleanupService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    
    /**
     * Tâche planifiée qui s'exécute tous les jours à minuit (00:00:00)
     * pour supprimer les tokens expirés de la base de données
     */
    @Scheduled(cron = "0 0 0 * * ?") // Tous les jours à minuit
    public void cleanupExpiredTokens() {
        logger.info("Démarrage du nettoyage des tokens expirés...");
        
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = tokenRepository.deleteExpiredTokens(now);
        
        logger.info("Nettoyage des tokens terminé : {} tokens expirés supprimés", deletedCount);
    }
} 