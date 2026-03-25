package com.project.Ums.service;

import com.project.Ums.dto.ForgotPasswordDto;
import com.project.Ums.dto.ResetPasswordDto;
import com.project.Ums.entity.PasswordResetToken;
import com.project.Ums.entity.User;
import com.project.Ums.exception.EmailServiceException;
import com.project.Ums.exception.InvalidTokenException;
import com.project.Ums.exception.UserNotFoundException;
import com.project.Ums.repository.PasswordResetTokenRepository;
import com.project.Ums.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public String forgotPassword(ForgotPasswordDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account found with this email address"));

        try {
            // Invalidate any existing tokens for this user
            tokenRepository.deleteByUserId(user.getId());

            // Generate new token
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token(token)
                    .userId(user.getId())
                    .expiryDate(LocalDateTime.now().plusHours(1)) // Token valid for 1 hour
                    .used(false)
                    .build();

            tokenRepository.save(resetToken);

            // Send email with reset link
            emailService.sendPasswordResetEmail(user.getEmail(), token);
            log.info("Password reset email sent to: {}", user.getEmail());
            return "Password reset link has been sent to your email address";
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", user.getEmail(), e);
            throw new EmailServiceException("Failed to send reset email. Please try again later.", e);
        }
    }

    @Transactional
    public String resetPassword(ResetPasswordDto dto) {
        PasswordResetToken token = tokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));

        if (token.isExpired() || token.getUsed()) {
            throw new InvalidTokenException("Invalid or expired reset token");
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        // Mark token as used
        token.setUsed(true);
        tokenRepository.save(token);

        // Clean up any other tokens for this user
        tokenRepository.deleteByUserId(user.getId());

        log.info("Password reset successfully for user: {}", user.getUserName());
        return "Password has been reset successfully";
    }

    public boolean validateToken(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.getUsed())
                .orElse(false);
    }
}
