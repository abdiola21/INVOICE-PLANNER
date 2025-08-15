package com.invoice.planner.controller;

import java.util.List;
import java.util.UUID;

import com.invoice.planner.dto.response.ClientResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.invoice.planner.dto.request.PrestationRequest;
import com.invoice.planner.dto.response.PrestationResponse;
import com.invoice.planner.service.PrestationService;
import com.invoice.planner.utils.ApiResponse;
import com.invoice.planner.entity.Prestation;

@RestController
@RequestMapping("/api/prestations")
public class PrestationController {
    
    private final PrestationService service;
    
    public PrestationController(PrestationService service) {
        this.service = service;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<PrestationResponse>> create(@RequestBody PrestationRequest request) {
        PrestationResponse response = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>("Prestation created successfully", response));
    }
    
    @PutMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<PrestationResponse>> update(@PathVariable UUID trackingId, @RequestBody PrestationRequest request) {
        System.out.println(request);
        PrestationResponse response = service.update(trackingId, request);
        return ResponseEntity.ok(new ApiResponse<>("Prestation updated successfully", response));
    }
    
    @DeleteMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID trackingId) {
        service.delete(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Prestation deleted successfully", null));
    }
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<PrestationResponse>> findByTrackingId(@PathVariable UUID trackingId) {
        PrestationResponse response = service.findByTrackingId(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Prestation found successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<PrestationResponse>>> findAll() {
        List<PrestationResponse> responses = service.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Prestations found successfully", responses));
    }

    @GetMapping("/createdBy")
    public ResponseEntity<ApiResponse<List<PrestationResponse>>> findByCreatedBy() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PrestationResponse> responses = service.findByCreatedBy(email);
        return ResponseEntity.ok(new ApiResponse<>("Prestations found successfully", responses));
    }
} 