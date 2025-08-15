package com.invoice.planner.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.PrestationRequest;
import com.invoice.planner.dto.response.PrestationResponse;
import com.invoice.planner.exception.ResourceNotFoundException;
import com.invoice.planner.mapper.PrestationMapper;
import com.invoice.planner.repository.PrestationRepository;
import com.invoice.planner.service.PrestationService;
import com.invoice.planner.entity.Prestation;

@Service
@Transactional
public class PrestationServiceImpl implements PrestationService {
    
    private final PrestationRepository repository;
    private final PrestationMapper mapper;
    
    public PrestationServiceImpl(PrestationRepository repository, PrestationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public PrestationResponse create(PrestationRequest request) {
        Prestation entity = mapper.toEntity(request);
        entity.setTrackingId(UUID.randomUUID());
        Prestation savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }
    
    @Override
    public PrestationResponse update(UUID trackingId, PrestationRequest request) {
        Prestation entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Prestation", "trackingId", trackingId));
        
        mapper.updateEntityFromRequest(request, entity);
        entity.setTrackingId(trackingId);
        Prestation updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }
    
    @Override
    public void delete(UUID trackingId) {
        Prestation entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Prestation", "trackingId", trackingId));
        
        repository.delete(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PrestationResponse findByTrackingId(UUID trackingId) {
        Prestation entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Prestation", "trackingId", trackingId));
        
        return mapper.toResponse(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PrestationResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PrestationResponse> search(String term) {
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
    public List<PrestationResponse> findByCreatedBy(String email) {
        return repository.findByCreatedBy(email).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }


} 