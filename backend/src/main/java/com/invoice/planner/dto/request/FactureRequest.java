package com.invoice.planner.dto.request;

import java.util.ArrayList;
import java.util.UUID;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import com.invoice.planner.entity.Client;
import com.invoice.planner.entity.Devis;
import com.invoice.planner.enums.EtatFacture;

/**
 * DTO pour recevoir les données du frontend pour l'entité Facture.
 * Contient uniquement les champs modifiables par l'utilisateur.
 */
public class FactureRequest {
    
    private String numero;
    private Client client;
    private LocalDate dateEcheance;
    private Double montantHT;
    private Double montantTTC;
    private Double remise;
    private EtatFacture etat;
    private String modeReglement;
    private String referenceDevis;

    private List<Devis> devis = new ArrayList<>();
    
    // Constructeur par défaut
    public FactureRequest() {
    }
    
    // Getters et setters
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
    public Double getMontantht() {
        return montantHT;
    }
    
    public void setMontantht(Double montantHT) {
        this.montantHT = montantHT;
    }
    public Double getMontantttc() {
        return montantTTC;
    }
    
    public void setMontantttc(Double montantTTC) {
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

    public List<Devis> getDevis() {
        return devis;
    }

    public void setDevis(List<Devis> devis) {
        this.devis = devis;
    }
}