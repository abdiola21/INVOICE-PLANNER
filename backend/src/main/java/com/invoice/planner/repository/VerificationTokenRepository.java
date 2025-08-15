package com.invoice.planner.repository;

import com.invoice.planner.entity.User;
import com.invoice.planner.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    
    Optional<VerificationToken> findByToken(String token);
    
    Optional<VerificationToken> findByUser(User user);
    
    @Query("SELECT t FROM VerificationToken t WHERE t.user = :user AND t.used = false AND t.expiryDate > :now")
    Optional<VerificationToken> findValidTokenByUser(@Param("user") User user, @Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM VerificationToken t WHERE t.expiryDate < :now")
    List<VerificationToken> findAllExpiredTokens(@Param("now") LocalDateTime now);
} 