package com.invoice.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.invoice.planner.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    Optional<UserRole> findByTrackingId(UUID trackingId);
    
    boolean existsByTrackingId(UUID trackingId);
    
    Optional<UserRole> findByRoleName(String roleName);
    
    @Query("SELECT e FROM UserRole e WHERE LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(e.roleName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY e.id DESC")
    List<UserRole> search(@Param("searchTerm") String searchTerm);

}