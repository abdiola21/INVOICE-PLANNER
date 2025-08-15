package com.invoice.planner.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.invoice.planner.dto.request.DevisRequest;
import com.invoice.planner.dto.response.DevisResponse;
import com.invoice.planner.service.DevisService;
import com.invoice.planner.service.FactureService;
import com.invoice.planner.utils.ApiResponse;
import com.invoice.planner.entity.Devis;
import com.invoice.planner.repository.DevisRepository;
import com.invoice.planner.enums.EtatDevis;
import com.invoice.planner.dto.response.FactureResponse;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/deviss")
public class DevisController {
    
    private final DevisService service;
    private final DevisRepository devisRepository;
    private final FactureService factureService;

    
    public DevisController(DevisService service, DevisRepository devisRepository, FactureService factureService) {
        this.service = service;
        this.devisRepository = devisRepository;
        this.factureService = factureService;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<DevisResponse>> create(@RequestBody DevisRequest request) {
        DevisResponse response = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>("Devis created successfully", response));
    }
    
    @PutMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<DevisResponse>> update(@PathVariable UUID trackingId, @RequestBody DevisRequest request) {
        DevisResponse response = service.update(trackingId, request);
        return ResponseEntity.ok(new ApiResponse<>("Devis updated successfully", response));
    }
    
    @DeleteMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID trackingId) {
        service.delete(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Devis deleted successfully", null));
    }
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<DevisResponse>> findByTrackingId(@PathVariable UUID trackingId) {
        DevisResponse response = service.findByTrackingId(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Devis found successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<DevisResponse>>> findAll() {
        List<DevisResponse> responses = service.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Deviss found successfully", responses));
    }

    @GetMapping("/createdBy")
    public ResponseEntity<ApiResponse<List<DevisResponse>>> findByCreatedBy() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<DevisResponse> responses = service.findByCreatedBy(email);
        return ResponseEntity.ok(new ApiResponse<>("Deviss found successfully", responses));
    }

// @GetMapping("/{trackingId}/status")
// public ResponseEntity<ApiResponse<String>> getStatus(@PathVariable UUID trackingId) {
//     DevisResponse response = service.findByTrackingId(trackingId);
//     String status = response.getStatut().toString();
    
//     return ResponseEntity.ok(new ApiResponse<>("Devis status retrieved successfully", status));
// }


    public static class StatutRequest {
        public String statut;
    }

    @PutMapping("/{trackingId}/status")
    public ResponseEntity<ApiResponse<String>> updateStatus(@PathVariable UUID trackingId, @RequestBody StatutRequest request) {
        Devis devis = devisRepository.findByTrackingId(trackingId)
            .orElseThrow(() -> new RuntimeException("Devis not found"));
        devis.setStatut(EtatDevis.valueOf(request.statut));
        devisRepository.save(devis);
        return ResponseEntity.ok(new ApiResponse<>("Devis status updated successfully", request.statut));
    }

@PostMapping("/{trackingId}/convertToFacture")
public ResponseEntity<ApiResponse<UUID>> convertToFacture(@PathVariable UUID trackingId) {
    try {
        // Vérifier que le devis existe et est accepté
        DevisResponse devis = service.findByTrackingId(trackingId);
        if (devis == null) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Devis non trouvé", null));
        }
        
        if (!"ACCEPTE".equalsIgnoreCase(devis.getStatut().toString())) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Seuls les devis acceptés peuvent être convertis en facture", null));
        }
        
        // Créer la facture à partir du devis
        FactureResponse factureResponse = factureService.createByDevisId(trackingId);
        
        return ResponseEntity.ok(new ApiResponse<>("Facture créée avec succès", factureResponse.getTrackingId()));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse<>("Erreur lors de la conversion en facture: " + e.getMessage(), null));
    }
}
}