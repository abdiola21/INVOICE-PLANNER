package com.invoice.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.invoice.planner.entity.Devis;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Long> {
    
    Optional<Devis> findByTrackingId(UUID trackingId);
    
    boolean existsByTrackingId(UUID trackingId);
    
    
    List<Devis> findAllByClientTrackingId(UUID clientTrackingId);
    
    @Query("SELECT e FROM Devis e WHERE LOWER(e.reference) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.nomProjet) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.notes) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY e.id DESC")
    List<Devis> search(@Param("searchTerm") String searchTerm);

    @Query("SELECT e FROM Devis e WHERE e.createdBy = :email ORDER BY e.id DESC")
    List<Devis> findByCreatedBy(@Param("email") String email);
}