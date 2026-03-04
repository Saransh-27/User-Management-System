package com.project.Ums.service;

import com.project.Ums.entity.User;
import com.project.Ums.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Slf4j
public class OtpService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    private final SecureRandom random = new SecureRandom();

    public void sendOtp(User user) {
        String otp = generateOtp();
        // SAVE OTP
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        // Send registration email with OTP
        emailService.sendRegistrationEmail(user, otp);
        log.info("Registration email with OTP sent to: {}", user.getEmail());
    }

    public String generateOtp() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public String verifyOtp(String id, String email, String enteredOtp) {
        User user = userRepository.findByIdAndEmail(id, email)
                .orElseThrow(() -> new RuntimeException("User not found with provided ID and email"));
        if ("ACTIVE".equals(user.getStatus())) {
            return "User already verified.";
        }
        if (user.getOtp() == null) {
            return "No OTP found. Please request new OTP.";
        }
        if (user.getOtpExpiry() == null ||
                user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return "OTP Expired. Please request new OTP.";
        }
        if (!user.getOtp().equals(enteredOtp)) {
            return "Invalid OTP.";
        }
        // SUCCESS
        user.setStatus("ACTIVE");
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        // Send welcome email with login credentials
        emailService.sendWelcomeEmail(user);
        return "OTP Verified Successfully! Welcome email with login credentials has been sent.";
    }
}
