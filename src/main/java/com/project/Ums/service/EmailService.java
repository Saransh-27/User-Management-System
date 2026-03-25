package com.project.Ums.service;

import com.project.Ums.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Value("${EMAIL_FROM:noreply@ums.com}")
    private String emailFrom;
    
    @Value("${EMAIL_TEAM_NAME:User Management System Team}")
    private String teamName;

    public void sendEmail(String to, String subject, String body){
        try{
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(emailFrom);
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            javaMailSender.send(mail);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Exception while sending email to {}: ", to, e);
        }
    }

    public void sendUserCreationEmail(User user) {
        String subject = "Account Created - Verification Required";
        String body = createUserCreationMessage(user);
        sendEmail(user.getEmail(), subject, body);
        log.info("User creation email sent to: {}", user.getEmail());
    }

    public void sendOTPEmail(User user, String otp) {
        String subject = "OTP Verification - User Management System";
        String body = createOTPMessage(user, otp);
        sendEmail(user.getEmail(), subject, body);
        log.info("OTP email sent to: {}", user.getEmail());
    }

    public void sendVerificationEmail(User user, String verificationLink) {
        String subject = "Verify Your Email Address";
        String body = createVerificationMessage(user, verificationLink);
        sendEmail(user.getEmail(), subject, body);
        log.info("Verification email sent to: {}", user.getEmail());
    }

    public void sendWelcomeEmail(User user, String rawPassword) {
        String subject = "Welcome to User Management System!";
        String body = createWelcomeMessage(user, rawPassword);
        sendEmail(user.getEmail(), subject, body);
        log.info("Welcome email sent to: {}", user.getEmail());
    }

    public void sendPasswordResetEmail(String email, String resetToken) {
        String subject = "Password Reset Request - User Management System";
        String body = createPasswordResetMessage(resetToken);
        sendEmail(email, subject, body);
        log.info("Password reset email sent to: {}", email);
    }

    private String createUserCreationMessage(User user) {
        return "Dear User,\n\n" +
                "Your account has been successfully created in the User Management System by an administrator.\n\n" +
                "Account Details:\n" +
                "User ID: " + user.getId() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "Username: " + user.getUserName() + "\n\n" +
                "IMPORTANT: You need to verify your account before accessing the system.\n" +
                "Please request OTP verification to complete your account setup.\n\n" +
                "Best regards,\n" +
                "User Management System Team";
    }

    private String createOTPMessage(User user, String otp) {
        return "Dear " + user.getUserName() + ",\n\n" +
                "Here is your OTP for account verification:\n\n" +
                "OTP: " + otp + "\n" +
                "Valid for: 5 minutes\n\n" +
                "Please use this OTP to complete your verification process.\n" +
                "If you didn't request this OTP, please contact support immediately.\n\n" +
                "Best regards,\n" +
                "User Management System Team";
    }

    private String createVerificationMessage(User user, String verificationLink) {
        return "Dear " + user.getUserName() + ",\n\n" +
                "Thank you for creating an account with the User Management System.\n\n" +
                "Please click on the link below to verify your email address and activate your account:\n\n" +
                verificationLink + "\n\n" +
                "This verification link will expire in 24 hours.\n\n" +
                "If you didn't create an account with us, please ignore this email.\n\n" +
                "Best regards,\n" +
                "User Management System Team";
    }

    private String createWelcomeMessage(User user, String rawPassword) {
        String passwordDisplay = (rawPassword != null && !rawPassword.isBlank())
                ? rawPassword
                : "(Set during registration)";
        
        return "Dear " + user.getUserName() + ",\n\n" +
                "Welcome to the User Management System!\n\n" +
                "Your account has been successfully verified and activated. You can now login with the following credentials:\n\n" +
                "Username: " + user.getUserName() + "\n" +
                "Password: " + passwordDisplay + "\n" +
                "User ID: " + user.getId() + "\n\n" +
                "IMPORTANT: For security reasons, we strongly recommend changing your password after your first login.\n\n" +
                "Please keep your credentials safe and do not share them with anyone.\n\n" +
                "You can now log in to your account and start using our services.\n\n" +
                "If you have any questions or need assistance, please don't hesitate to contact us.\n\n" +
                "Best regards,\n" +
                "User Management System Team";
    }

    private String createPasswordResetMessage(String resetToken) {
        return "Dear User,\n\n" +
                "We received a request to reset your password for your User Management System account.\n\n" +
                "To reset your password, use the following token:\n\n" +
                "Reset Token: " + resetToken + "\n\n" +
                "You can also visit: http://localhost:5173/reset-password?token=" + resetToken + "\n\n" +
                "This token will expire in 1 hour for security reasons.\n\n" +
                "If you didn't request a password reset, please ignore this email or contact support immediately.\n\n" +
                "For security, please:\n" +
                "- Choose a strong password with at least 6 characters\n" +
                "- Don't reuse old passwords\n" +
                "- Keep your password confidential\n\n" +
                "Best regards,\n" +
                "User Management System Team";
    }
}
