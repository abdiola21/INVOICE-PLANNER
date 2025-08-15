package com.invoice.planner.mapper;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.UserRoleRequest;
import com.invoice.planner.dto.response.UserRoleResponse;
import com.invoice.planner.entity.UserRole;

@Component
public class UserRoleMapper {
    
    /**
     * Convertit une entité en DTO de réponse
     * Ne transfère que les données nécessaires pour la présentation au frontend
     */
    public UserRoleResponse toResponse(UserRole entity) {
        if (entity == null) {
            return null;
        }
        
        UserRoleResponse response = new UserRoleResponse();
        // On utilise le trackingId comme identifiant public
        response.setTrackingId(entity.getTrackingId());
        
        // Mapping des attributs standard
        try {
            response.setRolename(entity.getRoleName());
        } catch (Exception e) {
            // Ignore si le getter n'existe pas
        }
        try {
            response.setDescription(entity.getDescription());
        } catch (Exception e) {
            // Ignore si le getter n'existe pas
        }
        try {
            response.setIsactive(entity.isActive());
        } catch (Exception e) {
            // Ignore si le getter n'existe pas
        }
        
        // Mapping des relations avec seulement les informations nécessaires
        
        // Métadonnées si disponibles
        try {
            response.setCreatedAt(entity.getCreatedAt());
            response.setUpdatedAt(entity.getUpdatedAt());
        } catch (Exception e) {
            // Ignorer si non disponible
        }
        
        return response;
    }
    
    /**
     * Convertit un DTO de requête en entité
     * Pour la création, les IDs seront générés par le système
     */
    public UserRole toEntity(UserRoleRequest request) {
        if (request == null) {
            return null;
        }
        
        UserRole entity = new UserRole();
        entity.setTrackingId(UUID.randomUUID());
        // Mapping des attributs standard
        try {
            entity.setRoleName(request.getRolename());
        } catch (Exception e) {
            // Ignore si le setter n'existe pas
        }
        try {
            entity.setDescription(request.getDescription());
        } catch (Exception e) {
            // Ignore si le setter n'existe pas
        }
        try {
            entity.setActive(request.getIsactive());
        } catch (Exception e) {
            // Ignore si le setter n'existe pas
        }
        
        return entity;
    }
    
    /**
     * Met à jour une entité à partir d'un DTO de requête
     * Préserve les IDs et trackingIds existants
     */
    public void updateEntityFromRequest(UserRoleRequest request, UserRole entity) {
        if (request == null || entity == null) {
            return;
        }
        
        // Mise à jour des attributs non-ID
        try {
            entity.setRoleName(request.getRolename());
        } catch (Exception e) {
            // Ignore si le setter n'existe pas
        }
        try {
            entity.setDescription(request.getDescription());
        } catch (Exception e) {
            // Ignore si le setter n'existe pas
        }
        try {
            entity.setActive(request.getIsactive());
        } catch (Exception e) {
            // Ignore si le setter n'existe pas
        }
    }
    
    /**
     * Convertit une liste d'entités en liste de DTOs de réponse
     */
    public List<UserRoleResponse> toResponseList(List<UserRole> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 