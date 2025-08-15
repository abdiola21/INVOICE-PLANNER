package com.invoice.planner.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class QuoteRequest {
    
    @NotBlank(message = "La référence du devis est obligatoire")
    private String reference;
    
    @NotNull(message = "La date d'émission est obligatoire")
    private LocalDate issueDate;
    
    @NotNull(message = "La date d'expiration est obligatoire")
    private LocalDate expiryDate;
    
    @NotNull(message = "Les informations du client sont obligatoires")
    @Valid
    private ClientInfo client;
    
    @NotEmpty(message = "Au moins une ligne de devis est obligatoire")
    @Valid
    private List<QuoteItem> items;
    
    private String notes;
    
    @NotNull(message = "Le taux de TVA est obligatoire")
    @Positive(message = "Le taux de TVA doit être positif")
    private Double taxRate = 20.0; // 20% par défaut
    
    private Boolean displayTax = true;
    
    private String paymentTerms;
    
    private String legalNotice;
    
    public static class ClientInfo {
        @NotBlank(message = "Le nom du client est obligatoire")
        private String name;
        
        @NotBlank(message = "L'adresse du client est obligatoire")
        private String address;
        
        private String city;
        
        private String postalCode;
        
        private String country;
        
        private String email;
        
        private String phoneNumber;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getAddress() {
            return address;
        }
        
        public void setAddress(String address) {
            this.address = address;
        }
        
        public String getCity() {
            return city;
        }
        
        public void setCity(String city) {
            this.city = city;
        }
        
        public String getPostalCode() {
            return postalCode;
        }
        
        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }
        
        public String getCountry() {
            return country;
        }
        
        public void setCountry(String country) {
            this.country = country;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPhoneNumber() {
            return phoneNumber;
        }
        
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
    
    public static class QuoteItem {
        @NotBlank(message = "La description de l'article est obligatoire")
        private String description;
        
        @NotNull(message = "La quantité est obligatoire")
        @Positive(message = "La quantité doit être positive")
        private Integer quantity;
        
        @NotNull(message = "Le prix unitaire est obligatoire")
        @Positive(message = "Le prix unitaire doit être positif")
        private Double unitPrice;
        
        private String unit = "unité";
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        public Integer getQuantity() {
            return quantity;
        }
        
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
        
        public Double getUnitPrice() {
            return unitPrice;
        }
        
        public void setUnitPrice(Double unitPrice) {
            this.unitPrice = unitPrice;
        }
        
        public String getUnit() {
            return unit;
        }
        
        public void setUnit(String unit) {
            this.unit = unit;
        }
        
        // Méthode de commodité pour calculer le montant total
        public Double getTotal() {
            return quantity * unitPrice;
        }
    }
    
    // Getters et Setters
    public String getReference() {
        return reference;
    }
    
    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public LocalDate getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public ClientInfo getClient() {
        return client;
    }
    
    public void setClient(ClientInfo client) {
        this.client = client;
    }
    
    public List<QuoteItem> getItems() {
        return items;
    }
    
    public void setItems(List<QuoteItem> items) {
        this.items = items;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Double getTaxRate() {
        return taxRate;
    }
    
    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }
    
    public Boolean getDisplayTax() {
        return displayTax;
    }
    
    public void setDisplayTax(Boolean displayTax) {
        this.displayTax = displayTax;
    }
    
    public String getPaymentTerms() {
        return paymentTerms;
    }
    
    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }
    
    public String getLegalNotice() {
        return legalNotice;
    }
    
    public void setLegalNotice(String legalNotice) {
        this.legalNotice = legalNotice;
    }
    
    // Méthodes utilitaires pour les calculs
    public Double getSubtotal() {
        return items.stream().mapToDouble(QuoteItem::getTotal).sum();
    }
    
    public Double getTaxAmount() {
        return displayTax ? getSubtotal() * (taxRate / 100) : 0.0;
    }
    
    public Double getTotal() {
        return getSubtotal() + getTaxAmount();
    }
} 