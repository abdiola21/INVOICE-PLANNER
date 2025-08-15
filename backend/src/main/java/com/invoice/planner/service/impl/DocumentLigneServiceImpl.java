package com.invoice.planner.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.DocumentLigneRequest;
import com.invoice.planner.dto.response.DocumentLigneResponse;
import com.invoice.planner.exception.ResourceNotFoundException;
import com.invoice.planner.mapper.DocumentLigneMapper;
import com.invoice.planner.repository.DocumentLigneRepository;
import com.invoice.planner.service.DocumentLigneService;
import com.invoice.planner.entity.DocumentLigne;

@Service
@Transactional
public class DocumentLigneServiceImpl implements DocumentLigneService {
    
    private final DocumentLigneRepository repository;
    private final DocumentLigneMapper mapper;
    
    public DocumentLigneServiceImpl(DocumentLigneRepository repository, DocumentLigneMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public DocumentLigneResponse create(DocumentLigneRequest request) {
        DocumentLigne entity = mapper.toEntity(request);
        entity.setTrackingId(UUID.randomUUID());
        DocumentLigne savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }
    
    @Override
    public DocumentLigneResponse update(UUID trackingId, DocumentLigneRequest request) {
        DocumentLigne entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("DocumentLigne", "trackingId", trackingId));
        
        mapper.updateEntityFromRequest(request, entity);
        entity.setTrackingId(trackingId);
        DocumentLigne updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }
    
    @Override
    public void delete(UUID trackingId) {
        DocumentLigne entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("DocumentLigne", "trackingId", trackingId));
        
        repository.delete(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DocumentLigneResponse findByTrackingId(UUID trackingId) {
        DocumentLigne entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("DocumentLigne", "trackingId", trackingId));
        
        return mapper.toResponse(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentLigneResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentLigneResponse> search(String term) {
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
} 