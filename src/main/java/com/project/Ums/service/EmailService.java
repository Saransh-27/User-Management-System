package com.project.Ums.service;

import com.project.Ums.dto.UserRequestDto;
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
        } catch (Exception e) {
            log.error("Exception while sendmail ", e);
        }
    }

    public void sendWelcomeEmail(UserRequestDto user) {
        String subject = "Welcome to User Management System!";
        String body = createWelcomeMessage(user);
        sendEmail(user.getEmail(), subject, body);
        log.info("Welcome email sent to: {}", user.getEmail());
    }

    private String createWelcomeMessage(UserRequestDto user) {
        return "Dear " + user.getUserName() + ",\n\n" +
                "Welcome to the User Management System!\n\n" +
                "Your account has been successfully created with the following details:\n" +
                "Username: " + user.getUserName() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "Password: " + user.getPassword() + "\n\n" +
                "You can now log in to your account and start using our services.\n\n" +
                "If you have any questions or need assistance, please don't hesitate to contact us.\n\n" +
                "Best regards,\n" +
                "User Management System Team";
    }
}
