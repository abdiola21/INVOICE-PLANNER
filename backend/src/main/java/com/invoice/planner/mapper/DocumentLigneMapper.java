package com.invoice.planner.mapper;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.DocumentLigneRequest;
import com.invoice.planner.dto.response.DocumentLigneResponse;
import com.invoice.planner.entity.DocumentLigne;

@Component
public class DocumentLigneMapper {
    
    /**
     * Convertit une entité en DTO de réponse
     * Ne transfère que les données nécessaires pour la présentation au frontend
     */
    public DocumentLigneResponse toResponse(DocumentLigne entity) {
        if (entity == null) {
            return null;
        }
        
        DocumentLigneResponse response = new DocumentLigneResponse();
        // On utilise le trackingId comme identifiant public
        response.setTrackingId(entity.getTrackingId());
        
        // Mapping des attributs standard
        response.setDesignation(entity.getDesignation());
        response.setDevis(entity.getDevis());
        response.setFacture(entity.getFacture());
        response.setQuantite(entity.getQuantite());
        response.setPrixunitaire(entity.getPrixUnitaire());
        
        // Mapping des relations avec seulement les informations nécessaires
        if (entity.getDevis() != null) {
            // On récupère le trackingId pour référence
            response.setDevisTrackingId(entity.getDevis().getTrackingId());
            
            // Mapping des champs personnalisés sélectionnés
            try {
                java.lang.reflect.Method method = entity.getDevis().getClass().getMethod("getName");
                Object value = method.invoke(entity.getDevis());
                response.setDevisName(value != null ? value.toString() : null);
            } catch (Exception e) {
                response.setDevisName("");
                System.err.println("Impossible de récupérer l'attribut '" + "name" + "' de l'entité 'devis'");
            }
        }
        if (entity.getFacture() != null) {
            // On récupère le trackingId pour référence
            response.setFactureTrackingId(entity.getFacture().getTrackingId());
            
            // Mapping des champs personnalisés sélectionnés
            try {
                java.lang.reflect.Method method = entity.getFacture().getClass().getMethod("getName");
                Object value = method.invoke(entity.getFacture());
                response.setFactureName(value != null ? value.toString() : null);
            } catch (Exception e) {
                response.setFactureName("");
                System.err.println("Impossible de récupérer l'attribut '" + "name" + "' de l'entité 'facture'");
            }
        }
        
        // Métadonnées si disponibles
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        
        return response;
    }
    
    /**
     * Convertit un DTO de requête en entité
     * Pour la création, les IDs seront générés par le système
     */
    public DocumentLigne toEntity(DocumentLigneRequest request) {
        if (request == null) {
            return null;
        }
        
        DocumentLigne entity = new DocumentLigne();
        
        // Mapping des attributs standard
        entity.setDesignation(request.getDesignation());
        entity.setDevis(request.getDevis());
        entity.setFacture(request.getFacture());
        entity.setQuantite(request.getQuantite());
        entity.setPrixUnitaire(request.getPrixunitaire());
        
        return entity;
    }
    
    /**
     * Met à jour une entité à partir d'un DTO de requête
     * Préserve les IDs et trackingIds existants
     */
    public void updateEntityFromRequest(DocumentLigneRequest request, DocumentLigne entity) {
        if (request == null || entity == null) {
            return;
        }
        
        // Mise à jour des attributs non-ID
        entity.setDesignation(request.getDesignation());
        entity.setDevis(request.getDevis());
        entity.setFacture(request.getFacture());
        entity.setQuantite(request.getQuantite());
        entity.setPrixUnitaire(request.getPrixunitaire());
    }
    
    /**
     * Convertit une liste d'entités en liste de DTOs de réponse
     */
    public List<DocumentLigneResponse> toResponseList(List<DocumentLigne> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 