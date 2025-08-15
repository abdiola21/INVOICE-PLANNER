package com.invoice.planner.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.HistoriqueRequest;
import com.invoice.planner.dto.response.HistoriqueResponse;
import com.invoice.planner.exception.ResourceNotFoundException;
import com.invoice.planner.mapper.HistoriqueMapper;
import com.invoice.planner.repository.HistoriqueRepository;
import com.invoice.planner.service.HistoriqueService;
import com.invoice.planner.entity.Historique;

@Service
@Transactional
public class HistoriqueServiceImpl implements HistoriqueService {
    
    private final HistoriqueRepository repository;
    private final HistoriqueMapper mapper;
    
    public HistoriqueServiceImpl(HistoriqueRepository repository, HistoriqueMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public HistoriqueResponse create(HistoriqueRequest request) {
        Historique entity = mapper.toEntity(request);
        entity.setTrackingId(UUID.randomUUID());
        Historique savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }
    
    @Override
    public HistoriqueResponse update(UUID trackingId, HistoriqueRequest request) {
        Historique entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Historique", "trackingId", trackingId));
        
        mapper.updateEntityFromRequest(request, entity);
        entity.setTrackingId(trackingId);
        Historique updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }
    
    @Override
    public void delete(UUID trackingId) {
        Historique entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Historique", "trackingId", trackingId));
        
        repository.delete(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public HistoriqueResponse findByTrackingId(UUID trackingId) {
        Historique entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Historique", "trackingId", trackingId));
        
        return mapper.toResponse(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HistoriqueResponse> search(String term) {
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