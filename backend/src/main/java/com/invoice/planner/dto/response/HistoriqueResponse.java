package com.invoice.planner.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.UUID;
import java.util.List;
import java.time.LocalDate;
import com.invoice.planner.entity.User;
import java.time.LocalDateTime;

/**
 * DTO pour retourner les données de l'entité Historique au frontend.
 * Contient uniquement les données nécessaires pour la présentation,
 * et non toute la structure de l'entité d'origine pour des raisons de sécurité.
 */
public class HistoriqueResponse {
    // Identifiant public exposé au frontend (jamais l'ID interne de la base de données)
    private UUID trackingId;
    
    private String action;
    private LocalDate date;
    private User utilisateur;
    private String details;
    
    // Attributs relationnels avec seulement les champs nécessaires
    // Relation ManyToOne - retourne uniquement les champs sélectionnés
    private UUID utilisateurTrackingId;
    private String utilisateurName; // Champ personnalisé sélectionné par l'utilisateur
    
    // Métadonnées
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructeur par défaut
    public HistoriqueResponse() {
    }
    
    // Getters et setters
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
    
    public UUID getUtilisateurTrackingId() {
        return utilisateurTrackingId;
    }
    
    public void setUtilisateurTrackingId(UUID utilisateurTrackingId) {
        this.utilisateurTrackingId = utilisateurTrackingId;
    }
    
    public String getUtilisateurName() {
        return utilisateurName;
    }
    
    public void setUtilisateurName(String utilisateurName) {
        this.utilisateurName = utilisateurName;
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