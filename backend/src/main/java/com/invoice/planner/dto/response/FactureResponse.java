package com.invoice.planner.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import com.invoice.planner.entity.Client;
import com.invoice.planner.entity.Devis;
import com.invoice.planner.enums.EtatFacture;

/**
 * DTO pour retourner les données de l'entité Facture au frontend.
 * Contient uniquement les données nécessaires pour la présentation,
 * et non toute la structure de l'entité d'origine pour des raisons de sécurité.
 */
public class FactureResponse {
    // Identifiant public exposé au frontend (jamais l'ID interne de la base de données)
    private UUID trackingId;
    
    private String numero;
    private Client client;
    private LocalDate dateEcheance;
    private Double montantHT;
    private Double montantTTC;
    private Double remise;
    private EtatFacture etat;
    private String modeReglement;
    private String referenceDevis;
    
    // Attributs relationnels avec seulement les champs nécessaires
    // Relation ManyToOne - retourne uniquement les champs sélectionnés
    private UUID clientTrackingId;
    private String clientName; // Champ personnalisé sélectionné par l'utilisateur
    
    // Métadonnées
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Devis> devis;

    
    // Constructeur par défaut
    public FactureResponse() {
    }
    
    // Getters et setters
    public UUID getTrackingId() {
        return trackingId;
    }
    
    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    public LocalDate getDateEcheance() {
        return dateEcheance;
    }
    
    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }
    public Double getMontantHT() {
        return montantHT;
    }
    
    public void setMontantHT(Double montantHT) {
        this.montantHT = montantHT;
    }
    public Double getMontantTTC() {
        return montantTTC;
    }
    
    public void setMontantTTC(Double montantTTC) {
        this.montantTTC = montantTTC;
    }
    public Double getRemise() {
        return remise;
    }
    
    public void setRemise(Double remise) {
        this.remise = remise;
    }
    public EtatFacture getEtat() {
        return etat;
    }
    
    public void setEtat(EtatFacture etat) {
        this.etat = etat;
    }
    public String getModereglement() {
        return modeReglement;
    }
    
    public void setModereglement(String modeReglement) {
        this.modeReglement = modeReglement;
    }
    public String getReferencedevis() {
        return referenceDevis;
    }
    
    public void setReferencedevis(String referenceDevis) {
        this.referenceDevis = referenceDevis;
    }
    
    public UUID getClientTrackingId() {
        return clientTrackingId;
    }
    
    public void setClientTrackingId(UUID clientTrackingId) {
        this.clientTrackingId = clientTrackingId;
    }
    
    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public List<Devis> getDevis() {
        return devis;
    }

    public void setDevis(List<Devis> devis) {
        this.devis = devis;
    }
}