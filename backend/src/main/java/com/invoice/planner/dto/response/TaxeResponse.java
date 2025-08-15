package com.invoice.planner.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.UUID;
import com.invoice.planner.entity.Devis;
import java.util.List;
import java.time.LocalDateTime;
import com.invoice.planner.entity.Facture;

/**
 * DTO pour retourner les données de l'entité Taxe au frontend.
 * Contient uniquement les données nécessaires pour la présentation,
 * et non toute la structure de l'entité d'origine pour des raisons de sécurité.
 */
public class TaxeResponse {
    // Identifiant public exposé au frontend (jamais l'ID interne de la base de données)
    private UUID trackingId;
    
    private String nom;
    private Double taux;
    private boolean estObligatoire;
    private Devis devis;
    private Facture facture;
    
    // Attributs relationnels avec seulement les champs nécessaires
    // Relation ManyToOne - retourne uniquement les champs sélectionnés
    private UUID devisTrackingId;
    private String devisName; // Champ personnalisé sélectionné par l'utilisateur
    // Relation ManyToOne - retourne uniquement les champs sélectionnés
    private UUID factureTrackingId;
    private String factureName; // Champ personnalisé sélectionné par l'utilisateur
    
    // Métadonnées
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructeur par défaut
    public TaxeResponse() {
    }
    
    // Getters et setters
    public UUID getTrackingId() {
        return trackingId;
    }
    
    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }
    
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
    
    public UUID getDevisTrackingId() {
        return devisTrackingId;
    }
    
    public void setDevisTrackingId(UUID devisTrackingId) {
        this.devisTrackingId = devisTrackingId;
    }
    
    public String getDevisName() {
        return devisName;
    }
    
    public void setDevisName(String devisName) {
        this.devisName = devisName;
    }
    public UUID getFactureTrackingId() {
        return factureTrackingId;
    }
    
    public void setFactureTrackingId(UUID factureTrackingId) {
        this.factureTrackingId = factureTrackingId;
    }
    
    public String getFactureName() {
        return factureName;
    }
    
    public void setFactureName(String factureName) {
        this.factureName = factureName;
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