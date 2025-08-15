package com.invoice.planner.service;

import java.util.List;
import java.util.UUID;

import com.invoice.planner.dto.request.UserRoleRequest;
import com.invoice.planner.dto.response.UserRoleResponse;

public interface UserRoleService {
    
    UserRoleResponse create(UserRoleRequest request);
    
    UserRoleResponse update(UUID trackingId, UserRoleRequest request);
    
    void delete(UUID trackingId);
    
    UserRoleResponse findByTrackingId(UUID trackingId);
    
    List<UserRoleResponse> findAll();
    
    List<UserRoleResponse> search(String term);
} 