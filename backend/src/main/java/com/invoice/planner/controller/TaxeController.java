package com.invoice.planner.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.invoice.planner.dto.request.TaxeRequest;
import com.invoice.planner.dto.response.TaxeResponse;
import com.invoice.planner.service.TaxeService;
import com.invoice.planner.utils.ApiResponse;
import com.invoice.planner.entity.Taxe;

@RestController
@RequestMapping("/api/taxes")
public class TaxeController {
    
    private final TaxeService service;
    
    public TaxeController(TaxeService service) {
        this.service = service;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<TaxeResponse>> create(@RequestBody TaxeRequest request) {
        TaxeResponse response = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>("Taxe created successfully", response));
    }
    
    @PutMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<TaxeResponse>> update(@PathVariable UUID trackingId, @RequestBody TaxeRequest request) {
        TaxeResponse response = service.update(trackingId, request);
        return ResponseEntity.ok(new ApiResponse<>("Taxe updated successfully", response));
    }
    
    @DeleteMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID trackingId) {
        service.delete(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Taxe deleted successfully", null));
    }
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<TaxeResponse>> findByTrackingId(@PathVariable UUID trackingId) {
        TaxeResponse response = service.findByTrackingId(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Taxe found successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaxeResponse>>> findAll() {
        List<TaxeResponse> responses = service.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Taxes found successfully", responses));
    }
} 