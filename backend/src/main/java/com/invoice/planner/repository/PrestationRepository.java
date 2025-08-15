package com.invoice.planner.repository;

import com.invoice.planner.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.invoice.planner.entity.Prestation;

@Repository
public interface PrestationRepository extends JpaRepository<Prestation, Long> {
    
    Optional<Prestation> findByTrackingId(UUID trackingId);
    
    boolean existsByTrackingId(UUID trackingId);
    
    
    
    @Query("SELECT e FROM Prestation e WHERE LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.designation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY e.createdAt DESC")
    List<Prestation> search(@Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Prestation p WHERE p.createdBy = :email ORDER BY p.id DESC")
    List<Prestation> findByCreatedBy(@Param("email") String email);

}