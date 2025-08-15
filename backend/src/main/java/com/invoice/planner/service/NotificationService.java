package com.invoice.planner.service;

import java.util.List;
import java.util.UUID;

import com.invoice.planner.dto.request.NotificationRequest;
import com.invoice.planner.dto.response.NotificationResponse;

public interface NotificationService {
    
    NotificationResponse create(NotificationRequest request);
    
    NotificationResponse update(UUID trackingId, NotificationRequest request);
    
    void delete(UUID trackingId);
    
    NotificationResponse findByTrackingId(UUID trackingId);
    
    List<NotificationResponse> findAll();
    
    List<NotificationResponse> search(String term);
} 