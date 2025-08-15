package com.invoice.planner.mapper;

import com.invoice.planner.dto.request.RegisterRequest;
import com.invoice.planner.dto.response.AuthResponse;
import com.invoice.planner.dto.response.UserResponse;
import com.invoice.planner.entity.User;
import com.invoice.planner.entity.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class AuthMapper {
    private final PasswordEncoder passwordEncoder;
    
    public AuthMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public User toEntity(RegisterRequest request) {
        if (request == null) {
            return null;
        }
        
        User user = new User();
        user.setPrenom(request.getPrenom());
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNumeroDeTelephone(request.getNumeroDeTelephone());
        user.setTrackingId(UUID.randomUUID());
        
        return user;
    }
    
    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponse response = new UserResponse();
        response.setTrackingId(user.getTrackingId());
        response.setPrenom(user.getPrenom());
        response.setNom(user.getNom());
        response.setEmail(user.getEmail());
        response.setNumeroDeTelephone(user.getNumeroDeTelephone());
        response.setRole(user.getRole().getRoleName());
        response.setCreatedAt(user.getCreatedAt());
        
        return response;
    }
    
    public AuthResponse toAuthResponse(String accessToken, String refreshToken, User user) {
        if (user == null) {
            return null;
        }
        
        AuthResponse response = new AuthResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(3600L); // 1 heure par défaut
        response.setUser(toUserResponse(user));
        
        return response;
    }
}
