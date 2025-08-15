package com.invoice.planner.repository;

import com.invoice.planner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByTrackingId(UUID trackingId);
    
    boolean existsByEmail(String email);
    
    /**
     * Recherche des utilisateurs par prénom, nom, email, ou numéro de téléphone
     * @param keyword le terme de recherche
     * @return la liste des utilisateurs correspondant au critère de recherche
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " + 
           "LOWER(u.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "u.numeroDeTelephone LIKE CONCAT('%', :keyword, '%')")
    List<User> search(@Param("keyword") String keyword);
}
