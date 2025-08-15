package com.invoice.planner.entity;

import java.io.Serializable;
import java.util.UUID;

import com.invoice.planner.utils.AuditTable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entité représentant un client
 */
@Entity
@Table(name = "clients")
public class Client extends AuditTable implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private UUID trackingId;
    
    private String nom;
    
    private String prenom;
    
    private String adresse;
    
    private String email;
    
    private String telephone;
    
    @Column(name = "societe", nullable = true)
    private String societe;
    
    @Column(name = "numero_tva", nullable = true)
    private String numeroTVA;

    private String ville;

    private String pays;
    
    
    
    // Constructeurs
    public Client() {
    }
    
    public Client(String nom, String adresse, String email, String telephone, String societe, String numeroTVA, String ville, String pays) {
        this.nom = nom;
        this.adresse = adresse;
        this.email = email;
        this.telephone = telephone;
        this.societe = societe;
        this.numeroTVA = numeroTVA;
        this.ville = ville;
        this.pays = pays;
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
    
    // Méthodes métier
    public boolean ajouterClient() {
        return true;
    }
    
    public boolean modifierClient() {
        return true;
    }
    
    public boolean supprimerClient() {
        return true;
    }
} 