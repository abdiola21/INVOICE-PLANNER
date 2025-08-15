package com.invoice.planner.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.UserRoleRequest;
import com.invoice.planner.dto.response.UserRoleResponse;
import com.invoice.planner.exception.ResourceNotFoundException;
import com.invoice.planner.mapper.UserRoleMapper;
import com.invoice.planner.repository.UserRoleRepository;
import com.invoice.planner.service.UserRoleService;
import com.invoice.planner.entity.UserRole;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {
    
    private final UserRoleRepository repository;
    private final UserRoleMapper mapper;
    
    public UserRoleServiceImpl(UserRoleRepository repository, UserRoleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    @Override
    public UserRoleResponse create(UserRoleRequest request) {
        UserRole entity = mapper.toEntity(request);
        entity.setTrackingId(UUID.randomUUID());
        UserRole savedEntity = repository.save(entity);
        return mapper.toResponse(savedEntity);
    }
    
    @Override
    public UserRoleResponse update(UUID trackingId, UserRoleRequest request) {
        UserRole entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("UserRole", "trackingId", trackingId));
        
        mapper.updateEntityFromRequest(request, entity);
        entity.setTrackingId(trackingId);
        UserRole updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }
    
    @Override
    public void delete(UUID trackingId) {
        UserRole entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("UserRole", "trackingId", trackingId));
        
        repository.delete(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserRoleResponse findByTrackingId(UUID trackingId) {
        UserRole entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("UserRole", "trackingId", trackingId));
        
        return mapper.toResponse(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponse> search(String term) {
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