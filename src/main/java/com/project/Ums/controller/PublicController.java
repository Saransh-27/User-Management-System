package com.project.Ums.controller;

import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.service.EmailService;
import com.project.Ums.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserRequestDto dto) {
        userService.addUser(dto);
        emailService.sendWelcomeEmail(dto);
        return ResponseEntity.ok("User added successfully and Username Password sent to your email");
    }
}
