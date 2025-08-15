package com.invoice.planner.service;

import java.util.List;
import java.util.UUID;

import com.invoice.planner.dto.request.DocumentLigneRequest;
import com.invoice.planner.dto.response.DocumentLigneResponse;

public interface DocumentLigneService {
    
    DocumentLigneResponse create(DocumentLigneRequest request);
    
    DocumentLigneResponse update(UUID trackingId, DocumentLigneRequest request);
    
    void delete(UUID trackingId);
    
    DocumentLigneResponse findByTrackingId(UUID trackingId);
    
    List<DocumentLigneResponse> findAll();
    
    List<DocumentLigneResponse> search(String term);
} 