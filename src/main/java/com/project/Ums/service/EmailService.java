package com.project.Ums.service;

import com.project.Ums.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.UDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Base64;


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
        } catch (Exception e) {
            log.error("Exception while sendmail ", e);
        }
    }

    public void sendRegistrationEmail(User user, String otp) {
        String subject = "User Registration - OTP Verification Required";
        String body = createRegistrationMessage(user, otp);
        sendEmail(user.getEmail(), subject, body);
        log.info("Registration email sent to: {}", user.getEmail());
    }

    public void sendWelcomeEmail(User user) {
        String subject = "Welcome to User Management System!";
        String body = createWelcomeMessage(user);
        sendEmail(user.getEmail(), subject, body);
        log.info("Welcome email sent to: {}", user.getEmail());
    }

    private String createRegistrationMessage(User user, String otp) {
        return "Dear User,\n\n" +
                "Welcome to the User Management System!\n\n" +
                "Your account has been created by an administrator. Please verify your account using the following details:\n\n" +
                "User ID: " + user.getId() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "OTP: " + otp + "\n\n" +
                "Please use this OTP to complete your registration process.\n\n" +
                "Best regards,\n" +
                "User Management System Team";
    }

    private String createWelcomeMessage(User user) {
        // Decode the original password from Base64
        String originalPassword = new String(Base64.getDecoder().decode(user.getEncodedOriginalPassword()));

        return "Dear " + user.getUserName() + ",\n\n" +
                "Welcome to the User Management System!\n\n" +
                "Your account has been successfully verified and activated. You can now login with the following credentials:\n\n" +
                "Username: " + user.getUserName() + "\n" +
                "Password: " + originalPassword + "\n" +
                "User ID: " + user.getId() + "\n\n" +
                "Please keep your User ID safe for future reference.\n\n" +
                "You can now log in to your account and start using our services.\n\n" +
                "If you have any questions or need assistance, please don't hesitate to contact us.\n\n" +
                "Best regards,\n" +
                "User Management System Team";
    }
}
