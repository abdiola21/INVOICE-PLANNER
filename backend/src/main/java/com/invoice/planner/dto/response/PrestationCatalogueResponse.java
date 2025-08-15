package com.invoice.planner.dto.response;

/**
 * DTO pour retourner les prestations du catalogue au frontend
 * Version simplifiée de PrestationResponse pour l'affichage dans les listes de sélection
 */
public class PrestationCatalogueResponse {
    
    private Long id;
    private String description;
    private Double prixUnitaire;
    private String unite;
    
    // Constructeur par défaut
    public PrestationCatalogueResponse() {
    }
    
    // Getters et setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Double getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
    
    public String getUnite() {
        return unite;
    }
    
    public void setUnite(String unite) {
        this.unite = unite;
    }
} 