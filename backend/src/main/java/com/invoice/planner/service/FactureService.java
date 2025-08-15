package com.invoice.planner.service;

import java.util.List;
import java.util.UUID;

import com.invoice.planner.dto.request.FactureRequest;
import com.invoice.planner.dto.response.FactureResponse;

public interface FactureService {
    
    FactureResponse create(FactureRequest request);

    FactureResponse createByDevisId(UUID trackingId);
    
    FactureResponse update(UUID trackingId, FactureRequest request);
    
    void delete(UUID trackingId);
    
    FactureResponse findByTrackingId(UUID trackingId);
    
    List<FactureResponse> findAll();
    
    List<FactureResponse> search(String term);
} 