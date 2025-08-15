package com.invoice.planner.controller;

import com.invoice.planner.dto.response.CompanyProfileResponse;
import com.invoice.planner.entity.User;
import com.invoice.planner.service.CompanyProfileService;
import com.invoice.planner.service.DocumentGenerationService;
import com.invoice.planner.utils.GlobalResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.invoice.planner.security.UserDetailsImpl;
import com.invoice.planner.repository.UserRepository;
import com.invoice.planner.dto.request.InvoiceRequest;
import com.invoice.planner.dto.request.QuoteRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "API pour la génération de devis et factures")
@SecurityRequirement(name = "bearerAuth")
public class DocumentController {

    private final DocumentGenerationService documentService;
    private final CompanyProfileService profileService;
    private final UserRepository userRepository;

    public DocumentController(DocumentGenerationService documentService,
                              CompanyProfileService profileService,
                              UserRepository userRepository) {
        this.documentService = documentService;
        this.profileService = profileService;
        this.userRepository = userRepository;
    }

    // Méthode utilitaire pour récupérer l'utilisateur courant
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userRepository.findById(userDetails.getId()).orElse(null);
        }
        return null;
    }

    @PostMapping("/quote/generate")
    @Operation(summary = "Générer un devis au format PDF")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Devis généré avec succès",
            content = @Content(mediaType = "application/pdf")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Requête invalide",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
        )
    })
    public ResponseEntity<?> generateQuote(@Valid @RequestBody QuoteRequest quoteRequest) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.badRequest()
                    .body(new GlobalResponse<>(false, "Utilisateur non authentifié", null));
            }

            // Vérifier si le profil de l'entreprise est complet
            if (!profileService.isProfileCompleted(currentUser.getId())) {
                return ResponseEntity.badRequest()
                    .body(new GlobalResponse<>(false, "Le profil de l'entreprise doit être complété avant de générer un devis", null));
            }

            CompanyProfileResponse companyProfile = profileService.getProfileByUserId(currentUser.getId());
            
            // Générer le PDF
            byte[] pdfBytes = documentService.generateQuotePdf(quoteRequest, companyProfile);
            
            // Préparer la réponse HTTP avec le PDF
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "devis-" + quoteRequest.getReference() + ".pdf");
            
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfBytes));
            
            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(resource);
                
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(new GlobalResponse<>(false, "Erreur lors de la génération du devis: " + e.getMessage(), null));
        }
    }

    @PostMapping("/invoice/generate")
    @Operation(summary = "Générer une facture au format PDF")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Facture générée avec succès",
            content = @Content(mediaType = "application/pdf")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Requête invalide",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
        )
    })
    public ResponseEntity<?> generateInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.badRequest()
                    .body(new GlobalResponse<>(false, "Utilisateur non authentifié", null));
            }

            // Vérifier si le profil de l'entreprise est complet
            if (!profileService.isProfileCompleted(currentUser.getId())) {
                return ResponseEntity.badRequest()
                    .body(new GlobalResponse<>(false, "Le profil de l'entreprise doit être complété avant de générer une facture", null));
            }

            CompanyProfileResponse companyProfile = profileService.getProfileByUserId(currentUser.getId());
            
            // Générer le PDF
            byte[] pdfBytes = documentService.generateInvoicePdf(invoiceRequest, companyProfile);
            
            // Préparer la réponse HTTP avec le PDF
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "facture-" + invoiceRequest.getReference() + ".pdf");
            
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfBytes));
            
            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(resource);
                
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(new GlobalResponse<>(false, "Erreur lors de la génération de la facture: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/quote/preview")
    @Operation(summary = "Obtenir une prévisualisation HTML d'un devis")
    public ResponseEntity<?> previewQuote(@Valid @RequestBody QuoteRequest quoteRequest) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.badRequest()
                    .body(new GlobalResponse<>(false, "Utilisateur non authentifié", null));
            }

            CompanyProfileResponse companyProfile = profileService.getProfileByUserId(currentUser.getId());
            
            // Générer le HTML
            String htmlContent = documentService.generateQuoteHtml(quoteRequest, companyProfile);
            
            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
                
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(new GlobalResponse<>(false, "Erreur lors de la prévisualisation du devis: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/invoice/preview")
    @Operation(summary = "Obtenir une prévisualisation HTML d'une facture")
    public ResponseEntity<?> previewInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest) {
        try {
            User currentUser = getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.badRequest()
                    .body(new GlobalResponse<>(false, "Utilisateur non authentifié", null));
            }

            CompanyProfileResponse companyProfile = profileService.getProfileByUserId(currentUser.getId());
            
            // Générer le HTML
            String htmlContent = documentService.generateInvoiceHtml(invoiceRequest, companyProfile);
            
            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
                
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(new GlobalResponse<>(false, "Erreur lors de la prévisualisation de la facture: " + e.getMessage(), null));
        }
    }
} 