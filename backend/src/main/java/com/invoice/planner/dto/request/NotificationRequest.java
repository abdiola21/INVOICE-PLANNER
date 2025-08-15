package com.invoice.planner.dto.request;

import java.util.UUID;
import java.util.UUID;
import java.util.List;
import java.time.LocalDate;
import com.invoice.planner.entity.User;
import com.invoice.planner.enums.TypeNotification;

import java.time.LocalDateTime;

/**
 * DTO pour recevoir les données du frontend pour l'entité Notification.
 * Contient uniquement les champs modifiables par l'utilisateur.
 */
public class NotificationRequest {
    
    private String message;
    private LocalDate date;
    private boolean estLu;
    private TypeNotification type;
    private User destinataire;
    
    // Constructeur par défaut
    public NotificationRequest() {
    }
    
    // Getters et setters
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
} 