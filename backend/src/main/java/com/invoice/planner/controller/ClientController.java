package com.invoice.planner.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.invoice.planner.dto.request.ClientRequest;
import com.invoice.planner.dto.response.ClientResponse;
import com.invoice.planner.service.ClientService;
import com.invoice.planner.utils.ApiResponse;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    
    private final ClientService service;
    
    public ClientController(ClientService service) {
        this.service = service;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<ClientResponse>> create(@RequestBody ClientRequest request) {
        ClientResponse response = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>("Client created successfully", response));
    }
    
    @PutMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<ClientResponse>> update(@PathVariable UUID trackingId, @RequestBody ClientRequest request) {
        ClientResponse response = service.update(trackingId, request);
        return ResponseEntity.ok(new ApiResponse<>("Client updated successfully", response));
    }
    
    @DeleteMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID trackingId) {
        service.delete(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Client deleted successfully", null));
    }
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<ClientResponse>> findByTrackingId(@PathVariable UUID trackingId) {
        ClientResponse response = service.findByTrackingId(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Client found successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientResponse>>> findAll() {
        List<ClientResponse> responses = service.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Clients found successfully", responses));
    }

    @GetMapping("/createdBy")
    public ResponseEntity<ApiResponse<List<ClientResponse>>> findByCreatedBy() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ClientResponse> responses = service.findByCreatedBy(email);
        return ResponseEntity.ok(new ApiResponse<>("Clients found successfully", responses));
    }
} 