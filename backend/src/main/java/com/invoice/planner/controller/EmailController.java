package com.invoice.planner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.invoice.planner.dto.response.CompanyProfileResponse;
import com.invoice.planner.entity.Devis;
import com.invoice.planner.entity.User;
import com.invoice.planner.exception.ResourceNotFoundException;
import com.invoice.planner.repository.DevisRepository;
import com.invoice.planner.repository.UserRepository;
import com.invoice.planner.service.CompanyProfileService;
import com.invoice.planner.service.EmailService;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final DevisRepository devisRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CompanyProfileService companyProfileService;

    @Autowired
    public EmailController(DevisRepository devisRepository, UserRepository userRepository, EmailService emailService, CompanyProfileService companyProfileService) {
        this.devisRepository = devisRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.companyProfileService = companyProfileService;
    }

    @GetMapping("/send-devis/{devisId}")
    public ResponseEntity<?> sendDevis(@PathVariable UUID devisId) {
        try {
            // Récupérer le devis
            Devis devis = devisRepository.findByTrackingId(devisId)
                .orElseThrow(() -> new ResourceNotFoundException("Devis", "trackingId", devisId));
            
            // Récupérer l'utilisateur courant
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = null;
            if (principal instanceof User) {
                userId = ((User) principal).getId();
            } else if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                User user = userRepository.findByEmail(username).orElse(null);
                if (user != null) {
                    userId = user.getId();
                }
            }
            
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new HashMap<String, Object>() {{
                    put("success", false);
                    put("message", "Utilisateur non authentifié");
                }});
            }
            
            // Récupérer le profil de compagnie
            CompanyProfileResponse companyProfile = userId != null ? 
                companyProfileService.getProfileByUserId(userId) : null;
            
            if (companyProfile == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HashMap<String, Object>() {{
                    put("success", false);
                    put("message", "Profil d'entreprise non trouvé. Veuillez compléter votre profil avant d'envoyer des devis.");
                }});
            }
            
            // Construire le chemin absolu du logo
            String logoPath = null;
            if (companyProfile != null && companyProfile.isHasLogo() && companyProfile.getTrackingId() != null) {
                // Construire le chemin absolu vers le répertoire uploads
                Path uploadDir = Paths.get("uploads").toAbsolutePath();
                String logoFileName = companyProfile.getTrackingId() + "_" + companyProfile.getCompanyName() + ".png";
                Path logoFilePath = uploadDir.resolve(logoFileName);
                
                // Vérifier si le fichier existe
                File logoFile = logoFilePath.toFile();
                if (logoFile.exists()) {
                    logoPath = logoFilePath.toString();
                    System.out.println("Logo trouvé: " + logoPath);
                } else {
                    System.err.println("Logo file not found: " + logoFilePath);
                    // Essayer de trouver le logo d'une autre manière
                    File uploadsDir = new File("uploads");
                    if (uploadsDir.exists() && uploadsDir.isDirectory()) {
                        File[] files = uploadsDir.listFiles((dir, name) -> name.startsWith(companyProfile.getTrackingId().toString()));
                        if (files != null && files.length > 0) {
                            logoPath = files[0].getAbsolutePath();
                            System.out.println("Logo alternatif trouvé: " + logoPath);
                        }
                    }
                }
            }
            
            // Générer le PDF
            java.io.File pdf = devis.genererPDF(
                logoPath,
                companyProfile.getCompanyName(),
                companyProfile.getAddress(),
                companyProfile.getCity(),
                companyProfile.getPostalCode(),
                companyProfile.getCountry(),
                companyProfile.getPhoneNumber(),
                companyProfile.getEmail(),
                companyProfile.getWebsite()
            );
            
            if (pdf == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<String, Object>() {{
                    put("success", false);
                    put("message", "Erreur lors de la génération du PDF du devis");
                }});
            }
            
            // Envoyer l'email
            if (devis.getClient() != null && devis.getClient().getEmail() != null) {
                String pdfPath = pdf.getAbsolutePath();
                try {
                    emailService.sendDevisWithAttachment(
                        devis.getClient().getEmail(),
                        "Votre devis " + devis.getReference() + " de " + companyProfile.getCompanyName(),
                        "Bonjour,\n\nVeuillez trouver ci-joint votre devis " + devis.getReference() + " pour le projet " + devis.getNomProjet() + ".\n\nCordialement,\nL'équipe " + companyProfile.getCompanyName(),
                        pdfPath
                    );
                    return ResponseEntity.ok(new HashMap<String, Object>() {{
                        put("success", true);
                        put("message", "Devis envoyé avec succès");
                    }});
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<String, Object>() {{
                        put("success", false);
                        put("message", "Erreur lors de l'envoi de l'email: " + e.getMessage());
                    }});
                }
            } else {
                return ResponseEntity.badRequest().body(new HashMap<String, Object>() {{
                    put("success", false);
                    put("message", "Le client n'a pas d'email associé");
                }});
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new HashMap<String, Object>() {{
                put("success", false);
                put("message", e.getMessage());
            }});
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<String, Object>() {{
                put("success", false);
                put("message", "Erreur lors de l'envoi du devis: " + e.getMessage());
                put("error", e.getClass().getName());
            }});
        }
    }
}
