package com.invoice.planner.service;

import com.invoice.planner.dto.request.InvoiceRequest;
import com.invoice.planner.dto.request.QuoteRequest;
import com.invoice.planner.dto.response.CompanyProfileResponse;

/**
 * Service responsable de la génération de documents (devis, factures) au format PDF et HTML
 */
public interface DocumentGenerationService {
    
    /**
     * Génère un devis au format PDF
     * 
     * @param quoteRequest données du devis
     * @param companyProfile profil de l'entreprise
     * @return le document PDF sous forme de tableau d'octets
     * @throws Exception en cas d'erreur lors de la génération
     */
    byte[] generateQuotePdf(QuoteRequest quoteRequest, CompanyProfileResponse companyProfile) throws Exception;
    
    /**
     * Génère une facture au format PDF
     * 
     * @param invoiceRequest données de la facture
     * @param companyProfile profil de l'entreprise
     * @return le document PDF sous forme de tableau d'octets
     * @throws Exception en cas d'erreur lors de la génération
     */
    byte[] generateInvoicePdf(InvoiceRequest invoiceRequest, CompanyProfileResponse companyProfile) throws Exception;
    
    /**
     * Génère le contenu HTML d'un devis pour la prévisualisation
     * 
     * @param quoteRequest données du devis
     * @param companyProfile profil de l'entreprise
     * @return le contenu HTML
     * @throws Exception en cas d'erreur lors de la génération
     */
    String generateQuoteHtml(QuoteRequest quoteRequest, CompanyProfileResponse companyProfile) throws Exception;
    
    /**
     * Génère le contenu HTML d'une facture pour la prévisualisation
     * 
     * @param invoiceRequest données de la facture
     * @param companyProfile profil de l'entreprise
     * @return le contenu HTML
     * @throws Exception en cas d'erreur lors de la génération
     */
    String generateInvoiceHtml(InvoiceRequest invoiceRequest, CompanyProfileResponse companyProfile) throws Exception;
} 