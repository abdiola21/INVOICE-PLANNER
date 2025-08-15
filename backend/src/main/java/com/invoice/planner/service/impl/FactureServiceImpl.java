package com.invoice.planner.service.impl;

import com.invoice.planner.entity.Devis;
import com.invoice.planner.repository.DevisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.invoice.planner.dto.request.FactureRequest;
import com.invoice.planner.dto.response.FactureResponse;
import com.invoice.planner.exception.ResourceNotFoundException;
import com.invoice.planner.mapper.FactureMapper;
import com.invoice.planner.repository.FactureRepository;
import com.invoice.planner.service.FactureService;
import com.invoice.planner.entity.Facture;
import com.invoice.planner.service.EmailService;
import com.invoice.planner.service.CompanyProfileService;
import com.invoice.planner.dto.response.CompanyProfileResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import com.invoice.planner.entity.User;

@Service
@Transactional
public class FactureServiceImpl implements FactureService {
    
    private final FactureRepository repository;
    private final FactureMapper mapper;
    private final DevisRepository devisRepository;
    private final EmailService emailService;
    private final CompanyProfileService companyProfileService;
    
    public FactureServiceImpl(FactureRepository repository, FactureMapper mapper, DevisRepository devisRepository, EmailService emailService, CompanyProfileService companyProfileService) {
        this.repository = repository;
        this.mapper = mapper;
        this.devisRepository = devisRepository;
        this.emailService = emailService;
        this.companyProfileService = companyProfileService;
    }
    
    @Override
    @Transactional
    public FactureResponse create(FactureRequest request) {
        // Récupérer l'utilisateur courant
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = null;
        if (principal instanceof User) {
            userId = ((User) principal).getId();
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            // Si tu utilises un UserDetails custom, adapte ici pour récupérer l'ID
            // userId = ((UserDetailsImpl) principal).getId();
        }
        CompanyProfileResponse companyProfile = userId != null ? companyProfileService.getProfileByUserId(userId) : null;
        String logoPath = null;
        if (companyProfile != null && companyProfile.isHasLogo() && companyProfile.getTrackingId() != null) {
            // Le logo est stocké dans le dossier d'uploads, adapte le chemin si besoin
            logoPath = "uploads/" + companyProfile.getTrackingId() + "_" + companyProfile.getCompanyName() + ".png";
        }
        // Envoi du devis au client avant la création de la facture
        for (Devis devis : request.getDevis()) {
            if (devis.getClient() != null && devis.getClient().getEmail() != null) {
                java.io.File pdf = devis.genererPDF(
                    logoPath,
                    companyProfile != null ? companyProfile.getCompanyName() : null,
                    companyProfile != null ? companyProfile.getAddress() : null,
                    companyProfile != null ? companyProfile.getCity() : null,
                    companyProfile != null ? companyProfile.getPostalCode() : null,
                    companyProfile != null ? companyProfile.getCountry() : null,
                    companyProfile != null ? companyProfile.getPhoneNumber() : null,
                    companyProfile != null ? companyProfile.getEmail() : null,
                    companyProfile != null ? companyProfile.getWebsite() : null
                );
                String pdfPath = pdf != null ? pdf.getAbsolutePath() : null;
                emailService.sendDevisWithAttachment(
                    devis.getClient().getEmail(),
                    "Votre devis avant facturation",
                    "Bonjour, veuillez trouver ci-joint votre devis avant la création de la facture.",
                    pdfPath
                );
            }
        }
        Facture entity = mapper.toEntity(request);
        entity.setTrackingId(UUID.randomUUID());
        Facture savedEntity = repository.save(entity);
        for(Devis devis : request.getDevis()){
            devis.setFacture(entity);
            devisRepository.save(devis);
        }
        return mapper.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public FactureResponse createByDevisId(UUID trackingId) {

        Devis devis = devisRepository.findByTrackingId(trackingId).orElseThrow(() -> new ResourceNotFoundException("Devis", "trackingId", trackingId));
        Facture entity = new Facture();
        entity.addDevis(devis);
        FactureResponse response = mapper.toResponse(repository.save(entity));
        devis.setFacture(entity);

        return response;
    }

    @Override
    public FactureResponse update(UUID trackingId, FactureRequest request) {
        Facture entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Facture", "trackingId", trackingId));
        
        mapper.updateEntityFromRequest(request, entity);
        entity.setTrackingId(trackingId);
        Facture updatedEntity = repository.save(entity);
        return mapper.toResponse(updatedEntity);
    }
    
    @Override
    public void delete(UUID trackingId) {
        Facture entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Facture", "trackingId", trackingId));
        
        repository.delete(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public FactureResponse findByTrackingId(UUID trackingId) {
        Facture entity = repository.findByTrackingId(trackingId)
            .orElseThrow(() -> new ResourceNotFoundException("Facture", "trackingId", trackingId));
        
        return mapper.toResponse(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FactureResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FactureResponse> search(String term) {
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