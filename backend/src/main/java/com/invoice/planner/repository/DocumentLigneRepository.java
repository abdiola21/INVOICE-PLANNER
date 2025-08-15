package com.invoice.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.invoice.planner.entity.DocumentLigne;

@Repository
public interface DocumentLigneRepository extends JpaRepository<DocumentLigne, Long> {
    
    Optional<DocumentLigne> findByTrackingId(UUID trackingId);
    
    boolean existsByTrackingId(UUID trackingId);
    
    
    List<DocumentLigne> findAllByDevisTrackingId(UUID devisTrackingId);
    List<DocumentLigne> findAllByFactureTrackingId(UUID factureTrackingId);
    
    @Query("SELECT e FROM DocumentLigne e WHERE LOWER(e.designation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY e.createdAt DESC")
    List<DocumentLigne> search(@Param("searchTerm") String searchTerm);

}