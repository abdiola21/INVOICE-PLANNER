package com.invoice.planner.dto.request;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

/**
 * DTO pour recevoir les données du frontend pour l'entité Client.
 * Contient uniquement les champs modifiables par l'utilisateur.
 */
public class ClientRequest {
    
    private String nom;
    private String prenom;
    private String adresse;
    private String email;
    private String telephone;
    private String societe;
    private String numeroTVA;
    private String ville;
    private String pays;
    
    // Constructeur par défaut
    public ClientRequest() {
    }
    
    // Getters et setters
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
} 