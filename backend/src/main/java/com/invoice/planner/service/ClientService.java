package com.invoice.planner.service;

import java.util.List;
import java.util.UUID;

import com.invoice.planner.dto.request.ClientRequest;
import com.invoice.planner.dto.response.ClientResponse;

public interface ClientService {
    
    ClientResponse create(ClientRequest request);
    
    ClientResponse update(UUID trackingId, ClientRequest request);
    
    void delete(UUID trackingId);
    
    ClientResponse findByTrackingId(UUID trackingId);
    
    List<ClientResponse> findAll();
    
    List<ClientResponse> search(String term);

    List<ClientResponse> findByCreatedBy(String email);
} 