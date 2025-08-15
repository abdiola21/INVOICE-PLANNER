package com.invoice.planner.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.ClientRequest;
import com.invoice.planner.dto.response.ClientResponse;
import com.invoice.planner.exception.ResourceNotFoundException;
import com.invoice.planner.mapper.ClientMapper;
import com.invoice.planner.repository.ClientRepository;
import com.invoice.planner.service.ClientService;
import com.invoice.planner.entity.Client;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    
    private final ClientRepository repository;
    private final ClientMapper mapper;
    
    public ClientServiceImpl(ClientRepository repository, ClientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public ClientResponse create(ClientRequest request) {
        Client entity = mapper.toEntity(request);
        entity.setTrackingId(UUID.randomUUID());
        Client savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }
    
    @Override
    public ClientResponse update(UUID trackingId, ClientRequest request) {
        Client entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Client", "trackingId", trackingId));
        
        mapper.updateEntityFromRequest(request, entity);
        entity.setTrackingId(trackingId);
        Client updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }
    
    @Override
    public void delete(UUID trackingId) {
        Client entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Client", "trackingId", trackingId));
        
        repository.delete(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClientResponse findByTrackingId(UUID trackingId) {
        Client entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Client", "trackingId", trackingId));
        
        return mapper.toResponse(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClientResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ClientResponse> search(String term) {
        // Implémentation simple : retourne tous les résultats si le terme est vide
        if (term == null || term.isEmpty()) {
            return findAll();
        }
        
        // Recherche par le repository si disponible, sinon filtre basique
        try {
            return repository.search(term).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            // Fallback à une méthode de recherche basique
            return findAll().stream()
                .filter(response -> response.toString().toLowerCase().contains(term.toLowerCase()))
                .collect(Collectors.toList());
        }
    }

    
    @Override
    public List<ClientResponse> findByCreatedBy(String email) {
        return repository.findByCreatedBy(email).stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
} 