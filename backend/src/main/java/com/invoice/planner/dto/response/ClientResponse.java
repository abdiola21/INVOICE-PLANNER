package com.invoice.planner.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

/**
 * DTO pour retourner les données de l'entité Client au frontend.
 * Contient uniquement les données nécessaires pour la présentation,
 * et non toute la structure de l'entité d'origine pour des raisons de sécurité.
 */
public class ClientResponse {
    // Identifiant public exposé au frontend (jamais l'ID interne de la base de données)
    private UUID trackingId;
    
    private String nom;
    private String prenom;
    private String adresse;
    private String email;
    private String telephone;
    private String societe;
    private String numeroTVA;
    private String ville;
    private String pays;
    
    
    // Métadonnées
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructeur par défaut
    public ClientResponse() {
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
    
    public String getPrenom() {
    	return prenom;
    }
    
    public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getSociete() {
        return societe;
    }
    
    public void setSociete(String societe) {
        this.societe = societe;
    }
    
    
    public String getNumeroTVA() {
        return numeroTVA;
    }

    public void setNumeroTVA(String numeroTVA) {
        this.numeroTVA = numeroTVA;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
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