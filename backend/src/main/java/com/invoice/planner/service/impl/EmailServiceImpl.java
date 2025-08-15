package com.invoice.planner.service.impl;

import com.invoice.planner.entity.User;
import com.invoice.planner.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${spring.mail.from:no-reply@invoiceplanner.local}")
    private String fromEmail;
    
    @Value("${app.base-url}")
    private String baseUrl;
    
    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    
    @Async
    @Override
    public void sendVerificationEmail(User user, String token) {
        try {
            String verificationUrl = baseUrl + "/auth/verify?token=" + token;
            
            Context context = new Context();
            context.setVariable("name", user.getPrenom() + " " + user.getNom());
            context.setVariable("verificationUrl", verificationUrl);
            
            String content = templateEngine.process("email/verification-email", context);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(user.getEmail());
            helper.setSubject("Vérification de votre compte Invoice Planner");
            helper.setText(content, true);
            
            mailSender.send(message);
            logger.info("Email de vérification envoyé à : {}", user.getEmail());
            
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de vérification", e);
        }
    }
    
    @Async
    @Override
    public void sendPasswordResetEmail(User user, String token) {
        try {
            String resetUrl = baseUrl + "/auth/reset-password?token=" + token;
            
            Context context = new Context();
            context.setVariable("name", user.getPrenom() + " " + user.getNom());
            context.setVariable("resetUrl", resetUrl);
            
            String content = templateEngine.process("email/reset-password", context);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(user.getEmail());
            helper.setSubject("Réinitialisation de votre mot de passe - Invoice Planner");
            helper.setText(content, true);
            
            mailSender.send(message);
            logger.info("Email de réinitialisation de mot de passe envoyé à : {}", user.getEmail());
            
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de réinitialisation", e);
        }
    }
    
    @Async
    @Override
    public void sendWelcomeEmail(User user) {
        try {
            String loginUrl = baseUrl + "/auth/login";
            
            Context context = new Context();
            context.setVariable("name", user.getPrenom() + " " + user.getNom());
            context.setVariable("loginUrl", loginUrl);
            
            String content = templateEngine.process("email/welcome", context);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(user.getEmail());
            helper.setSubject("Bienvenue sur Invoice Planner");
            helper.setText(content, true);
            
            mailSender.send(message);
            logger.info("Email de bienvenue envoyé à : {}", user.getEmail());
            
        } catch (MessagingException e) {
            logger.error("Erreur lors de l'envoi de l'email de bienvenue", e);
        }
    }
    
    @Override
    public void sendDevisWithAttachment(String toEmail, String subject, String message, String pdfPath) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(message, true);
            if (pdfPath != null) {
                java.io.File file = new java.io.File(pdfPath);
                if (file.exists()) {
                    helper.addAttachment("devis.pdf", file);
                }
            }
            mailSender.send(mimeMessage);
            logger.info("Devis envoyé à : {}", toEmail);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi du devis au client", e);
        }
    }
} 