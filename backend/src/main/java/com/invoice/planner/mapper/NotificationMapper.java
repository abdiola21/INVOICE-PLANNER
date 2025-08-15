package com.invoice.planner.mapper;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.NotificationRequest;
import com.invoice.planner.dto.response.NotificationResponse;
import com.invoice.planner.entity.Notification;

@Component
public class NotificationMapper {
    
    /**
     * Convertit une entité en DTO de réponse
     * Ne transfère que les données nécessaires pour la présentation au frontend
     */
    public NotificationResponse toResponse(Notification entity) {
        if (entity == null) {
            return null;
        }
        
        NotificationResponse response = new NotificationResponse();
        // On utilise le trackingId comme identifiant public
        response.setTrackingId(entity.getTrackingId());
        
        // Mapping des attributs standard
        response.setMessage(entity.getMessage());
        response.setDate(entity.getDate());
        response.setEstlu(entity.isEstLu());
        response.setType(entity.getType());
        response.setDestinataire(entity.getDestinataire());
        
        // Mapping des relations avec seulement les informations nécessaires
        if (entity.getDestinataire() != null) {
            // On récupère le trackingId pour référence
            response.setDestinataireTrackingId(entity.getDestinataire().getTrackingId());
            
            // Mapping des champs personnalisés sélectionnés
            try {
                // On essaie de récupérer la valeur du champ avec le type approprié
                java.lang.reflect.Method method = entity.getDestinataire().getClass().getMethod("getName");
                Object value = method.invoke(entity.getDestinataire());
                
                // On convertit la valeur en String car c'est le type utilisé dans le DTO de réponse
                response.setDestinataireName(value != null ? value.toString() : null);
            } catch (Exception e) {
                // Fallback sur vide si la méthode n'existe pas
                response.setDestinataireName("");
                // Log pour le debugging
                System.err.println("Impossible de récupérer l'attribut '" + "name" + "' de l'entité 'destinataire'");
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
    public Notification toEntity(NotificationRequest request) {
        if (request == null) {
            return null;
        }
        
        Notification entity = new Notification();
        
        // Mapping des attributs standard
        entity.setMessage(request.getMessage());
        entity.setDate(request.getDate());
        entity.setEstLu(request.getEstlu());
        entity.setType(request.getType());
        entity.setDestinataire(request.getDestinataire());
        
        return entity;
    }
    
    /**
     * Met à jour une entité à partir d'un DTO de requête
     * Préserve les IDs et trackingIds existants
     */
    public void updateEntityFromRequest(NotificationRequest request, Notification entity) {
        if (request == null || entity == null) {
            return;
        }
        
        // Mise à jour des attributs non-ID
        entity.setMessage(request.getMessage());
        entity.setDate(request.getDate());
        entity.setEstLu(request.getEstlu());
        entity.setType(request.getType());
        entity.setDestinataire(request.getDestinataire());
    }
    
    /**
     * Convertit une liste d'entités en liste de DTOs de réponse
     */
    public List<NotificationResponse> toResponseList(List<Notification> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 