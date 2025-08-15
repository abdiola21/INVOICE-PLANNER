package com.invoice.planner.dto.request;

import java.util.UUID;
import java.util.UUID;
import com.invoice.planner.entity.Devis;
import java.util.List;
import java.time.LocalDateTime;
import com.invoice.planner.entity.Facture;

/**
 * DTO pour recevoir les données du frontend pour l'entité Taxe.
 * Contient uniquement les champs modifiables par l'utilisateur.
 */
public class TaxeRequest {
    
    private String nom;
    private Double taux;
    private boolean estObligatoire;
    private Devis devis;
    private Facture facture;
    
    // Constructeur par défaut
    public TaxeRequest() {
    }
    
    // Getters et setters
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    public Double getTaux() {
        return taux;
    }
    
    public void setTaux(Double taux) {
        this.taux = taux;
    }
    public boolean getEstobligatoire() {
        return estObligatoire;
    }
    
    public void setEstobligatoire(boolean estObligatoire) {
        this.estObligatoire = estObligatoire;
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
} 