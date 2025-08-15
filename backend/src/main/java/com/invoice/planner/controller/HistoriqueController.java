package com.invoice.planner.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.invoice.planner.dto.request.HistoriqueRequest;
import com.invoice.planner.dto.response.HistoriqueResponse;
import com.invoice.planner.service.HistoriqueService;
import com.invoice.planner.utils.ApiResponse;
import com.invoice.planner.entity.Historique;

@RestController
@RequestMapping("/api/historiques")
public class HistoriqueController {
    
    private final HistoriqueService service;
    
    public HistoriqueController(HistoriqueService service) {
        this.service = service;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<HistoriqueResponse>> create(@RequestBody HistoriqueRequest request) {
        HistoriqueResponse response = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>("Historique created successfully", response));
    }
    
    @PutMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<HistoriqueResponse>> update(@PathVariable UUID trackingId, @RequestBody HistoriqueRequest request) {
        HistoriqueResponse response = service.update(trackingId, request);
        return ResponseEntity.ok(new ApiResponse<>("Historique updated successfully", response));
    }
    
    @DeleteMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID trackingId) {
        service.delete(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Historique deleted successfully", null));
    }
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<HistoriqueResponse>> findByTrackingId(@PathVariable UUID trackingId) {
        HistoriqueResponse response = service.findByTrackingId(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Historique found successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<HistoriqueResponse>>> findAll() {
        List<HistoriqueResponse> responses = service.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Historiques found successfully", responses));
    }
} 