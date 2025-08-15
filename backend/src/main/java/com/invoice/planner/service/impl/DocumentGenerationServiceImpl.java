package com.invoice.planner.service.impl;

import com.invoice.planner.dto.request.InvoiceRequest;
import com.invoice.planner.dto.request.QuoteRequest;
import com.invoice.planner.dto.response.CompanyProfileResponse;
import com.invoice.planner.service.DocumentGenerationService;
import com.lowagie.text.DocumentException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;         

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class DocumentGenerationServiceImpl implements DocumentGenerationService {

    private final TemplateEngine templateEngine;
    
    @Value("${app.uploads.directory:uploads}")
    private String uploadDirectory;
    
    // Format de date français
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE);

    public DocumentGenerationServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public byte[] generateQuotePdf(QuoteRequest quoteRequest, CompanyProfileResponse companyProfile) throws Exception {
        String htmlContent = generateQuoteHtml(quoteRequest, companyProfile);
        return convertHtmlToPdf(htmlContent);
    }

    @Override
    public byte[] generateInvoicePdf(InvoiceRequest invoiceRequest, CompanyProfileResponse companyProfile) throws Exception {
        String htmlContent = generateInvoiceHtml(invoiceRequest, companyProfile);
        return convertHtmlToPdf(htmlContent);
    }

    @Override
    public String generateQuoteHtml(QuoteRequest quoteRequest, CompanyProfileResponse companyProfile) throws Exception {
        Context context = new Context(Locale.FRANCE);
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("quote", quoteRequest);
        variables.put("company", companyProfile);
        variables.put("formattedIssueDate", quoteRequest.getIssueDate().format(dateFormatter));
        variables.put("formattedExpiryDate", quoteRequest.getExpiryDate().format(dateFormatter));
        
        // Calculs
        variables.put("subtotal", quoteRequest.getSubtotal());
        variables.put("taxAmount", quoteRequest.getTaxAmount());
        variables.put("total", quoteRequest.getTotal());
        
        // Logo de l'entreprise - URL relative du logo
        if (companyProfile.isHasLogo()) {
            variables.put("logoUrl", "/api/profile/logo/" + companyProfile.getTrackingId());
        }
        
        context.setVariables(variables);
        
        // Utiliser le template Thymeleaf pour le devis
        return templateEngine.process("documents/quote", context);
    }

    @Override
    public String generateInvoiceHtml(InvoiceRequest invoiceRequest, CompanyProfileResponse companyProfile) throws Exception {
        Context context = new Context(Locale.FRANCE);
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("invoice", invoiceRequest);
        variables.put("company", companyProfile);
        variables.put("formattedIssueDate", invoiceRequest.getIssueDate().format(dateFormatter));
        variables.put("formattedDueDate", invoiceRequest.getDueDate().format(dateFormatter));
        
        // Calculs
        variables.put("subtotal", invoiceRequest.getSubtotal());
        variables.put("taxAmount", invoiceRequest.getTaxAmount());
        variables.put("total", invoiceRequest.getTotal());
        
        // Logo de l'entreprise
        if (companyProfile.isHasLogo()) {
            variables.put("logoUrl", "/api/profile/logo/" + companyProfile.getTrackingId());
        }
        
        // Si la facture fait référence à un devis
        if (invoiceRequest.getQuoteReference() != null && !invoiceRequest.getQuoteReference().isEmpty()) {
            variables.put("quoteReference", invoiceRequest.getQuoteReference());
        }
        
        context.setVariables(variables);
        
        // Utiliser le template Thymeleaf pour la facture
        return templateEngine.process("documents/invoice", context);
    }
    
    /**
     * Convertit le contenu HTML en document PDF
     * 
     * @param htmlContent contenu HTML à convertir
     * @return le document PDF sous forme de tableau d'octets
     * @throws DocumentException en cas d'erreur lors de la conversion
     */
    private byte[] convertHtmlToPdf(String htmlContent) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(outputStream);
        
        return outputStream.toByteArray();
    }
} 