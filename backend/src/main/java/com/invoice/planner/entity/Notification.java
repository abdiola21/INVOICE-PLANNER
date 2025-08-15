package com.invoice.planner.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.invoice.planner.enums.TypeNotification;
import com.invoice.planner.utils.AuditTable;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entité représentant une notification
 */
@Entity
@Table(name = "notifications")
public class Notification extends AuditTable implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private UUID trackingId = UUID.randomUUID();
    
    private String message;
    
    @Temporal(TemporalType.DATE)
    private LocalDate date;
    
    private boolean estLu;
    
    @Enumerated(EnumType.STRING)
    private TypeNotification type;
    
    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private User destinataire;
    
    // Constructeurs
    public Notification() {
    }
    
    public Notification(String message, LocalDate date, boolean estLu, TypeNotification type, User destinataire) {
        this.message = message;
        this.date = date;
        this.estLu = estLu;
        this.type = type;
        this.destinataire = destinataire;
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

    public boolean isEstLu() {
        return estLu;
    }

    public void setEstLu(boolean estLu) {
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
    
    // Méthodes métier
    public void marquerCommeLu() {
        this.estLu = true;
    }
    
    public static void envoyerNotification(String message, TypeNotification type, User destinataire) {
        Notification notification = new Notification(message, LocalDate.now(), false, type, destinataire);
        // Logique pour sauvegarder la notification
    }
} 