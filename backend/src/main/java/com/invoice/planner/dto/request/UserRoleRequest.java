package com.invoice.planner.dto.request;

/**
 * DTO pour recevoir les données du frontend pour l'entité UserRole.
 * Contient uniquement les champs modifiables par l'utilisateur.
 */
public class UserRoleRequest {
    
    private String roleName;
    private String description;
    private boolean isActive;
    
    // Constructeur par défaut
    public UserRoleRequest() {
    }
    
    // Getters et setters
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
} 