package com.invoice.planner.entity;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.invoice.planner.enums.EtatDevis;
import com.invoice.planner.utils.AuditTable;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.Image;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.Chunk;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entité représentant un devis
 */
@Entity
@Table(name = "devis")
public class Devis extends AuditTable implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private UUID trackingId;
    
    @Column(name = "reference", unique = true, nullable = false)
    private String reference;
    
    @Column(name = "nom_projet")
    private String nomProjet;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "createur_id")
    private User createur;
    
    @Column(name = "date_emission", nullable = false)
    private LocalDate dateEmission;
    
    @Column(name = "date_echeance")
    private LocalDate dateEcheance;
    
    @Column(name = "prix_total", nullable = false)
    private Double prixTotal = 0.0;
    
    @Column(name = "prix_ttc", nullable = true)
    private Double prixTTC = 0.0;
    
    @Column(name = "tva")
    private Double tva = 20.0; // Taux de TVA par défaut à 20%
    
    @Column(name = "remise")
    private Double remise = 0.0;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private EtatDevis statut;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<Prestation> prestations = new ArrayList<>();
    
    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DocumentLigne> lignes = new ArrayList<>();
    
    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Taxe> taxes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "facture_id")
    @JsonBackReference
    private Facture facture;
    
    // @PrePersist
    // public void prePersist() {
    //     if (this.trackingId == null) {
    //         this.trackingId = UUID.randomUUID();
    //     }
    //     if (this.dateEmission == null) {
    //         this.dateEmission = LocalDate.now();
    //     }
    // }
    
    // Constructeurs
    public Devis() {
    }
    
    public Devis(String reference, String nomProjet, Client client, User createur, 
                LocalDate dateEcheance, Double remise, EtatDevis statut, String description, LocalDate dateEmission) {
        this.reference = reference;
        this.nomProjet = nomProjet;
        this.client = client;
        this.createur = createur;
        this.dateEmission = dateEmission; //LocalDate.now();
        this.dateEcheance = dateEcheance;
        this.remise = remise;
        this.statut = statut;
        this.description = description;
        this.prixTotal = 0.0;
        this.prixTTC = 0.0;
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

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    // Méthodes utilitaires pour la gestion des prestations
    public void addPrestation(Prestation prestation) {
        prestations.add(prestation);
        prestation.setDevis(this);
    }
    
    public void removePrestation(Prestation prestation) {
        prestations.remove(prestation);
        prestation.setDevis(null);
    }
    
    // Méthodes métier
    public double calculerPrixTotal() {
        double total = 0.0;
        
        // Calcul basé sur les prestations
        for (Prestation prestation : prestations) {
            total += prestation.calculerMontant();
        }
        
        // Calcul basé sur les lignes de document (pour rétrocompatibilité)
        if (total == 0.0 && !lignes.isEmpty()) {
            for (DocumentLigne ligne : lignes) {
                total += ligne.calculerMontant();
            }
        }
        
        if (remise != null && remise > 0) {
            total = total * (1 - remise / 100);
        }
        
        this.prixTotal = total;
        return total;
    }
    
    public double calculerPrixTTC() {
        double ht = calculerPrixTotal();
        double ttc = ht;
        
        // Si un taux de TVA global est défini
        if (tva != null && tva > 0) {
            ttc += ht * (tva / 100);
        } else {
            // Sinon, utiliser les taxes individuelles
            for (Taxe taxe : taxes) {
                ttc += ht * (taxe.getTaux() / 100);
            }
        }
        
        this.prixTTC = ttc;
        return ttc;
    }
    
    public Facture genererFacture() {
        // Implémentation de la création d'une facture à partir d'un devis
        Facture facture = new Facture();
        facture.setClient(this.client);
        facture.setReferenceDevis(this.reference);
        facture.setRemise(this.remise);
        
        // Copier les lignes du devis vers la facture
//        for (DocumentLigne ligne : this.lignes) {
//            DocumentLigne nouvelleLigne = new DocumentLigne();
//            nouvelleLigne.setDesignation(ligne.getDesignation());
//            nouvelleLigne.setQuantite(ligne.getQuantite());
//            nouvelleLigne.setPrixUnitaire(ligne.getPrixUnitaire());
//            nouvelleLigne.setFacture(facture);
//            facture.getLignes().add(nouvelleLigne);
//        }
        
        // Copier les taxes
//        for (Taxe taxe : this.taxes) {
//            Taxe nouvelleTaxe = new Taxe();
//            nouvelleTaxe.setNom(taxe.getNom());
//            nouvelleTaxe.setTaux(taxe.getTaux());
//            nouvelleTaxe.setFacture(facture);
//            facture.getTaxes().add(nouvelleTaxe);
//        }
        
        facture.calculerMontantHT();
        facture.calculerMontantTTC();
        
        return facture;
    }
    
    /**
     * Génère un PDF enrichi du devis avec logo et infos société
     * @param logoPath chemin du logo (peut être null)
     * @param companyName nom de la société
     * @param companyAddress adresse
     * @param companyCity ville
     * @param companyPostalCode code postal
     * @param companyCountry pays
     * @param companyPhone téléphone
     * @param companyEmail email
     * @param companyWebsite site web
     */
    public File genererPDF(String logoPath, String companyName, String companyAddress, String companyCity, String companyPostalCode, String companyCountry, String companyPhone, String companyEmail, String companyWebsite) {
        String fileName = "devis_" + this.getReference() + ".pdf";
        File pdfFile = new File(System.getProperty("java.io.tmpdir"), fileName);
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            
            // Définition des polices
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new java.awt.Color(44, 62, 80));
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new java.awt.Color(44, 62, 80));
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font highlightFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new java.awt.Color(52, 152, 219));

            // En-tête avec logo et informations de l'entreprise
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 2});
            headerTable.setSpacingAfter(20);

            // Cellule pour le logo
            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(0);
            if (logoPath != null && !logoPath.isEmpty()) {
                File logoFile = new File(logoPath);
                if (logoFile.exists()) {
                    try {
                        Image logo = Image.getInstance(logoPath);
                        logo.scaleToFit(120, 60);
                        logo.setAlignment(Element.ALIGN_LEFT);
                        logoCell.addElement(logo);
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement du logo: " + e.getMessage());
                    }
                } else {
                    System.err.println("Le fichier logo n'existe pas: " + logoPath);
                }
            }
            headerTable.addCell(logoCell);

            // Cellule pour les informations de l'entreprise
            PdfPCell companyCell = new PdfPCell();
            companyCell.setBorder(0);
            companyCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            
            Paragraph companyNamePara = new Paragraph(companyName != null ? companyName : "", headerFont);
            companyNamePara.setAlignment(Element.ALIGN_RIGHT);
            companyCell.addElement(companyNamePara);
            
            Paragraph addressPara = new Paragraph();
            addressPara.setAlignment(Element.ALIGN_RIGHT);
            if (companyAddress != null && !companyAddress.isEmpty()) {
                addressPara.add(new Chunk(companyAddress + "\n", normalFont));
            }
            if (companyPostalCode != null || companyCity != null) {
                addressPara.add(new Chunk((companyPostalCode != null ? companyPostalCode : "") + " " + (companyCity != null ? companyCity : "") + "\n", normalFont));
            }
            if (companyCountry != null && !companyCountry.isEmpty()) {
                addressPara.add(new Chunk(companyCountry + "\n", normalFont));
            }
            companyCell.addElement(addressPara);
            
            Paragraph contactPara = new Paragraph();
            contactPara.setAlignment(Element.ALIGN_RIGHT);
            if (companyPhone != null && !companyPhone.isEmpty()) {
                contactPara.add(new Chunk("Tél: " + companyPhone + "\n", normalFont));
            }
            if (companyEmail != null && !companyEmail.isEmpty()) {
                contactPara.add(new Chunk("Email: " + companyEmail + "\n", normalFont));
            }
            if (companyWebsite != null && !companyWebsite.isEmpty()) {
                contactPara.add(new Chunk("Web: " + companyWebsite, normalFont));
            }
            companyCell.addElement(contactPara);
            
            headerTable.addCell(companyCell);
            document.add(headerTable);

            // Titre du document
            Paragraph title = new Paragraph("DEVIS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);
            
            // Référence et date
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);
            
            // Colonne gauche: Références du devis
            PdfPCell refCell = new PdfPCell();
            refCell.setBorder(0);
            refCell.addElement(new Paragraph("Référence: " + this.getReference(), boldFont));
            refCell.addElement(new Paragraph("Date d'émission: " + (this.getDateEmission() != null ? this.getDateEmission().toString() : ""), normalFont));
            refCell.addElement(new Paragraph("Date d'échéance: " + (this.getDateEcheance() != null ? this.getDateEcheance().toString() : ""), normalFont));
            infoTable.addCell(refCell);
            
            // Colonne droite: Informations client
            PdfPCell clientCell = new PdfPCell();
            clientCell.setBorder(0);
            clientCell.addElement(new Paragraph("CLIENT", headerFont));
            if (this.getClient() != null) {
                clientCell.addElement(new Paragraph(this.getClient().getNom(), boldFont));
                if (this.getClient().getAdresse() != null) {
                    clientCell.addElement(new Paragraph(this.getClient().getAdresse(), normalFont));
                }
                if (this.getClient().getEmail() != null) {
                    clientCell.addElement(new Paragraph("Email: " + this.getClient().getEmail(), normalFont));
                }
                if (this.getClient().getTelephone() != null) {
                    clientCell.addElement(new Paragraph("Tél: " + this.getClient().getTelephone(), normalFont));
                }
            }
            infoTable.addCell(clientCell);
            document.add(infoTable);
            
            // Informations du projet
            Paragraph projectTitle = new Paragraph("PROJET: " + this.getNomProjet(), headerFont);
            projectTitle.setSpacingAfter(5);
            document.add(projectTitle);
            
            if (this.getDescription() != null && !this.getDescription().isEmpty()) {
                Paragraph description = new Paragraph(this.getDescription(), normalFont);
                description.setSpacingAfter(15);
                document.add(description);
            }
            
            // Table des prestations
            if (this.getPrestations() != null && !this.getPrestations().isEmpty()) {
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                table.setSpacingAfter(15);
                
                // En-têtes de colonnes
                PdfPCell headerCell1 = new PdfPCell(new Phrase("Désignation", headerFont));
                headerCell1.setBackgroundColor(new java.awt.Color(240, 240, 240));
                headerCell1.setPadding(5);
                table.addCell(headerCell1);
                
                PdfPCell headerCell2 = new PdfPCell(new Phrase("Prix unitaire (Fcfa)", headerFont));
                headerCell2.setBackgroundColor(new java.awt.Color(240, 240, 240));
                headerCell2.setPadding(5);
                headerCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(headerCell2);
                
                PdfPCell headerCell3 = new PdfPCell(new Phrase("Durée", headerFont));
                headerCell3.setBackgroundColor(new java.awt.Color(240, 240, 240));
                headerCell3.setPadding(5);
                headerCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(headerCell3);
                
                PdfPCell headerCell4 = new PdfPCell(new Phrase("Total (Fcfa)", headerFont));
                headerCell4.setBackgroundColor(new java.awt.Color(240, 240, 240));
                headerCell4.setPadding(5);
                headerCell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(headerCell4);
                
                // Lignes de prestations
                for (Prestation prestation : this.getPrestations()) {
                    PdfPCell cell1 = new PdfPCell(new Phrase(prestation.getDesignation(), normalFont));
                    cell1.setPadding(5);
                    table.addCell(cell1);
                    
                    PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(prestation.getPrixUnitaire()), normalFont));
                    cell2.setPadding(5);
                    cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell2);
                    
                    PdfPCell cell3 = new PdfPCell(new Phrase(String.valueOf(prestation.getDuree()), normalFont));
                    cell3.setPadding(5);
                    cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell3);
                    
                    PdfPCell cell4 = new PdfPCell(new Phrase(String.valueOf(prestation.getPrixTotal()), normalFont));
                    cell4.setPadding(5);
                    cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell4);
                }
                
                document.add(table);
            } else {
                Paragraph noPrestations = new Paragraph("Aucune prestation détaillée.", normalFont);
                noPrestations.setSpacingAfter(15);
                document.add(noPrestations);
            }
            
            // Résumé financier
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(50);
            summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            summaryTable.setSpacingBefore(10);
            
            // Sous-total HT
            PdfPCell labelCell1 = new PdfPCell(new Phrase("Montant HT :", normalFont));
            labelCell1.setBorder(0);
            labelCell1.setPadding(5);
            summaryTable.addCell(labelCell1);
            
            PdfPCell valueCell1 = new PdfPCell(new Phrase(String.valueOf(this.getPrixTotal()) + " Fcfa", normalFont));
            valueCell1.setBorder(0);
            valueCell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            valueCell1.setPadding(5);
            summaryTable.addCell(valueCell1);
            
            // TVA
            PdfPCell labelCell2 = new PdfPCell(new Phrase("TVA (" + (this.getTva() != null ? this.getTva() : 0) + "%) :", normalFont));
            labelCell2.setBorder(0);
            labelCell2.setPadding(5);
            summaryTable.addCell(labelCell2);
            
            double tvaAmount = this.getPrixTotal() * ((this.getTva() != null ? this.getTva() : 0) / 100);
            PdfPCell valueCell2 = new PdfPCell(new Phrase(String.format("%.2f", tvaAmount) + " Fcfa", normalFont));
            valueCell2.setBorder(0);
            valueCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            valueCell2.setPadding(5);
            summaryTable.addCell(valueCell2);
            
            // Remise (si applicable)
            if (this.getRemise() != null && this.getRemise() > 0) {
                PdfPCell labelCell3 = new PdfPCell(new Phrase("Remise (" + this.getRemise() + "%) :", normalFont));
                labelCell3.setBorder(0);
                labelCell3.setPadding(5);
                summaryTable.addCell(labelCell3);
                
                double remiseAmount = this.getPrixTotal() * (this.getRemise() / 100);
                PdfPCell valueCell3 = new PdfPCell(new Phrase("-" + String.format("%.2f", remiseAmount) + " Fcfa", normalFont));
                valueCell3.setBorder(0);
                valueCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                valueCell3.setPadding(5);
                summaryTable.addCell(valueCell3);
            }
            
            // Total TTC
            PdfPCell labelCell4 = new PdfPCell(new Phrase("Total TTC :", boldFont));
            labelCell4.setBorder(0);
            labelCell4.setPadding(5);
            summaryTable.addCell(labelCell4);
            
            PdfPCell valueCell4 = new PdfPCell(new Phrase(String.valueOf(this.getPrixTTC()) + " Fcfa", boldFont));
            valueCell4.setBorder(0);
            valueCell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            valueCell4.setPadding(5);
            summaryTable.addCell(valueCell4);
            
            document.add(summaryTable);
            
            // Notes et conditions
            if (this.getNotes() != null && !this.getNotes().isEmpty()) {
                document.add(new Paragraph(" "));
                Paragraph notesTitle = new Paragraph("Notes et conditions", headerFont);
                notesTitle.setSpacingBefore(20);
                notesTitle.setSpacingAfter(5);
                document.add(notesTitle);
                
                Paragraph notesPara = new Paragraph(this.getNotes(), normalFont);
                notesPara.setSpacingAfter(15);
                document.add(notesPara);
            }
            
            // Pied de page
            Paragraph footer = new Paragraph();
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            footer.add(new Chunk("Nous vous remercions pour votre confiance.\n", normalFont));
            if (this.getStatut() == EtatDevis.BROUILLON) {
                footer.add(new Chunk("Ce devis est un brouillon et n'a pas encore été validé.\n", smallFont));
            } else if (this.getStatut() == EtatDevis.ENVOYE) {
                footer.add(new Chunk("Ce devis est valable jusqu'au " + (this.getDateEcheance() != null ? this.getDateEcheance().toString() : "") + ".\n", smallFont));
            } else if (this.getStatut() == EtatDevis.ACCEPTE) {
                footer.add(new Chunk("Ce devis a été accepté le " + LocalDate.now().toString() + ".\n", smallFont));
            }
            document.add(footer);

            document.close();
            return pdfFile;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            if (document.isOpen()) {
                document.close();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            if (document.isOpen()) {
                document.close();
            }
            return null;
        }
    }
    
    public boolean envoyerParEmail() {
        // Implémentation de l'envoi par email
        return false;
    }
} 