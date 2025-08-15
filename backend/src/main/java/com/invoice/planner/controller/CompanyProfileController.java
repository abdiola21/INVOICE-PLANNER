package com.invoice.planner.controller;

import com.invoice.planner.dto.request.CompanyProfileRequest;
import com.invoice.planner.dto.response.CompanyProfileResponse;
import com.invoice.planner.entity.User;
import com.invoice.planner.service.CompanyProfileService;
import com.invoice.planner.utils.GlobalResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.invoice.planner.security.UserDetailsImpl;
import com.invoice.planner.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/profile")
@Tag(name = "Profil d'entreprise", description = "API pour la gestion des profils d'entreprise")
@SecurityRequirement(name = "bearerAuth")
public class CompanyProfileController {

    private CompanyProfileService companyProfileService;
    
    private UserRepository userRepository;

    public CompanyProfileController(CompanyProfileService companyProfileService, UserRepository userRepository) {
        this.companyProfileService = companyProfileService;
        this.userRepository = userRepository;
    } 
    // Méthode utilitaire pour récupérer l'utilisateur à partir du contexte de sécurité
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userRepository.findById(userDetails.getId()).orElse(null);
        }
        return null;
    }

    @PostMapping
    @Operation(summary = "Créer ou mettre à jour un profil d'entreprise")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Profil créé ou mis à jour avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyProfileResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Requête invalide",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
        )
    })
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> createOrUpdateProfile(
            @Valid @RequestBody CompanyProfileRequest request) {
        try {
            // Récupérer l'utilisateur à partir du contexte de sécurité
            User currentUser = getCurrentUser();
            
            // Vérifier si l'utilisateur est authentifié
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new GlobalResponse<>(false, "Utilisateur non authentifié", null));
            }
            
            CompanyProfileResponse response = companyProfileService.createOrUpdateProfile(currentUser.getId(), request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log l'exception pour le débogage
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalResponse<>(false, "Erreur lors de la création/mise à jour du profil: " + e.getMessage(), null));
        }
    }

    @GetMapping
    @Operation(summary = "Récupérer le profil d'entreprise de l'utilisateur connecté")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Profil récupéré avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyProfileResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Profil non trouvé",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
        )
    })
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> getProfile() {
        try {
            // Récupérer l'utilisateur à partir du contexte de sécurité
            User currentUser = getCurrentUser();
            
            // Vérifier si l'utilisateur est authentifié
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new GlobalResponse<>(false, "Utilisateur non authentifié", null));
            }
            
            CompanyProfileResponse response = companyProfileService.getProfileByUserId(currentUser.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log l'exception pour le débogage
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalResponse<>(false, "Erreur lors de la récupération du profil: " + e.getMessage(), null));
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Vérifier si le profil d'entreprise est complet")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Statut du profil récupéré avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
        )
    })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GlobalResponse> isProfileComplete() {
        try {
            // Récupérer l'utilisateur à partir du contexte de sécurité
            User currentUser = getCurrentUser();
            
            // Vérifier si l'utilisateur est authentifié
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new GlobalResponse<>(false, "Utilisateur non authentifié", null));
            }
            
            boolean isComplete = companyProfileService.isProfileCompleted(currentUser.getId());
            return ResponseEntity.ok(new GlobalResponse(true, "Statut du profil récupéré avec succès", isComplete));
        } catch (Exception e) {
            // Log l'exception pour le débogage
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalResponse<>(false, "Erreur lors de la récupération du statut du profil: " + e.getMessage(), null));
        }
    }

    @PostMapping("/logo")
    @Operation(summary = "Télécharger un logo pour le profil d'entreprise")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Logo téléchargé avec succès",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompanyProfileResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", 
            description = "Fichier invalide",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", 
            description = "Profil non trouvé",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))
        )
    })
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> uploadLogo(
            @RequestParam("file") MultipartFile file) {
        try {
            // Récupérer l'utilisateur à partir du contexte de sécurité
            User currentUser = getCurrentUser();
            
            // Vérifier si l'utilisateur est authentifié
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new GlobalResponse<>(false, "Utilisateur non authentifié", null));
            }
            
            CompanyProfileResponse response = companyProfileService.uploadLogo(currentUser.getId(), file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log l'exception pour le débogage
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalResponse<>(false, "Erreur lors du téléchargement du logo: " + e.getMessage(), null));
        }
    }

    @CrossOrigin(origins = "http://localhost:4200") // URL de ton Angular
    @GetMapping("/logo/{profileId}")
    @Operation(summary = "Récupérer le logo d'un profil d'entreprise")
    public ResponseEntity<byte[]> getLogo(@PathVariable UUID profileId) throws IOException {
        byte[] logoData = companyProfileService.getLogo(profileId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // ou IMAGE_PNG selon ce que tu utilises
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        headers.setPragma("no-cache");
        headers.setExpires(0);
        return new ResponseEntity<>(logoData, headers, HttpStatus.OK);
    }

} 