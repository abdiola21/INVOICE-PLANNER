package com.invoice.planner.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.invoice.planner.dto.request.DocumentLigneRequest;
import com.invoice.planner.dto.response.DocumentLigneResponse;
import com.invoice.planner.service.DocumentLigneService;
import com.invoice.planner.utils.ApiResponse;
import com.invoice.planner.entity.DocumentLigne;

@RestController
@RequestMapping("/api/documentlignes")
public class DocumentLigneController {
    
    private final DocumentLigneService service;
    
    public DocumentLigneController(DocumentLigneService service) {
        this.service = service;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<DocumentLigneResponse>> create(@RequestBody DocumentLigneRequest request) {
        DocumentLigneResponse response = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>("DocumentLigne created successfully", response));
    }
    
    @PutMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<DocumentLigneResponse>> update(@PathVariable UUID trackingId, @RequestBody DocumentLigneRequest request) {
        DocumentLigneResponse response = service.update(trackingId, request);
        return ResponseEntity.ok(new ApiResponse<>("DocumentLigne updated successfully", response));
    }
    
    @DeleteMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID trackingId) {
        service.delete(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("DocumentLigne deleted successfully", null));
    }
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<DocumentLigneResponse>> findByTrackingId(@PathVariable UUID trackingId) {
        DocumentLigneResponse response = service.findByTrackingId(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("DocumentLigne found successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentLigneResponse>>> findAll() {
        List<DocumentLigneResponse> responses = service.findAll();
        return ResponseEntity.ok(new ApiResponse<>("DocumentLignes found successfully", responses));
    }
} 