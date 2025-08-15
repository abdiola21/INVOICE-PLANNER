package com.invoice.planner.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CompanyProfileRequest {
    
    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    @Size(max = 100, message = "Le nom de l'entreprise ne doit pas dépasser 100 caractères")
    private String companyName;
    
    @Size(max = 200, message = "L'adresse ne doit pas dépasser 200 caractères")
    private String address;
    
    @Size(max = 100, message = "La ville ne doit pas dépasser 100 caractères")
    private String city;
    
    @Size(max = 20, message = "Le code postal ne doit pas dépasser 20 caractères")
    private String postalCode;
    
    @Size(max = 100, message = "Le pays ne doit pas dépasser 100 caractères")
    private String country;
    
    @Size(max = 20, message = "Le numéro de téléphone ne doit pas dépasser 20 caractères")
    private String phoneNumber;
    
    @Email(message = "Veuillez fournir une adresse email valide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String email;
    
    @Size(max = 200, message = "Le site web ne doit pas dépasser 200 caractères")
    private String website;
    
    @Size(max = 20, message = "Le numéro d'identification fiscale ne doit pas dépasser 20 caractères")
    private String taxNumber;
    
    @Size(max = 20, message = "Le numéro d'enregistrement ne doit pas dépasser 20 caractères")
    private String registrationNumber;
    
    // Getters et Setters
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getTaxNumber() {
        return taxNumber;
    }
    
    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }
    
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
} 