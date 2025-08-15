package com.invoice.planner.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour retourner les données de l'entité UserRole au frontend.
 * Contient uniquement les données nécessaires pour la présentation,
 * et non toute la structure de l'entité d'origine pour des raisons de sécurité.
 */
public class UserRoleResponse {
    // Identifiant public exposé au frontend (jamais l'ID interne de la base de données)
    private UUID trackingId;
    
    private String roleName;
    private String description;
    private boolean isActive;
    
    
    // Métadonnées
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructeur par défaut
    public UserRoleResponse() {
    }
    
    // Getters et setters
    public UUID getTrackingId() {
        return trackingId;
    }
    
    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }
    
    public String getRolename() {
        return roleName;
    }
    
    public void setRolename(String roleName) {
        this.roleName = roleName;
    }
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean getIsactive() {
        return isActive;
    }
    
    public void setIsactive(boolean isActive) {
        this.isActive = isActive;
    }
    
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 