package com.invoice.planner.service;

import com.invoice.planner.dto.request.CompanyProfileRequest;
import com.invoice.planner.dto.response.CompanyProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface CompanyProfileService {
    
    /**
     * Crée ou met à jour le profil d'entreprise d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @param request Données du profil à créer ou mettre à jour
     * @return Le profil créé ou mis à jour
     */
    CompanyProfileResponse createOrUpdateProfile(Long userId, CompanyProfileRequest request);
    
    /**
     * Récupère le profil d'entreprise d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @return Le profil d'entreprise de l'utilisateur
     */
    CompanyProfileResponse getProfileByUserId(Long userId);
    
    /**
     * Vérifie si le profil d'entreprise d'un utilisateur est complet
     * 
     * @param userId ID de l'utilisateur
     * @return true si le profil est complet, false sinon
     */
    boolean isProfileCompleted(Long userId);
    
    /**
     * Télécharge un logo pour le profil d'entreprise d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @param file Fichier du logo
     * @return Le profil d'entreprise mis à jour
     * @throws IOException Si une erreur survient lors de la manipulation du fichier
     */
    CompanyProfileResponse uploadLogo(Long userId, MultipartFile file) throws IOException;
    
    /**
     * Récupère le logo d'un profil d'entreprise
     * 
     * @param profileId ID de tracking du profil
     * @return Le contenu du fichier logo
     * @throws IOException Si une erreur survient lors de la lecture du fichier
     */
    byte[] getLogo(UUID profileId) throws IOException;
} 