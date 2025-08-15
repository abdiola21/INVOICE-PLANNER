package com.invoice.planner.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.UUID;
import java.util.List;
import java.time.LocalDate;
import com.invoice.planner.entity.User;
import com.invoice.planner.enums.TypeNotification;

import java.time.LocalDateTime;

/**
 * DTO pour retourner les données de l'entité Notification au frontend.
 * Contient uniquement les données nécessaires pour la présentation,
 * et non toute la structure de l'entité d'origine pour des raisons de sécurité.
 */
public class NotificationResponse {
    // Identifiant public exposé au frontend (jamais l'ID interne de la base de données)
    private UUID trackingId;
    
    private String message;
    private LocalDate date;
    private boolean estLu;
    private TypeNotification type;
    private User destinataire;
    
    // Attributs relationnels avec seulement les champs nécessaires
    // Relation ManyToOne - retourne uniquement les champs sélectionnés
    private UUID destinataireTrackingId;
    private String destinataireName; // Champ personnalisé sélectionné par l'utilisateur
    
    // Métadonnées
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructeur par défaut
    public NotificationResponse() {
    }
    
    // Getters et setters
    public UUID getTrackingId() {
        return trackingId;
    }
    
    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public boolean getEstlu() {
        return estLu;
    }
    
    public void setEstlu(boolean estLu) {
        this.estLu = estLu;
    }
    public TypeNotification getType() {
        return type;
    }
    
    public void setType(TypeNotification type) {
        this.type = type;
    }
    public User getDestinataire() {
        return destinataire;
    }
    
    public void setDestinataire(User destinataire) {
        this.destinataire = destinataire;
    }
    
    public UUID getDestinataireTrackingId() {
        return destinataireTrackingId;
    }
    
    public void setDestinataireTrackingId(UUID destinataireTrackingId) {
        this.destinataireTrackingId = destinataireTrackingId;
    }
    
    public String getDestinataireName() {
        return destinataireName;
    }
    
    public void setDestinataireName(String destinataireName) {
        this.destinataireName = destinataireName;
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