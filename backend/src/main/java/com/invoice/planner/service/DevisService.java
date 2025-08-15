package com.invoice.planner.service;

import java.util.List;
import java.util.UUID;

import com.invoice.planner.dto.request.DevisRequest;
import com.invoice.planner.dto.response.DevisResponse;

public interface DevisService {
    
    DevisResponse create(DevisRequest request);
    
    DevisResponse update(UUID trackingId, DevisRequest request);
    
    void delete(UUID trackingId);
    
    DevisResponse findByTrackingId(UUID trackingId);
    
    List<DevisResponse> findAll();
    
    List<DevisResponse> search(String term);

    List<DevisResponse> findByCreatedBy(String email);
} 