package com.invoice.planner.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.invoice.planner.dto.request.UserRoleRequest;
import com.invoice.planner.dto.response.UserRoleResponse;
import com.invoice.planner.service.UserRoleService;
import com.invoice.planner.utils.ApiResponse;

@RestController
@RequestMapping("/api/userroles")
public class UserRoleController {
    
    private final UserRoleService service;
    
    public UserRoleController(UserRoleService service) {
        this.service = service;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserRoleResponse>> create(@RequestBody UserRoleRequest request) {
        UserRoleResponse response = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>("UserRole created successfully", response));
    }
    
    @PutMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<UserRoleResponse>> update(@PathVariable UUID trackingId, @RequestBody UserRoleRequest request) {
        UserRoleResponse response = service.update(trackingId, request);
        return ResponseEntity.ok(new ApiResponse<>("UserRole updated successfully", response));
    }
    
    @DeleteMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID trackingId) {
        service.delete(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("UserRole deleted successfully", null));
    }
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<UserRoleResponse>> findByTrackingId(@PathVariable UUID trackingId) {
        UserRoleResponse response = service.findByTrackingId(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("UserRole found successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserRoleResponse>>> findAll() {
        List<UserRoleResponse> responses = service.findAll();
        return ResponseEntity.ok(new ApiResponse<>("UserRoles found successfully", responses));
    }
} 