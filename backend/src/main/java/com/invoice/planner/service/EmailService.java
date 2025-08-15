package com.invoice.planner.service;

import com.invoice.planner.entity.User;

public interface EmailService {
    
    /**
     * Envoie un email de vérification à l'utilisateur
     * @param user l'utilisateur destinataire
     * @param token le token de vérification
     */
    void sendVerificationEmail(User user, String token);
    
    /**
     * Envoie un email de réinitialisation de mot de passe
     * @param user l'utilisateur destinataire
     * @param token le token de réinitialisation
     */
    void sendPasswordResetEmail(User user, String token);
    
    /**
     * Envoie un email de bienvenue après la vérification du compte
     * @param user l'utilisateur vérifié
     */
    void sendWelcomeEmail(User user);
    
    /**
     * Envoie un devis au client avec le PDF en pièce jointe
     * @param toEmail l'email du client
     * @param subject le sujet de l'email
     * @param message le corps du message
     * @param pdfPath le chemin du fichier PDF à joindre
     */
    void sendDevisWithAttachment(String toEmail, String subject, String message, String pdfPath);
} 