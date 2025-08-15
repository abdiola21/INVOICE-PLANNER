package com.invoice.planner.controller;

import com.invoice.planner.dto.request.LoginRequest;
import com.invoice.planner.dto.request.RegisterRequest;
import com.invoice.planner.dto.response.AuthResponse;
import com.invoice.planner.service.AuthService;
import com.invoice.planner.utils.GlobalResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentification", description = "API pour l'authentification des utilisateurs")
public class AuthController {

    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    @Operation(summary = "Connexion", description = "Authentifie un utilisateur avec son email et mot de passe")
    public ResponseEntity<GlobalResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(GlobalResponse.success("Connexion réussie", response));
    }
    
    @PostMapping("/register")
    @Operation(summary = "Inscription", description = "Crée un nouveau compte utilisateur")
    public ResponseEntity<GlobalResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(GlobalResponse.success("Inscription réussie", response));
    }
    
    @GetMapping("/verify")
    @Operation(summary = "Vérification d'email", description = "Vérifie l'adresse email d'un utilisateur avec un token")
    public ResponseEntity<?> verifyEmail(@RequestParam String token, @Value("${app.base-url-angular}") String angularBaseUrl) {
        boolean verified = authService.verifyEmail(token);
        if (verified) {
            // Rediriger vers l'application Angular
            return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, angularBaseUrl + "/auth")
                .build();
        } else {
            return ResponseEntity.ok(GlobalResponse.error("Échec de la vérification d'email", false));
        }
    }
    
    @PostMapping("/resend-verification")
    @Operation(summary = "Renvoyer email de vérification", description = "Renvoie un email de vérification à l'adresse spécifiée")
    public ResponseEntity<GlobalResponse<Boolean>> resendVerificationEmail(@RequestParam String email) {
        boolean sent = authService.resendVerificationEmail(email);
        if (sent) {
            return ResponseEntity.ok(GlobalResponse.success("Email de vérification renvoyé avec succès", true));
        } else {
            return ResponseEntity.ok(GlobalResponse.error("Échec de l'envoi de l'email de vérification", false));
        }
    }
    
    @PostMapping("/refresh-token")
    @Operation(summary = "Rafraîchir le token", description = "Génère un nouveau token d'accès avec un refresh token")
    public ResponseEntity<GlobalResponse<AuthResponse>> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(GlobalResponse.success("Token rafraîchi avec succès", response));
    }
    
    @PostMapping("/logout")
    @Operation(summary = "Déconnexion", description = "Révoque le token d'accès actuel")
    public ResponseEntity<GlobalResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok(GlobalResponse.success("Déconnexion réussie", null));
    }
}
