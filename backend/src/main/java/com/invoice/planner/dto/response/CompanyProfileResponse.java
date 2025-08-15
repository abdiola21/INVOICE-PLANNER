package com.invoice.planner.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class CompanyProfileResponse {
    
    private UUID trackingId;
    
    private String companyName;
    
    private String address;
    
    private String city;
    
    private String postalCode;
    
    private String country;
    
    private String phoneNumber;
    
    private String email;
    
    private String website;
    
    private String taxNumber;
    
    private String registrationNumber;
    
    private boolean hasLogo;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Getters et Setters
    public UUID getTrackingId() {
        return trackingId;
    }
    
    public void setTrackingId(UUID trackingId) {
        this.trackingId = trackingId;
    }
    
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
    
    public boolean isHasLogo() {
        return hasLogo;
    }
    
    public void setHasLogo(boolean hasLogo) {
        this.hasLogo = hasLogo;
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