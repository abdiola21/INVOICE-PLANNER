package com.invoice.planner.dto.request;

import java.util.UUID;
import java.util.UUID;
import java.util.List;
import java.time.LocalDate;
import com.invoice.planner.entity.User;
import java.time.LocalDateTime;

/**
 * DTO pour recevoir les données du frontend pour l'entité Historique.
 * Contient uniquement les champs modifiables par l'utilisateur.
 */
public class HistoriqueRequest {
    
    private String action;
    private LocalDate date;
    private User utilisateur;
    private String details;
    
    // Constructeur par défaut
    public HistoriqueRequest() {
    }
    
    // Getters et setters
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
} 