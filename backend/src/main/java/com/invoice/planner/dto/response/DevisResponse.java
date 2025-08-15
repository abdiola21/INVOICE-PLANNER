package com.invoice.planner.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import com.invoice.planner.enums.EtatDevis;

/**
 * DTO pour retourner les données de l'entité Devis au frontend.
 * Contient uniquement les données nécessaires pour la présentation,
 * et non toute la structure de l'entité d'origine pour des raisons de sécurité.
 */
public class DevisResponse {
    // Identifiant public exposé au frontend (jamais l'ID interne de la base de données)
    private UUID trackingId;
    
    private String reference;
    private String nomProjet;
    private String description;
    
    // Informations client simplifiées
    private UUID clientTrackingId;
    private String clientName;
    private String clientEmail;
    
    // Informations créateur simplifiées
    private UUID createurTrackingId;
    private String createurName;
    private String createurEmail;
    
    private LocalDate dateEmission;
    private LocalDate dateEcheance;
    private Double prixTotal;
    private Double prixTTC;
    private Double tva;
    private Double remise;
    private EtatDevis statut;
    private String notes;
    
    
    // Listes simplifiées sans références circulaires
    private List<PrestationResponse> prestations;
    private List<DocumentLigneResponse> lignes;
    private List<TaxeResponse> taxes;
    
    // Métadonnées
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructeur par défaut
    public DevisResponse() {
    }
    
    // Getters et setters
    public UUID getTrackingId() {
        return trackingId;
    }
    
    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public UUID getCreateurTrackingId() {
        return createurTrackingId;
    }

    public void setCreateurTrackingId(UUID createurTrackingId) {
        this.createurTrackingId = createurTrackingId;
    }

    public String getCreateurName() {
        return createurName;
    }

    public void setCreateurName(String createurName) {
        this.createurName = createurName;
    }

    public String getCreateurEmail() {
        return createurEmail;
    }

    public void setCreateurEmail(String createurEmail) {
        this.createurEmail = createurEmail;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Double getPrixTTC() {
        return prixTTC;
    }

    public void setPrixTTC(Double prixTTC) {
        this.prixTTC = prixTTC;
    }

    public Double getTva() {
        return tva;
    }

    public void setTva(Double tva) {
        this.tva = tva;
    }

    public Double getRemise() {
        return remise;
    }
    
    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public EtatDevis getStatut() {
        return statut;
    }

    public void setStatut(EtatDevis statut) {
        this.statut = statut;
    }

    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<PrestationResponse> getPrestations() {
        return prestations;
    }

    public void setPrestations(List<PrestationResponse> prestations) {
        this.prestations = prestations;
    }

    public List<DocumentLigneResponse> getLignes() {
        return lignes;
    }

    public void setLignes(List<DocumentLigneResponse> lignes) {
        this.lignes = lignes;
    }

    public List<TaxeResponse> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<TaxeResponse> taxes) {
        this.taxes = taxes;
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