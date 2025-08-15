package com.invoice.planner.service;

import com.invoice.planner.dto.request.LoginRequest;
import com.invoice.planner.dto.request.RegisterRequest;
import com.invoice.planner.dto.response.AuthResponse;

public interface AuthService {
    
    /**
     * Authentifie un utilisateur avec ses identifiants
     * @param request les informations de connexion
     * @return les données d'authentification incluant le token JWT
     */
    AuthResponse login(LoginRequest request);
    
    /**
     * Enregistre un nouvel utilisateur
     * @param request les informations d'inscription
     * @return les données d'authentification du nouvel utilisateur
     */
    AuthResponse register(RegisterRequest request);
    
    /**
     * Vérifie l'email d'un utilisateur à l'aide d'un token de vérification
     * @param token le token de vérification
     * @return true si la vérification est réussie, false sinon
     */
    boolean verifyEmail(String token);
    
    /**
     * Renvoie un email de vérification à un utilisateur
     * @param email l'email de l'utilisateur
     * @return true si l'email a été envoyé, false sinon
     */
    boolean resendVerificationEmail(String email);
    
    /**
     * Rafraîchit un token d'authentification
     * @param refreshToken le token de rafraîchissement
     * @return les nouvelles données d'authentification
     */
    AuthResponse refreshToken(String refreshToken);
    
    /**
     * Déconnecte un utilisateur en révoquant son token
     * @param token le token à révoquer
     */
    void logout(String token);
}
