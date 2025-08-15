package com.invoice.planner.mapper;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.ClientRequest;
import com.invoice.planner.dto.response.ClientResponse;
import com.invoice.planner.entity.Client;

@Component
public class ClientMapper {
    
    /**
     * Convertit une entité en DTO de réponse
     * Ne transfère que les données nécessaires pour la présentation au frontend
     */
    public ClientResponse toResponse(Client entity) {
        if (entity == null) {
            return null;
        }
        
        ClientResponse response = new ClientResponse();
        // On utilise le trackingId comme identifiant public
        response.setTrackingId(entity.getTrackingId());
        
        // Mapping des attributs standard
        response.setNom(entity.getNom());
        response.setPrenom(entity.getPrenom());
        response.setAdresse(entity.getAdresse());
        response.setEmail(entity.getEmail());
        response.setTelephone(entity.getTelephone());
        response.setSociete(entity.getSociete());
        response.setNumeroTVA(entity.getNumeroTVA());
        response.setVille(entity.getVille());
        response.setPays(entity.getPays());
        
        // Métadonnées si disponibles
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        
        return response;
    }
    
    /**
     * Convertit un DTO de requête en entité
     * Pour la création, les IDs seront générés par le système
     */
    public Client toEntity(ClientRequest request) {
        if (request == null) {
            return null;
        }
        
        Client entity = new Client();
        
        // Mapping des attributs standard
        entity.setNom(request.getNom());
        entity.setPrenom(request.getPrenom());
        entity.setAdresse(request.getAdresse());
        entity.setEmail(request.getEmail());
        entity.setTelephone(request.getTelephone());
        entity.setSociete(request.getSociete());
        entity.setNumeroTVA(request.getNumeroTVA());
        entity.setVille(request.getVille());
        entity.setPays(request.getPays());
        
        return entity;
    }
    
    /**
     * Met à jour une entité à partir d'un DTO de requête
     * Préserve les IDs et trackingIds existants
     */
    public void updateEntityFromRequest(ClientRequest request, Client entity) {
        if (request == null || entity == null) {
            return;
        }
        
        // Mise à jour des attributs non-ID
        entity.setNom(request.getNom());
        entity.setPrenom(request.getPrenom());
        entity.setAdresse(request.getAdresse());
        entity.setEmail(request.getEmail());
        entity.setTelephone(request.getTelephone());
        entity.setSociete(request.getSociete());
        entity.setNumeroTVA(request.getNumeroTVA());
        entity.setVille(request.getVille());
        entity.setPays(request.getPays());
    }
    
    /**
     * Convertit une liste d'entités en liste de DTOs de réponse
     */
    public List<ClientResponse> toResponseList(List<Client> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
} 