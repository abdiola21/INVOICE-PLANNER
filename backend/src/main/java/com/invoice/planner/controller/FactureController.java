package com.invoice.planner.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.invoice.planner.controller.DevisController.StatutRequest;
import com.invoice.planner.dto.request.FactureRequest;
import com.invoice.planner.dto.response.FactureResponse;
import com.invoice.planner.service.FactureService;
import com.invoice.planner.utils.ApiResponse;
import com.invoice.planner.entity.Devis;
import com.invoice.planner.entity.Facture;
import com.invoice.planner.enums.EtatDevis;
import com.invoice.planner.enums.EtatFacture;
import com.invoice.planner.repository.FactureRepository;

@RestController
@RequestMapping("/api/factures")
public class FactureController {
    
    private final FactureService service;
    private final FactureRepository factureRepository;
    
    public FactureController(FactureService service, FactureRepository factureRepository) {
        this.service = service;
        this.factureRepository = factureRepository;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<FactureResponse>> create(@RequestBody FactureRequest request) {
        FactureResponse response = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>("Facture created successfully", response));
    }

    @GetMapping("save/{trackingId}")
    public ResponseEntity<ApiResponse<FactureResponse>> saveByDevisId(@PathVariable UUID trackingId) {
        FactureResponse response = service.createByDevisId(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Facture created successfully", response));
    }
    
    @PutMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<FactureResponse>> update(@PathVariable UUID trackingId, @RequestBody FactureRequest request) {
        FactureResponse response = service.update(trackingId, request);
        return ResponseEntity.ok(new ApiResponse<>("Facture updated successfully", response));
    }
    
    @DeleteMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID trackingId) {
        service.delete(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Facture deleted successfully", null));
    }
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<FactureResponse>> findByTrackingId(@PathVariable UUID trackingId) {
        FactureResponse response = service.findByTrackingId(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Facture found successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<FactureResponse>>> findAll() {
        List<FactureResponse> responses = service.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Factures found successfully", responses));
    }
    
    
    public static class StatutRequest {
        public String statut;
    }

    @PutMapping("/{trackingId}/status")
    public ResponseEntity<ApiResponse<String>> updateStatus(@PathVariable UUID trackingId, @RequestBody StatutRequest request) {
        Facture facture = factureRepository.findByTrackingId(trackingId)
            .orElseThrow(() -> new RuntimeException("Facture not found"));
        facture.setEtat(EtatFacture.valueOf(request.statut));
        factureRepository.save(facture);
        return ResponseEntity.ok(new ApiResponse<>("Facture status updated successfully", request.statut));
    }
    
} 