package com.invoice.planner.mapper;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.TaxeRequest;
import com.invoice.planner.dto.response.TaxeResponse;
import com.invoice.planner.entity.Taxe;

@Component
public class TaxeMapper {
    
    /**
     * Convertit une entité en DTO de réponse
     * Ne transfère que les données nécessaires pour la présentation au frontend
     */
    public TaxeResponse toResponse(Taxe entity) {
        if (entity == null) {
            return null;
        }
        
        TaxeResponse response = new TaxeResponse();
        // On utilise le trackingId comme identifiant public
        response.setTrackingId(entity.getTrackingId());
        
        // Mapping des attributs standard
        response.setNom(entity.getNom());
        response.setTaux(entity.getTaux());
        response.setEstobligatoire(entity.isEstObligatoire());
        response.setDevis(entity.getDevis());
        response.setFacture(entity.getFacture());
        
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
    public Taxe toEntity(TaxeRequest request) {
        if (request == null) {
            return null;
        }
        
        Taxe entity = new Taxe();
        
        // Mapping des attributs standard
        entity.setNom(request.getNom());
        entity.setTaux(request.getTaux());
        entity.setEstObligatoire(request.getEstobligatoire());
        entity.setDevis(request.getDevis());
        entity.setFacture(request.getFacture());
        
        return entity;
    }
    
    /**
     * Met à jour une entité à partir d'un DTO de requête
     * Préserve les IDs et trackingIds existants
     */
    public void updateEntityFromRequest(TaxeRequest request, Taxe entity) {
        if (request == null || entity == null) {
            return;
        }
        
        // Mise à jour des attributs non-ID
        entity.setNom(request.getNom());
        entity.setTaux(request.getTaux());
        entity.setEstObligatoire(request.getEstobligatoire());
        entity.setDevis(request.getDevis());
        entity.setFacture(request.getFacture());
    }
    
    /**
     * Convertit une liste d'entités en liste de DTOs de réponse
     */
    public List<TaxeResponse> toResponseList(List<Taxe> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 