package com.invoice.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.invoice.planner.entity.Facture;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
    
    Optional<Facture> findByTrackingId(UUID trackingId);
    
    boolean existsByTrackingId(UUID trackingId);
    
    @Query("SELECT f FROM Facture f WHERE f.client.trackingId = :clientTrackingId ORDER BY f.createdAt DESC")
    List<Facture> findAllByClientTrackingId(UUID clientTrackingId);
    
    @Query("SELECT e FROM Facture e WHERE LOWER(e.numero) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.modeReglement) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.referenceDevis) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY e.id DESC")
    List<Facture> search(@Param("searchTerm") String searchTerm);

}