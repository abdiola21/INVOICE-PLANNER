package com.invoice.planner.repository;

import com.invoice.planner.entity.CompanyProfile;
import com.invoice.planner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {
    
    Optional<CompanyProfile> findByUser(User user);
    
    Optional<CompanyProfile> findByTrackingId(UUID trackingId);
    
    boolean existsByUser(User user);
} 