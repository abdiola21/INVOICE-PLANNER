package com.invoice.planner.service;

import java.util.List;
import java.util.UUID;

import com.invoice.planner.dto.request.PrestationRequest;
import com.invoice.planner.dto.response.ClientResponse;
import com.invoice.planner.dto.response.PrestationResponse;

public interface PrestationService {
    
    PrestationResponse create(PrestationRequest request);
    
    PrestationResponse update(UUID trackingId, PrestationRequest request);
    
    void delete(UUID trackingId);
    
    PrestationResponse findByTrackingId(UUID trackingId);
    
    List<PrestationResponse> findAll();
    
    List<PrestationResponse> search(String term);

    List<PrestationResponse> findByCreatedBy(String email);
} 