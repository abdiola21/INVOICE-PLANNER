package com.invoice.planner.mapper;

import com.invoice.planner.entity.Devis;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.FactureRequest;
import com.invoice.planner.dto.response.FactureResponse;
import com.invoice.planner.entity.Facture;

@Component
public class FactureMapper {
    
    /**
     * Convertit une entité en DTO de réponse
     * Ne transfère que les données nécessaires pour la présentation au frontend
     */
    public FactureResponse toResponse(Facture entity) {
        if (entity == null) {
            return null;
        }
        
        FactureResponse response = new FactureResponse();
        // On utilise le trackingId comme identifiant public
        response.setTrackingId(entity.getTrackingId());
        
        // Mapping des attributs standard
        response.setNumero(entity.getNumero());
        response.setClient(entity.getClient());
        response.setDateEcheance(entity.getDateEcheance());
        response.setMontantHT(entity.getMontantHT());
        response.setMontantTTC(entity.getMontantTTC());
        response.setRemise(entity.getRemise());
        response.setEtat(entity.getEtat());
        response.setModereglement(entity.getModeReglement());
        response.setReferencedevis(entity.getReferenceDevis());
        response.setDevis(entity.getDevis());
        
        // Mapping des relations avec seulement les informations nécessaires
        if (entity.getClient() != null) {
            // On récupère le trackingId pour référence
            response.setClientTrackingId(entity.getClient().getTrackingId());
            
            // Mapping des champs personnalisés sélectionnés
            try {
                // On essaie de récupérer la valeur du champ avec le type approprié
                java.lang.reflect.Method method = entity.getClient().getClass().getMethod("getName");
                Object value = method.invoke(entity.getClient());
                
                // On convertit la valeur en String car c'est le type utilisé dans le DTO de réponse
                response.setClientName(value != null ? value.toString() : null);
            } catch (Exception e) {
                // Fallback sur vide si la méthode n'existe pas
                response.setClientName("");
                // Log pour le debugging
                System.err.println("Impossible de récupérer l'attribut '" + "name" + "' de l'entité 'client'");
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
    public Facture toEntity(FactureRequest request) {
        if (request == null) {
            return null;
        }
        
        Facture entity = new Facture();
        
        // Mapping des attributs standard
        entity.setNumero(request.getNumero());
        entity.setClient(request.getClient());
        entity.setDateEcheance(request.getDateEcheance());
        entity.setMontantHT(request.getMontantht());
        entity.setMontantTTC(request.getMontantttc());
        entity.setRemise(request.getRemise());
        entity.setEtat(request.getEtat());
        entity.setModeReglement(request.getModereglement());
        entity.setReferenceDevis(request.getReferencedevis());

        if(!request.getDevis().isEmpty()){
            for(Devis devis : request.getDevis()){
                entity.getDevis().add(devis);
            }
        }
        
        return entity;
    }
    
    /**
     * Met à jour une entité à partir d'un DTO de requête
     * Préserve les IDs et trackingIds existants
     */
    public void updateEntityFromRequest(FactureRequest request, Facture entity) {
        if (request == null || entity == null) {
            return;
        }
        
        // Mise à jour des attributs non-ID
        entity.setNumero(request.getNumero());
        entity.setClient(request.getClient());
        entity.setDateEcheance(request.getDateEcheance());
        entity.setMontantHT(request.getMontantht());
        entity.setMontantTTC(request.getMontantttc());
        entity.setRemise(request.getRemise());
        entity.setEtat(request.getEtat());
        entity.setModeReglement(request.getModereglement());
        entity.setReferenceDevis(request.getReferencedevis());
        if(!request.getDevis().isEmpty()){
            for(Devis devis : request.getDevis()){
                entity.getDevis().add(devis);
            }
        }
    }
    
    /**
     * Convertit une liste d'entités en liste de DTOs de réponse
     */
    public List<FactureResponse> toResponseList(List<Facture> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 