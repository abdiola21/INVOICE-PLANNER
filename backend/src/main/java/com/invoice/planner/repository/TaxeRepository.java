package com.invoice.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.invoice.planner.entity.Taxe;

@Repository
public interface TaxeRepository extends JpaRepository<Taxe, Long> {
    
    Optional<Taxe> findByTrackingId(UUID trackingId);
    
    boolean existsByTrackingId(UUID trackingId);
    
    
    List<Taxe> findAllByDevisTrackingId(UUID devisTrackingId);
    List<Taxe> findAllByFactureTrackingId(UUID factureTrackingId);
    
    @Query("SELECT e FROM Taxe e WHERE LOWER(e.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY e.id DESC")
    List<Taxe> search(@Param("searchTerm") String searchTerm);

}