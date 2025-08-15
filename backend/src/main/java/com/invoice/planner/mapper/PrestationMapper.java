package com.invoice.planner.mapper;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.PrestationRequest;
import com.invoice.planner.dto.response.PrestationResponse;
import com.invoice.planner.entity.Prestation;

@Component
public class PrestationMapper {
    
    /**
     * Convertit une entité en DTO de réponse
     * Ne transfère que les données nécessaires pour la présentation au frontend
     */
    public PrestationResponse toResponse(Prestation entity) {
        if (entity == null) {
            return null;
        }
        
        PrestationResponse response = new PrestationResponse();
        // On utilise le trackingId comme identifiant public
        response.setTrackingId(entity.getTrackingId());
        
        // Mapping des attributs standard
        response.setDesignation(entity.getDesignation());
        response.setDescription(entity.getDescription());
        response.setPrixUnitaire(entity.getPrixUnitaire());
        response.setDuree(entity.getDuree());
        response.setPrixTotal(entity.getPrixTotal());
        
        // Métadonnées si disponibles
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        
        return response;
    }
    
    /**
     * Convertit un DTO de requête en entité
     * Pour la création, les IDs seront générés par le système
     */
    public Prestation toEntity(PrestationRequest request) {
        if (request == null) {
            return null;
        }
        
        Prestation entity = new Prestation();
        
        // Mapping des attributs standard
        entity.setDesignation(request.getDesignation());
        entity.setDescription(request.getDescription());
        entity.setPrixUnitaire(request.getPrixUnitaire());
        entity.setDuree(request.getDuree());
        if(request.getPrixTotal()==null || request.getPrixTotal()==0.0){
            entity.setPrixTotal(entity.calculerMontant());
        }else{
            entity.setPrixTotal(request.getPrixTotal());
        }

        
        return entity;
    }
    
    /**
     * Met à jour une entité à partir d'un DTO de requête
     * Préserve les IDs et trackingIds existants
     */
    public void updateEntityFromRequest(PrestationRequest request, Prestation entity) {
        if (request == null || entity == null) {
            return;
        }
        
        // Mise à jour des attributs non-ID
        entity.setDesignation(request.getDesignation());
        entity.setDescription(request.getDescription());
        entity.setPrixUnitaire(request.getPrixUnitaire());
        entity.setDuree(request.getDuree());
        if(request.getPrixTotal()==null || request.getPrixTotal()==0.0){
            entity.setPrixTotal(entity.calculerMontant());
        }else{
            entity.setPrixTotal(request.getPrixTotal());
        }
    }
    
    /**
     * Convertit une liste d'entités en liste de DTOs de réponse
     */
    public List<PrestationResponse> toResponseList(List<Prestation> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 