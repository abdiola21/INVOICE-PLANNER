package com.invoice.planner.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.invoice.planner.utils.AuditTable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entité représentant un événement historique dans le système
 */
@Entity
@Table(name = "historiques")
public class Historique extends AuditTable implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private UUID trackingId;
    
    private String action;
    
    @Temporal(TemporalType.DATE)
    private LocalDate date;
    
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User utilisateur;
    
    @Column(length = 2000)
    private String details;
    
    // Constructeurs
    public Historique() {
    }
    
    public Historique(String action, LocalDate date, User utilisateur, String details) {
        this.action = action;
        this.date = date;
        this.utilisateur = utilisateur;
        this.details = details;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    
    // Méthodes métier
    public static void enregistrerAction(User utilisateur, String action, String details) {
        Historique historique = new Historique(action, LocalDate.now(), utilisateur, details);
        // Logique pour sauvegarder l'historique
    }
} 