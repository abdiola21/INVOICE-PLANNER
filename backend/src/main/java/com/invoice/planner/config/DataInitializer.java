// package com.invoice.planner.config;

// import com.invoice.planner.entity.User;
// import com.invoice.planner.enums.UserRole;
// import com.invoice.planner.repository.UserRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import java.util.UUID;

// @Configuration
// public class DataInitializer {

//     @Bean
//     public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//         return args -> {
//             // Vérifie si un utilisateur admin existe déjà
//             if (userRepository.findByEmail("admin@example.com").isEmpty()) {
//                 System.out.println("Création de l'utilisateur admin par défaut...");
                
//                 // Créer un utilisateur admin par défaut
//                 User admin = new User();
//                 admin.setEmail("admin@example.com");
//                 admin.setPassword(passwordEncoder.encode("password"));
//                 admin.setNom("Admin");
//                 admin.setPrenom("System");
//                 admin.setRole(UserRole.ADMIN);
//                 admin.setTrackingId(UUID.randomUUID());
                
//                 userRepository.save(admin);
                
//                 System.out.println("Utilisateur admin créé avec succès!");
//             }
            
//             // Vérifie si l'utilisateur lenuel@gmail.com existe déjà
//             if (userRepository.findByEmail("lenuel@gmail.com").isEmpty()) {
//                 System.out.println("Création de l'utilisateur test...");
                
//                 // Créer un utilisateur test
//                 User testUser = new User();
//                 testUser.setEmail("lenuel@gmail.com");
//                 testUser.setPassword(passwordEncoder.encode("12345678"));
//                 testUser.setNom("Lenuel");
//                 testUser.setPrenom("Test");
//                 testUser.setRole(UserRole.USER);
//                 testUser.setTrackingId(UUID.randomUUID());
                
//                 userRepository.save(testUser);
                
//                 System.out.println("Utilisateur test créé avec succès!");
//             }
//         };
//     }
// } 