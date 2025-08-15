package com.invoice.planner.mapper;

import com.invoice.planner.entity.Prestation;
import com.invoice.planner.repository.ClientRepository;
import com.invoice.planner.repository.PrestationRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.DevisRequest;
import com.invoice.planner.dto.response.DevisResponse;
import com.invoice.planner.entity.Devis;
import com.invoice.planner.mapper.PrestationMapper;
import com.invoice.planner.mapper.DocumentLigneMapper;
import com.invoice.planner.mapper.TaxeMapper;

@Component
public class DevisMapper {
    
    /**
     * Convertit une entité en DTO de réponse
     * Ne transfère que les données nécessaires pour la présentation au frontend
     */
    
    private final PrestationMapper prestationMapper = new PrestationMapper();
    private final DocumentLigneMapper documentLigneMapper = new DocumentLigneMapper();
    private final TaxeMapper taxeMapper = new TaxeMapper();
    private final PrestationRepository prestationRepository;
    private final ClientRepository clientRepository;

    public DevisMapper(PrestationRepository prestationRepository, ClientRepository clientRepository) {
        this.prestationRepository = prestationRepository;
        this.clientRepository = clientRepository;
    }

    public DevisResponse toResponse(Devis entity) {
        if (entity == null) {
            return null;
        }
        
        DevisResponse response = new DevisResponse();
        response.setTrackingId(entity.getTrackingId());
        response.setReference(entity.getReference());
        response.setNomProjet(entity.getNomProjet());
        response.setDescription(entity.getDescription());
        
        if (entity.getClient() != null) {
            response.setClientTrackingId(entity.getClient().getTrackingId());
            response.setClientName(entity.getClient().getNom());
            response.setClientEmail(entity.getClient().getEmail()); // Ajout de l'email du client
        }
        
        if (entity.getCreateur() != null) {
            response.setCreateurTrackingId(entity.getCreateur().getTrackingId());
            response.setCreateurName(entity.getCreateur().getNom()); // Ajout du nom du créateur
            response.setCreateurEmail(entity.getCreateur().getEmail()); // Ajout de l'email du créateur
        }
        
        response.setDateEmission(entity.getDateEmission());
        response.setDateEcheance(entity.getDateEcheance());
        response.setPrixTotal(entity.getPrixTotal());
        response.setPrixTTC(entity.getPrixTTC());
        response.setTva(entity.getTva());
        response.setRemise(entity.getRemise());
        response.setStatut(entity.getStatut());
        response.setNotes(entity.getNotes());
        response.setPrestations(prestationMapper.toResponseList(entity.getPrestations()));
        //response.setLignes(documentLigneMapper.toResponseList(entity.getLignes()));
        //response.setTaxes(taxeMapper.toResponseList(entity.getTaxes()));
        
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        
        return response;
    }
    
    /**
     * Convertit un DTO de requête en entité
     * Pour la création, les IDs seront générés par le système
     * Les prix (prixTotal et prixTTC) seront calculés par le service
     */
    public Devis toEntity(DevisRequest request) {
        if (request == null) {
            return null;
        }

        
        Devis entity = new Devis();
        
        entity.setTrackingId(UUID.randomUUID());
        entity.setReference(request.getReference());
        entity.setNomProjet(request.getNomProjet());
        entity.setDescription(request.getDescription());
        entity.setClient(clientRepository.findByTrackingId(request.getClient().getTrackingId()).orElseThrow(()->new RuntimeException("Client non trouver")));
        entity.setCreateur(request.getCreateur());
        entity.setDateEmission(request.getDateEmission());
        entity.setDateEcheance(request.getDateEcheance());
        entity.setTva(request.getTva());
        entity.setRemise(request.getRemise());
        entity.setStatut(request.getStatut());
        entity.setNotes(request.getNotes());

        for(int i=0;i<request.getPrestations().size();i++){

            request.getPrestations().set(i,prestationRepository.findByTrackingId(request.getPrestations().get(i).getTrackingId()).orElseThrow(()->new RuntimeException("Prestation non trouver")));

        }
        entity.setPrestations(request.getPrestations());
        entity.setLignes(request.getLignes());
        entity.setTaxes(request.getTaxes());
        
        return entity;
    }
    
    /**
     * Met à jour une entité à partir d'un DTO de requête
     * Préserve les IDs et trackingIds existants
     * Les prix (prixTotal et prixTTC) seront recalculés par le service
     */
    public void updateEntityFromRequest(DevisRequest request, Devis entity) {
        if (request == null || entity == null) {
            return;
        }

        System.out.println(request.getTva());
        System.out.println(request.getRemise());
        
        entity.setReference(request.getReference());
        entity.setNomProjet(request.getNomProjet());
        entity.setDescription(request.getDescription());
        entity.setClient(clientRepository.findByTrackingId(request.getClient().getTrackingId()).orElseThrow(()->new RuntimeException("Client non trouver")));
        entity.setCreateur(request.getCreateur());
        entity.setDateEmission(request.getDateEmission());
        entity.setDateEcheance(request.getDateEcheance());
        entity.setTva(request.getTva());
        entity.setRemise(request.getRemise());
        entity.setStatut(request.getStatut());
        entity.setNotes(request.getNotes());
        for(int i=0;i<request.getPrestations().size();i++){
            request.getPrestations().set(i,prestationRepository.findByTrackingId(request.getPrestations().get(i).getTrackingId()).orElseThrow(()->new RuntimeException("Prestation non trouver")));
        }
        entity.setLignes(request.getLignes());
        entity.setTaxes(request.getTaxes());
    }
    
    /**
     * Convertit une liste d'entités en liste de DTOs de réponse
     */
    public List<DevisResponse> toResponseList(List<Devis> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}