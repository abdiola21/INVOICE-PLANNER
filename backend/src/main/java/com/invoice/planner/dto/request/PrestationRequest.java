package com.invoice.planner.dto.request;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

/**
 * DTO pour recevoir les données du frontend pour l'entité Prestation.
 * Contient uniquement les champs modifiables par l'utilisateur.
 */
public class PrestationRequest {
    
    private String designation;
    private String description;
    private Double prixUnitaire;
    private int duree;
    private Double prixTotal;

    
    // Constructeur par défaut
    public PrestationRequest() {
    }
    
    // Getters et setters
    public String getDesignation() {
        return designation;
    }
    
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public int getDuree() {
        return duree;
    }
    
    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }


    @Override
    public String toString() {
        return "PrestationRequest{" +
                "designation='" + designation + '\'' +
                ", description='" + description + '\'' +
                ", prixUnitaire=" + prixUnitaire +
                ", duree=" + duree +
                ", prixTotal=" + prixTotal +
                '}';
    }
}