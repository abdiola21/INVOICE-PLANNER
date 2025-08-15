package com.invoice.planner.service.impl;

import com.invoice.planner.entity.Prestation;
import com.invoice.planner.repository.PrestationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.DevisRequest;
import com.invoice.planner.dto.response.DevisResponse;
import com.invoice.planner.exception.ResourceNotFoundException;
import com.invoice.planner.mapper.DevisMapper;
import com.invoice.planner.repository.DevisRepository;
import com.invoice.planner.service.DevisService;
import com.invoice.planner.entity.Devis;

@Service
@Transactional
public class DevisServiceImpl implements DevisService {
    
    private final DevisRepository repository;
    private final DevisMapper mapper;
    private final PrestationRepository prestationRepository;
    
    public DevisServiceImpl(DevisRepository repository, DevisMapper mapper, PrestationRepository prestationRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.prestationRepository = prestationRepository;
    }
    
    @Override
    @Transactional
    public DevisResponse create(DevisRequest request) {

        Devis entity = mapper.toEntity(request);
        entity.setTrackingId(UUID.randomUUID());
        
        // Calculer automatiquement le prix total et le prix TTC
        entity.calculerPrixTotal();
        entity.calculerPrixTTC();
        
        Devis savedEntity = repository.save(entity);
        for(int i=0;i<request.getPrestations().size();i++){
            System.out.println(request.getPrestations().get(i).getTrackingId());
            Prestation prestation = prestationRepository.findByTrackingId(request.getPrestations().get(i).getTrackingId()).orElseThrow(()->new RuntimeException("Prestation non trouver"));
            prestation.setDevis(savedEntity);

        }



        return mapper.toResponse(savedEntity);
    }
    
    @Override
    public DevisResponse update(UUID trackingId, DevisRequest request) {
        Devis entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Devis", "trackingId", trackingId));
        
        mapper.updateEntityFromRequest(request, entity);
        entity.setTrackingId(trackingId);
        
        // Recalculer automatiquement le prix total et le prix TTC
        entity.calculerPrixTotal();
        entity.calculerPrixTTC();

        System.out.println(entity);
        
        Devis updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }
    
    @Override
    public void delete(UUID trackingId) {
        Devis entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Devis", "trackingId", trackingId));
        
        repository.delete(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DevisResponse findByTrackingId(UUID trackingId) {
        Devis entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Devis", "trackingId", trackingId));
        
        return mapper.toResponse(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DevisResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DevisResponse> search(String term) {
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
    public List<DevisResponse> findByCreatedBy(String email) {
        return repository.findByCreatedBy(email).stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
} 