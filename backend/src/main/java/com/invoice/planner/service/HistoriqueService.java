package com.invoice.planner.service;

import java.util.List;
import java.util.UUID;

import com.invoice.planner.dto.request.HistoriqueRequest;
import com.invoice.planner.dto.response.HistoriqueResponse;

public interface HistoriqueService {
    
    HistoriqueResponse create(HistoriqueRequest request);
    
    HistoriqueResponse update(UUID trackingId, HistoriqueRequest request);
    
    void delete(UUID trackingId);
    
    HistoriqueResponse findByTrackingId(UUID trackingId);
    
    List<HistoriqueResponse> findAll();
    
    List<HistoriqueResponse> search(String term);
} 