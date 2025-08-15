package com.invoice.planner.dto.request;

import java.time.LocalDate;
import java.util.List;
import com.invoice.planner.entity.Client;
import com.invoice.planner.entity.User;
import com.invoice.planner.entity.Prestation;
import com.invoice.planner.entity.DocumentLigne;
import com.invoice.planner.entity.Taxe;
import com.invoice.planner.enums.EtatDevis;

/**
 * DTO pour recevoir les données du frontend pour l'entité Devis.
 * Contient uniquement les champs modifiables par l'utilisateur.
 */
public class DevisRequest {
    
    private String reference;
    private String nomProjet;
    private String description;
    private Client client;
    private User createur;
    private LocalDate dateEmission;
    private LocalDate dateEcheance;
    private Double prixTotal;
    private Double prixTTC;
    private Double tva;
    private Double remise;
    private EtatDevis statut;
    private String notes;
    private List<Prestation> prestations;
    private List<DocumentLigne> lignes;
    private List<Taxe> taxes;
    
    // Constructeur par défaut
    public DevisRequest() {
    }
    
    // Getters et setters
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

    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }

    public User getCreateur() {
        return createur;
    }

    public void setCreateur(User createur) {
        this.createur = createur;
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

    public List<Prestation> getPrestations() {
        return prestations;
    }

    public void setPrestations(List<Prestation> prestations) {
        this.prestations = prestations;
    }

    public List<DocumentLigne> getLignes() {
        return lignes;
    }

    public void setLignes(List<DocumentLigne> lignes) {
        this.lignes = lignes;
    }

    public List<Taxe> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Taxe> taxes) {
        this.taxes = taxes;
    }
    

}