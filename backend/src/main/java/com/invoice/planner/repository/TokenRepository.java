package com.invoice.planner.repository;

import com.invoice.planner.entity.Token;
import com.invoice.planner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    
    /**
     * Trouve tous les tokens valides (non expirés et non révoqués) pour un utilisateur
     */
    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND t.expired = false AND t.revoked = false")
    List<Token> findAllValidTokensByUser(Long userId);
    
    /**
     * Trouve un token par sa valeur
     */
    Optional<Token> findByToken(String token);
    
    /**
     * Invalide tous les tokens d'un utilisateur
     */
    @Modifying
    @Transactional
    @Query("UPDATE Token t SET t.revoked = true, t.expired = true WHERE t.user = :user AND t.revoked = false")
    void revokeAllUserTokens(@Param("user") User user);
    
    /**
     * Supprime tous les tokens expirés à la date et heure actuelle
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expired = true OR t.expiresAt < :now")
    int deleteExpiredTokens(@Param("now") LocalDateTime now);
}
