package com.invoice.planner.entity;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.invoice.planner.enums.EtatFacture;
import com.invoice.planner.utils.AuditTable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entité représentant une facture
 */
@Entity
@Table(name = "factures")
public class Facture extends AuditTable implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private UUID trackingId = UUID.randomUUID();
    
    private String numero;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    
    // @ManyToOne
    // @JoinColumn(name = "createur_id")
    // private User createur;
    
    // @Temporal(TemporalType.DATE)
    // private Date dateCreation;
    
    @Temporal(TemporalType.DATE)
    private LocalDate dateEcheance;
    
    private Double montantHT;
    
    private Double montantTTC;
    
    private Double remise;
    
    @Enumerated(EnumType.STRING)
    private EtatFacture etat;
    
    private String modeReglement;
    
    private String referenceDevis;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Devis> devis = new ArrayList<>();
    
    //@OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //private List<DocumentLigne> lignes = new ArrayList<>();
    
    //@OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //private List<Taxe> taxes = new ArrayList<>();
    
    // Constructeurs
    public Facture() {
    }
    
    public Facture(String numero, Client client, LocalDate dateEcheance, Double remise, EtatFacture etat, String modeReglement, String referenceDevis) {
        this.numero = numero;
        this.client = client;
        // this.createur = createur;
        // this.dateCreation = dateCreation;
        this.dateEcheance = dateEcheance;
        this.remise = remise;
        this.etat = etat;
        this.modeReglement = modeReglement;
        this.referenceDevis = referenceDevis;
        this.montantHT = (Double) 0.0;
        this.montantTTC = (Double) 0.0;
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

    // public User getCreateur() {
    //     return createur;
    // }

    // public void setCreateur(User createur) {
    //     this.createur = createur;
    // }

    // public Date getDateCreation() {
    //     return dateCreation;
    // }

    // public void setDateCreation(Date dateCreation) {
    //     this.dateCreation = dateCreation;
    // }

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

    public String getModeReglement() {
        return modeReglement;
    }

    public void setModeReglement(String modeReglement) {
        this.modeReglement = modeReglement;
    }

    public String getReferenceDevis() {
        return referenceDevis;
    }

    public void setReferenceDevis(String referenceDevis) {
        this.referenceDevis = referenceDevis;
    }

    public List<Devis> getDevis() {
        return devis;
    }

    public void setDevis(List<Devis> devis) {
        this.devis = devis;
    }

    //    public List<DocumentLigne> getLignes() {
//        return lignes;
//    }
//
//    public void setLignes(List<DocumentLigne> lignes) {
//        this.lignes = lignes;
//    }
//
//    public List<Taxe> getTaxes() {
//        return taxes;
//    }
//
//    public void setTaxes(List<Taxe> taxes) {
//        this.taxes = taxes;
//    }
//
    // Méthodes métier
    public double calculerMontantHT() {
        double total = 0.0;
//        for (DocumentLigne ligne : lignes) {
//            total += ligne.calculerMontant();
//        }
        
        if (remise != null && remise > 0) {
            total = total * (1 - remise / 100);
        }
        
        this.montantHT = (Double) total;
        return total;
    }
    
    public double calculerMontantTTC() {
        double ht = calculerMontantHT();
        double ttc = ht;
        
//        for (Taxe taxe : taxes) {
//            ttc += ht * (taxe.getTaux() / 100);
//        }
        
        this.montantTTC = (Double) ttc;
        return ttc;
    }
    
    public File genererPDF() {
        // Implémentation de la génération du PDF
        return null;
    }
    
    public boolean envoyerParEmail() {
        // Implémentation de l'envoi par email
        return false;
    }

    public String generateFactureNumber() {
        LocalDateTime now = LocalDateTime.now();

        int year = now.getYear();
        int monthValue = now.getMonthValue();
        String month = (monthValue < 10 ? "0" : "") + monthValue;

        long milliseconds = System.currentTimeMillis();
        long random = milliseconds % 1000;

        String numeroDevis = "FAC-" + year + month + "-" + random;
        return numeroDevis;
    }


    public void addDevis(Devis devis){

        long difference = ChronoUnit.DAYS.between(devis.getDateEmission(), devis.getDateEcheance());
        int diff = (int) difference;



        this.client = devis.getClient();
        this.etat = EtatFacture.BROUILLON;
        this.dateEcheance= LocalDate.now().plusDays(diff);
        this.referenceDevis = referenceDevis;
        this.modeReglement = "ESPECE";
        this.montantHT=devis.getPrixTotal();
        this.montantTTC = devis.getPrixTTC();
        this.numero = generateFactureNumber();
        this.trackingId = UUID.randomUUID();
        this.getDevis().add(devis);



    }
} 