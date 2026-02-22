package com.project.Ums.controller;

import com.project.Ums.dto.LoginDto;
import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.entity.User;
import com.project.Ums.repository.UserRepository;
import com.project.Ums.service.AdminService;
import com.project.Ums.service.UserDetailServiceImpl;
import com.project.Ums.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private AdminService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminService adminService;


    @GetMapping("/view-profile")
    public ResponseEntity<?> userByUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return new ResponseEntity<>(userRepository.findByUserName(userName), HttpStatus.OK);
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userRepository.findByUserName(userName);
            userInDb.setUserName(dto.getUserName());
            userInDb.setPassword(passwordEncoder.encode(dto.getPassword()));
            userInDb.setEmail(dto.getEmail());
        return new ResponseEntity<>(userRepository.save(userInDb), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userRepository.deleteUserByUserName(userName);
        return ResponseEntity.ok("User Deleted successfully");
    }

}
