package com.project.Ums.service;

import com.project.Ums.entity.User;
import com.project.Ums.entity.VerificationToken;
import com.project.Ums.repository.UserRepository;
import com.project.Ums.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@Slf4j
public class VerificationService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Value("${app.verification.url}")
    private String verificationUrl;
    
    private final SecureRandom random = new SecureRandom();

    public String createVerificationToken(User user, String rawPassword) {
        // Delete any existing tokens for this user
        verificationTokenRepository.deleteByUserId(user.getId());
        
        // Generate new token
        String token = generateToken();
        
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .rawPassword(rawPassword) // Store raw password for welcome email
                .expiryDate(LocalDateTime.now().plusHours(24)) // Token valid for 24 hours
                .verified(false)
                .build();
        
        verificationTokenRepository.save(verificationToken);
        
        // Send verification email
        String verificationLink = verificationUrl + "/verify-email?token=" + token;
        try {
            emailService.sendVerificationEmail(user, verificationLink);
            log.info("Verification token created and email sent for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Verification token created but FAILED to send email to {}: {}", user.getEmail(), e.getMessage(), e);
        }
        return token;
    }

    public String verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));
        
        if (verificationToken.isExpired()) {
            verificationTokenRepository.deleteByToken(token);
            throw new RuntimeException("Verification token has expired");
        }
        
        if (verificationToken.isVerified()) {
            return "Email already verified";
        }
        
        // Get user and update status
        User user = userRepository.findById(verificationToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if ("ACTIVE".equals(user.getStatus())) {
            return "User already verified";
        }
        
        // Mark user as active
        user.setStatus("ACTIVE");
        userRepository.save(user);
        
        // Mark token as verified
        verificationToken.setVerified(true);
        verificationTokenRepository.save(verificationToken);
        
        // Send welcome email with the original password
        String rawPassword = verificationToken.getRawPassword();
        try {
            emailService.sendWelcomeEmail(user, rawPassword);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", user.getEmail(), e.getMessage());
        }
        
        // Clear the raw password from the token after sending the email (security)
        verificationToken.setRawPassword(null);
        verificationTokenRepository.save(verificationToken);
        
        log.info("User verified successfully: {}", user.getEmail());
        return "User verified successfully";
    }

    private String generateToken() {
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
