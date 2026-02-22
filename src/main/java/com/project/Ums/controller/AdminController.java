package com.project.Ums.controller;

import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.service.EmailService;
import com.project.Ums.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserRequestDto dto) {
        adminService.addUser(dto);
        emailService.sendWelcomeEmail(dto);
        return ResponseEntity.ok("User added successfully and Username Password sent to Users email");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(adminService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/User/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id){
        return new ResponseEntity<>(adminService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String id){
        adminService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

}
