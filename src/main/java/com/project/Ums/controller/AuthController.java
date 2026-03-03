package com.project.Ums.controller;

import com.project.Ums.dto.LoginDto;
import com.project.Ums.entity.User;
import com.project.Ums.repository.UserRepository;
import com.project.Ums.service.OtpService;
import com.project.Ums.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestBody LoginDto dto){
        try{
            User user = userRepository.findByUserName(dto.getUserName())
                        .orElseThrow(() -> new RuntimeException("User not found"));
            
            if(!"ACTIVE".equals(user.getStatus())){
                throw new RuntimeException("User is not verified");
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUserName(), dto.getPassword()));
            return jwtUtil.generateToken(authentication.getName());
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            throw new RuntimeException("Incorrect username or password", e);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @RequestParam String id, 
            @RequestParam String email, 
            @RequestParam String otp) {
        String response = otpService.verifyOtp(id, email, otp);
        return ResponseEntity.ok(response);
    }
}
