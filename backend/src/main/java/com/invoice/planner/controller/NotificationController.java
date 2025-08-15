package com.invoice.planner.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.invoice.planner.dto.request.NotificationRequest;
import com.invoice.planner.dto.response.NotificationResponse;
import com.invoice.planner.service.NotificationService;
import com.invoice.planner.utils.ApiResponse;
import com.invoice.planner.entity.Notification;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    private final NotificationService service;
    
    public NotificationController(NotificationService service) {
        this.service = service;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> create(@RequestBody NotificationRequest request) {
        NotificationResponse response = service.create(request);
        return ResponseEntity.ok(new ApiResponse<>("Notification created successfully", response));
    }
    
    @PutMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<NotificationResponse>> update(@PathVariable UUID trackingId, @RequestBody NotificationRequest request) {
        NotificationResponse response = service.update(trackingId, request);
        return ResponseEntity.ok(new ApiResponse<>("Notification updated successfully", response));
    }
    
    @DeleteMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID trackingId) {
        service.delete(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Notification deleted successfully", null));
    }
    
    @GetMapping("/{trackingId}")
    public ResponseEntity<ApiResponse<NotificationResponse>> findByTrackingId(@PathVariable UUID trackingId) {
        NotificationResponse response = service.findByTrackingId(trackingId);
        return ResponseEntity.ok(new ApiResponse<>("Notification found successfully", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> findAll() {
        List<NotificationResponse> responses = service.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Notifications found successfully", responses));
    }
} 