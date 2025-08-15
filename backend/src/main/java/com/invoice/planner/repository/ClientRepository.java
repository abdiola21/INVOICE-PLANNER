package com.invoice.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.invoice.planner.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByTrackingId(UUID trackingId);
    
    boolean existsByTrackingId(UUID trackingId);
    
    // filtrer par created_by --> utilisateur connecté
    @Query("SELECT e FROM Client e WHERE e.createdBy = :email ORDER BY e.id DESC")
    List<Client> findByCreatedBy(@Param("email") String email);
    
    @Query("SELECT e FROM Client e WHERE LOWER(e.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.adresse) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.telephone) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY e.id DESC")
    List<Client> search(@Param("searchTerm") String searchTerm);

}