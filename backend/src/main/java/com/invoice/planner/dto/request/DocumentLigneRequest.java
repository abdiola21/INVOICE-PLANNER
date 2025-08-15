package com.invoice.planner.dto.request;

import java.util.UUID;
import java.util.UUID;
import com.invoice.planner.entity.Devis;
import java.util.List;
import java.time.LocalDateTime;
import com.invoice.planner.entity.Facture;

/**
 * DTO pour recevoir les données du frontend pour l'entité DocumentLigne.
 * Contient uniquement les champs modifiables par l'utilisateur.
 */
public class DocumentLigneRequest {
    
    private String designation;
    private Devis devis;
    private Facture facture;
    private int quantite;
    private Double prixUnitaire;
    
    // Constructeur par défaut
    public DocumentLigneRequest() {
    }
    
    // Getters et setters
    public String getDesignation() {
        return designation;
    }
    
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public Devis getDevis() {
        return devis;
    }
    
    public void setDevis(Devis devis) {
        this.devis = devis;
    }
    public Facture getFacture() {
        return facture;
    }
    
    public void setFacture(Facture facture) {
        this.facture = facture;
    }
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    public Double getPrixunitaire() {
        return prixUnitaire;
    }
    
    public void setPrixunitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
} 