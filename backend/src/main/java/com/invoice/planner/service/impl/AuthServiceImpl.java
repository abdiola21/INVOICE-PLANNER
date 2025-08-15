package com.invoice.planner.service.impl;

import com.invoice.planner.dto.request.LoginRequest;
import com.invoice.planner.dto.request.RegisterRequest;
import com.invoice.planner.dto.response.AuthResponse;
import com.invoice.planner.entity.Token;
import com.invoice.planner.entity.User;
import com.invoice.planner.entity.UserRole;
import com.invoice.planner.entity.VerificationToken;
import com.invoice.planner.mapper.AuthMapper;
import com.invoice.planner.repository.TokenRepository;
import com.invoice.planner.repository.UserRepository;
import com.invoice.planner.repository.UserRoleRepository;
import com.invoice.planner.repository.VerificationTokenRepository;
import com.invoice.planner.service.AuthService;
import com.invoice.planner.service.EmailService;
import com.invoice.planner.utils.JwtUtil;
import com.invoice.planner.utils.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserRoleRepository userRoleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    public AuthServiceImpl(
        UserRepository userRepository,
        TokenRepository tokenRepository,
        UserRoleRepository userRoleRepository,
        VerificationTokenRepository verificationTokenRepository,
        AuthenticationManager authenticationManager,
        JwtUtil jwtUtil,
        AuthMapper authMapper,
        PasswordEncoder passwordEncoder,
        EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.userRoleRepository = userRoleRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            System.out.println("Tentative de connexion avec email: " + request.getEmail());
            
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Email ou mot de passe incorrect", HttpStatus.UNAUTHORIZED));
            
            System.out.println("Utilisateur trouvé avec ID: " + user.getId());
            
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                System.out.println("Mot de passe incorrect pour l'utilisateur: " + user.getEmail());
                throw new ApiException("Email ou mot de passe incorrect", HttpStatus.UNAUTHORIZED);
            }
            
            // Vérification si l'email est vérifié
            if (!user.isEmailVerified()) {
                System.out.println("⚠️ Email non vérifié pour l'utilisateur: " + user.getEmail() + " — mais on autorise la connexion.");
            }

            
            System.out.println("Authentification réussie pour: " + user.getEmail());
            
            // Révocation des tokens existants
            System.out.println("Révocation des tokens existants pour l'utilisateur: " + user.getEmail());
            revokeAllUserTokens(user);
            
            String accessToken = jwtUtil.generateAccessToken(user);
            System.out.println("Token d'accès généré: " + accessToken);
            
            String refreshToken = jwtUtil.generateRefreshToken(user);
            System.out.println("Token de rafraîchissement généré: " + refreshToken);
            
            saveUserToken(user, accessToken);
            System.out.println("Token d'accès sauvegardé pour l'utilisateur: " + user.getEmail());
            
            return authMapper.toAuthResponse(accessToken, refreshToken, user);
        } catch (Exception e) {
            // Log l'exception
            System.err.println("ERREUR D'AUTHENTIFICATION: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            // Relancer avec un message plus clair
            throw new ApiException("Erreur d'authentification: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        try {
            // Vérification que l'email n'est pas déjà utilisé
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ApiException("Email déjà utilisé", HttpStatus.BAD_REQUEST);
            }
            
            // Vérification que les mots de passe correspondent
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                throw new ApiException("Les mots de passe ne correspondent pas", HttpStatus.BAD_REQUEST);
            }
            
            // Création de l'utilisateur
            User user = authMapper.toEntity(request);
            
            // Par défaut, l'email n'est pas vérifié
            user.setEmailVerified(false);
            user.setProfileCompleted(false);
            
            // Chercher le rôle USER ou le créer s'il n'existe pas
            UserRole userRole = userRoleRepository.findByRoleName("USER")
                .orElseGet(() -> {
                    UserRole newRole = new UserRole("USER", "Rôle utilisateur standard", true);
                    return userRoleRepository.save(newRole);
                });
            
            // Assigner le rôle à l'utilisateur
            user.setRole(userRole);
            
            // Sauvegarder l'utilisateur
            userRepository.save(user);
            
            // Créer un token de vérification
            VerificationToken verificationToken = new VerificationToken(user);
            verificationTokenRepository.save(verificationToken);
            
            // Envoyer l'email de vérification
            emailService.sendVerificationEmail(user, verificationToken.getToken());
            
            // L'utilisateur n'est pas encore vérifié, donc pas de JWT
            return new AuthResponse(null, null, "Bearer", 0L, authMapper.toUserResponse(user));
        } catch (Exception e) {
            // Log l'exception
            System.err.println("ERREUR D'INSCRIPTION: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            // Relancer avec un message plus clair
            throw new ApiException("Erreur d'inscription: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public boolean verifyEmail(String token) {
        try {
            System.out.println("Début de la vérification de l'email avec le token: " + token);
            VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    System.err.println("Token de vérification invalide: " + token);
                    return new ApiException("Token de vérification invalide", HttpStatus.NOT_FOUND);
                });
            
            if (verificationToken.isExpired()) {
                System.err.println("Token de vérification expiré: " + token);
                throw new ApiException("Token de vérification expiré", HttpStatus.BAD_REQUEST);
            }
            
            if (verificationToken.isUsed()) {
                System.err.println("Token de vérification déjà utilisé: " + token);
                throw new ApiException("Token de vérification déjà utilisé", HttpStatus.BAD_REQUEST);
            }
            
            User user = verificationToken.getUser();
            System.out.println("Utilisateur trouvé pour le token: " + user.getEmail());
            user.setEmailVerified(true);
            
            try {
                userRepository.save(user);
                System.out.println("Statut de l'utilisateur mis à jour avec succès.");
            } catch (Exception e) {
                System.err.println("Échec de la mise à jour du statut de l'utilisateur: " + e.getMessage());
                throw new ApiException("Erreur lors de la mise à jour du statut de l'utilisateur", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            verificationToken.setUsed(true);
            try {
                verificationTokenRepository.save(verificationToken);
                System.out.println("Token marqué comme utilisé avec succès.");
            } catch (Exception e) {
                System.err.println("Échec de la mise à jour du token: " + e.getMessage());
                throw new ApiException("Erreur lors de la mise à jour du token", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            // Envoyer l'email de bienvenue
            emailService.sendWelcomeEmail(user);
            System.out.println("Email de bienvenue envoyé à: " + user.getEmail());
            
            return true;
        } catch (ApiException apiException) {
            System.err.println("ERREUR DE VÉRIFICATION D'EMAIL: " + apiException.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("ERREUR INCONNUE: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean resendVerificationEmail(String email) {
        try {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Utilisateur non trouvé", HttpStatus.NOT_FOUND));
            
            if (user.isEmailVerified()) {
                throw new ApiException("Votre email est déjà vérifié", HttpStatus.BAD_REQUEST);
            }
            
            // Supprimer les anciens tokens de vérification
            verificationTokenRepository.findByUser(user).ifPresent(verificationTokenRepository::delete);
            
            // Créer un nouveau token de vérification
            VerificationToken verificationToken = new VerificationToken(user);
            verificationTokenRepository.save(verificationToken);
            
            // Renvoyer l'email de vérification
            emailService.sendVerificationEmail(user, verificationToken.getToken());
            
            return true;
        } catch (Exception e) {
            System.err.println("ERREUR DE RENVOI D'EMAIL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private void saveUserToken(User user, String jwtToken) {
        Token token = new Token();
        token.setToken(jwtToken);
        token.setTokenType("Bearer");
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plus(jwtUtil.getAccessTokenExpirationInSeconds(), ChronoUnit.SECONDS));
        tokenRepository.save(token);
    }
    
    private void revokeAllUserTokens(User user) {
        tokenRepository.revokeAllUserTokens(user);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        
        String userEmail = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ApiException("Utilisateur non trouvé", HttpStatus.NOT_FOUND));
        
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new ApiException("Refresh token expiré", HttpStatus.UNAUTHORIZED);
        }
        
        // Révocation des tokens existants
        revokeAllUserTokens(user);
        
        // Génération de nouveaux tokens
        String accessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        
        // Sauvegarde du token
        saveUserToken(user, accessToken);
        
        return authMapper.toAuthResponse(accessToken, newRefreshToken, user);
    }

    @Override
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        tokenRepository.findByToken(token)
            .ifPresent(t -> {
                t.setRevoked(true);
                t.setExpired(true);
                tokenRepository.save(t);
            });
    }
}
