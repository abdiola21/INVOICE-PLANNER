package com.invoice.planner.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.TaxeRequest;
import com.invoice.planner.dto.response.TaxeResponse;
import com.invoice.planner.exception.ResourceNotFoundException;
import com.invoice.planner.mapper.TaxeMapper;
import com.invoice.planner.repository.TaxeRepository;
import com.invoice.planner.service.TaxeService;
import com.invoice.planner.entity.Taxe;

@Service
@Transactional
public class TaxeServiceImpl implements TaxeService {
    
    private final TaxeRepository repository;
    private final TaxeMapper mapper;
    
    public TaxeServiceImpl(TaxeRepository repository, TaxeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public TaxeResponse create(TaxeRequest request) {
        Taxe entity = mapper.toEntity(request);
        entity.setTrackingId(UUID.randomUUID());
        Taxe savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }
    
    @Override
    public TaxeResponse update(UUID trackingId, TaxeRequest request) {
        Taxe entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Taxe", "trackingId", trackingId));
        
        mapper.updateEntityFromRequest(request, entity);
        entity.setTrackingId(trackingId);
        Taxe updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }
    
    @Override
    public void delete(UUID trackingId) {
        Taxe entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Taxe", "trackingId", trackingId));
        
        repository.delete(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TaxeResponse findByTrackingId(UUID trackingId) {
        Taxe entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Taxe", "trackingId", trackingId));
        
        return mapper.toResponse(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TaxeResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TaxeResponse> search(String term) {
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