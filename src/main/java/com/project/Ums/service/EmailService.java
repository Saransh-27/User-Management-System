package com.project.Ums.service;

import com.project.Ums.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body){
        try{
            SimpleMailMessage mail = new SimpleMailMessage();
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

    public void sendWelcomeEmail(User user) {
        String subject = "Welcome to User Management System!";
        String body = createWelcomeMessage(user);
        sendEmail(user.getEmail(), subject, body);
        log.info("Welcome email sent to: {}", user.getEmail());
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

    private String createWelcomeMessage(User user) {
        return "Dear " + user.getUserName() + ",\n\n" +
                "Welcome to the User Management System!\n\n" +
                "Your account has been successfully verified and activated. You can now login with the following credentials:\n\n" +
                "Username: " + user.getUserName() + "\n" +
                "Password: " + "Same as UserName" + "\n" +
                "User ID: " + user.getId() + "\n\n" +
                "Please keep your User ID safe for future reference.\n\n" +
                "You can now log in to your account and start using our services.\n\n" +
                "If you have any questions or need assistance, please don't hesitate to contact us.\n\n" +
                "Best regards,\n" +
                "User Management System Team";
    }
}
