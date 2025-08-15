package com.invoice.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.invoice.planner.entity.Historique;

@Repository
public interface HistoriqueRepository extends JpaRepository<Historique, Long> {
    
    Optional<Historique> findByTrackingId(UUID trackingId);
    
    boolean existsByTrackingId(UUID trackingId);
    
    
    List<Historique> findAllByUtilisateurTrackingId(UUID utilisateurTrackingId);
    
    @Query("SELECT e FROM Historique e WHERE LOWER(e.action) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.details) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY e.id DESC")
    List<Historique> search(@Param("searchTerm") String searchTerm);

}