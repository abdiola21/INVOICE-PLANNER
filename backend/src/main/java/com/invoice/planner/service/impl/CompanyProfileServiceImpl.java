package com.invoice.planner.service.impl;

import com.invoice.planner.dto.request.CompanyProfileRequest;
import com.invoice.planner.dto.response.CompanyProfileResponse;
import com.invoice.planner.entity.CompanyProfile;
import com.invoice.planner.entity.User;
import com.invoice.planner.exception.ResourceNotFoundException;
import com.invoice.planner.mapper.CompanyProfileMapper;
import com.invoice.planner.repository.CompanyProfileRepository;
import com.invoice.planner.repository.UserRepository;
import com.invoice.planner.service.CompanyProfileService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.Optional;

@Service
public class CompanyProfileServiceImpl implements CompanyProfileService {

    private CompanyProfileRepository companyProfileRepository;
    
    private UserRepository userRepository;
    
    @Value("${app.uploads.directory:uploads}")
    private String uploadDirectory;
    
    private CompanyProfileMapper companyProfileMapper;
    
    public CompanyProfileServiceImpl(CompanyProfileRepository companyProfileRepository,
									 UserRepository userRepository,
									 @Value("${app.uploads.directory:uploads}") String uploadDirectory,
									 CompanyProfileMapper companyProfileMapper) {
		this.companyProfileRepository = companyProfileRepository;
		this.userRepository = userRepository;
		this.uploadDirectory = uploadDirectory;
		this.companyProfileMapper = companyProfileMapper;
	}
    
    @Override
    @Transactional
    public CompanyProfileResponse createOrUpdateProfile(Long userId, CompanyProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        
        CompanyProfile profile = companyProfileRepository.findByUser(user)
                .orElse(new CompanyProfile());
        
        // Mettre à jour ou créer le profil d'entreprise
        profile.setCompanyName(request.getCompanyName());
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setPostalCode(request.getPostalCode());
        profile.setCountry(request.getCountry());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setEmail(request.getEmail());
        profile.setWebsite(request.getWebsite());
        profile.setTaxNumber(request.getTaxNumber());
        profile.setRegistrationNumber(request.getRegistrationNumber());
        
        // Si c'est un nouveau profil, définir l'utilisateur
        if (profile.getUser() == null) {
            profile.setUser(user);
        }
        
        CompanyProfile savedProfile = companyProfileRepository.save(profile);
        
        // Mettre à jour le statut de l'utilisateur si le profil est complet
        if (isProfileCompleted(userId)) {
            user.setProfileCompleted(true);
            userRepository.save(user);
        }
        
        return companyProfileMapper.toResponse(savedProfile);
    }
    
    @Override
    public CompanyProfileResponse getProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        
        // Plutôt que de lancer une exception, retourner un profil vide si l'utilisateur n'a pas encore de profil
        Optional<CompanyProfile> profileOptional = companyProfileRepository.findByUser(user);
        if (profileOptional.isEmpty()) {
            // Créer et retourner un profil vide avec les informations minimales
            CompanyProfileResponse emptyProfile = new CompanyProfileResponse();
            emptyProfile.setHasLogo(false);
            return emptyProfile;
        }
        
        return companyProfileMapper.toResponse(profileOptional.get());
    }
    
    @Override
    public boolean isProfileCompleted(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        
        // Vérifier d'abord si le profil existe
        boolean profileExists = companyProfileRepository.existsByUser(user);
        if (!profileExists) {
            return false;
        }
        
        CompanyProfile profile = companyProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profil d'entreprise non trouvé pour l'utilisateur"));
        
        // Vérifier si tous les champs obligatoires sont remplis
        return profile.getCompanyName() != null && !profile.getCompanyName().isEmpty() &&
               profile.getAddress() != null && !profile.getAddress().isEmpty() &&
               profile.getCity() != null && !profile.getCity().isEmpty() &&
               profile.getPostalCode() != null && !profile.getPostalCode().isEmpty() &&
               profile.getCountry() != null && !profile.getCountry().isEmpty() &&
               profile.getEmail() != null && !profile.getEmail().isEmpty();
    }
    
    @Override
    @Transactional
    public CompanyProfileResponse uploadLogo(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));
        
        CompanyProfile profile = companyProfileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profil d'entreprise non trouvé pour l'utilisateur : " + userId));
        
        // Créer le répertoire de stockage s'il n'existe pas
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Générer un nom de fichier unique
        String fileName = profile.getTrackingId() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        
        // Copier le fichier dans le répertoire de stockage
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Mettre à jour le chemin du logo dans le profil et marquer hasLogo comme true
        profile.setLogoPath(fileName);
        profile.setHasLogo(true);
        
        CompanyProfile updatedProfile = companyProfileRepository.save(profile);
        
        // Log pour le débogage
        System.out.println("Logo enregistré pour le profil " + profile.getTrackingId() + " à " + fileName);
        
        return companyProfileMapper.toResponse(updatedProfile);
    }
    
    @Override
    public byte[] getLogo(UUID profileId) throws IOException {
        CompanyProfile profile = companyProfileRepository.findByTrackingId(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profil d'entreprise non trouvé avec l'ID : " + profileId));
        
        if (profile.getLogoPath() == null) {
            throw new ResourceNotFoundException("Logo non trouvé pour ce profil");
        }
        
        Path logoPath = Paths.get(uploadDirectory).resolve(profile.getLogoPath());
        if (!Files.exists(logoPath)) {
            throw new ResourceNotFoundException("Fichier logo non trouvé");
        }
        
        return Files.readAllBytes(logoPath);
    }
    
    
    
} 