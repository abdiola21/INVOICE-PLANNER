package com.invoice.planner.mapper;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.HistoriqueRequest;
import com.invoice.planner.dto.response.HistoriqueResponse;
import com.invoice.planner.entity.Historique;

@Component
public class HistoriqueMapper {
    
    /**
     * Convertit une entité en DTO de réponse
     * Ne transfère que les données nécessaires pour la présentation au frontend
     */
    public HistoriqueResponse toResponse(Historique entity) {
        if (entity == null) {
            return null;
        }
        
        HistoriqueResponse response = new HistoriqueResponse();
        // On utilise le trackingId comme identifiant public
        response.setTrackingId(entity.getTrackingId());
        
        // Mapping des attributs standard
        response.setAction(entity.getAction());
        response.setDate(entity.getDate());
        response.setUtilisateur(entity.getUtilisateur());
        response.setDetails(entity.getDetails());
        
        // Mapping des relations avec seulement les informations nécessaires
        if (entity.getUtilisateur() != null) {
            // On récupère le trackingId pour référence
            response.setUtilisateurTrackingId(entity.getUtilisateur().getTrackingId());
            
            // Mapping des champs personnalisés sélectionnés
            try {
                // On essaie de récupérer la valeur du champ avec le type approprié
                java.lang.reflect.Method method = entity.getUtilisateur().getClass().getMethod("getName");
                Object value = method.invoke(entity.getUtilisateur());
                
                // On convertit la valeur en String car c'est le type utilisé dans le DTO de réponse
                response.setUtilisateurName(value != null ? value.toString() : null);
            } catch (Exception e) {
                // Fallback sur vide si la méthode n'existe pas
                response.setUtilisateurName("");
                // Log pour le debugging
                System.err.println("Impossible de récupérer l'attribut '" + "name" + "' de l'entité 'utilisateur'");
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
    public Historique toEntity(HistoriqueRequest request) {
        if (request == null) {
            return null;
        }
        
        Historique entity = new Historique();
        
        // Mapping des attributs standard
        entity.setAction(request.getAction());
        entity.setDate(request.getDate());
        entity.setUtilisateur(request.getUtilisateur());
        entity.setDetails(request.getDetails());
        
        return entity;
    }
    
    /**
     * Met à jour une entité à partir d'un DTO de requête
     * Préserve les IDs et trackingIds existants
     */
    public void updateEntityFromRequest(HistoriqueRequest request, Historique entity) {
        if (request == null || entity == null) {
            return;
        }
        
        // Mise à jour des attributs non-ID
        entity.setAction(request.getAction());
        entity.setDate(request.getDate());
        entity.setUtilisateur(request.getUtilisateur());
        entity.setDetails(request.getDetails());
    }
    
    /**
     * Convertit une liste d'entités en liste de DTOs de réponse
     */
    public List<HistoriqueResponse> toResponseList(List<Historique> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 