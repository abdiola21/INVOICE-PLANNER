package com.invoice.planner.entity;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.invoice.planner.utils.AuditTable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entité représentant une prestation
 */
@Entity
@Table(name = "prestations")
public class Prestation extends AuditTable implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private UUID trackingId;
    
    @Column(nullable = false)
    private String designation;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private Double prixUnitaire = 0.0;

    @Column(nullable = false)
    private int duree = 1;
    
    @Column(nullable = false)
    private Double prixTotal = 0.0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devis_id")
    @JsonBackReference
    private Devis devis;
    
    @PrePersist
    public void prePersist() {
        if (this.trackingId == null) {
            this.trackingId = UUID.randomUUID();
        }
        this.prixTotal = calculerMontant();
    }
    
    // Constructeurs
    public Prestation() {
    }
    
    public Prestation(String designation, String description, Double prixUnitaire, int duree) {
        this.designation = designation;
        this.description = description;
        this.prixUnitaire = prixUnitaire;
        this.duree = duree;
        this.prixTotal = calculerMontant();
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
    
    public Devis getDevis() {
        return devis;
    }
    
    public void setDevis(Devis devis) {
        this.devis = devis;
    }

    // Méthodes métier
    public double calculerMontant() {
        if (prixUnitaire == null) {
            return 0.0;
        }
        return duree * prixUnitaire;
    }
} 