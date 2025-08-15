package com.invoice.planner.service;

import java.util.List;
import java.util.UUID;

import com.invoice.planner.dto.request.TaxeRequest;
import com.invoice.planner.dto.response.TaxeResponse;

public interface TaxeService {
    
    TaxeResponse create(TaxeRequest request);
    
    TaxeResponse update(UUID trackingId, TaxeRequest request);
    
    void delete(UUID trackingId);
    
    TaxeResponse findByTrackingId(UUID trackingId);
    
    List<TaxeResponse> findAll();
    
    List<TaxeResponse> search(String term);
} 