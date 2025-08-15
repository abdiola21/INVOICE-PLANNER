package com.invoice.planner.entity;

import java.io.Serializable;
import java.util.UUID;

import com.invoice.planner.utils.AuditTable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entité représentant une taxe appliquée à un document
 */
@Entity
@Table(name = "taxes")
public class Taxe extends AuditTable implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private UUID trackingId;
    
    private String nom;
    
    private Double taux;
    
    private boolean estObligatoire;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devis_id")
    private Devis devis;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facture_id")
    private Facture facture;
    
    // Constructeurs
    public Taxe() {
    }
    
    public Taxe(String nom, Double taux, boolean estObligatoire) {
        this.nom = nom;
        this.taux = taux;
        this.estObligatoire = estObligatoire;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isEstObligatoire() {
        return estObligatoire;
    }

    public void setEstObligatoire(boolean estObligatoire) {
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
    
    // Méthodes métier
    public double calculerMontant(double montantHT) {
        if (taux == null) {
            return 0.0;
        }
        return montantHT * (taux / 100);
    }
} 