package com.project.Ums.controller;

import com.project.Ums.dto.ForgotPasswordDto;
import com.project.Ums.dto.LoginDto;
import com.project.Ums.dto.LoginResponse;
import com.project.Ums.dto.ResetPasswordDto;
import com.project.Ums.exception.UserNotFoundException;
import com.project.Ums.exception.InvalidTokenException;
import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.entity.User;
import com.project.Ums.logging.LogActivity;
import com.project.Ums.mapper.UserMapper;
import com.project.Ums.repository.UserRepository;
import com.project.Ums.service.AdminService;
import com.project.Ums.service.OtpService;
import com.project.Ums.service.PasswordResetService;
import com.project.Ums.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Autowired
    private AdminService adminService;
    @Autowired
    private PasswordResetService passwordResetService;

    @Operation(summary = "User login", description = "Authenticates user credentials and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful - JWT token returned"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "403", description = "User not verified"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="LOGIN", description="User login attempt")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto){
        try{
            User user = userRepository.findByUserName(dto.getUserName())
                        .orElseThrow(() -> new UserNotFoundException("User not found"));

            if(!"ACTIVE".equals(user.getStatus())){
                throw new RuntimeException("User is not verified");
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUserName(), dto.getPassword()));
            String token = jwtUtil.generateToken(authentication.getName());
            
            // Return both token and user data
            return ResponseEntity.ok(new LoginResponse(token, UserMapper.toProfile(user)));
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            throw e; // Let global exception handler handle it
        }
    }

    @Operation(summary = "Verify OTP", description = "Verifies user email using One-Time Password for account activation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP verified successfully - Account activated"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired OTP"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="VERIFY_OTP", description="Email verification via OTP")
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @Parameter(description = "User ID", required = true)
            @RequestParam String id,
            @Parameter(description = "User email address", required = true)
            @RequestParam String email,
            @Parameter(description = "One-Time Password sent to email", required = true)
            @RequestParam String otp) {
        String response = otpService.verifyOtp(id, email, otp);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Request OTP verification", description = "Sends OTP to user's email for account verification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email or user already verified"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="REQUEST_OTP", description="User requested OTP verification")
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(
            @RequestParam String id,
            @RequestParam String email) {
        try {
            otpService.sendOtpOnRequest(id, email);
            return ResponseEntity.ok("OTP sent to your email. Valid for 5 minutes.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Forgot password", description = "Sends password reset link to user's email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset link sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid email address"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="FORGOT_PASSWORD", description="User requested password reset")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDto dto) {
        try {
            String response = passwordResetService.forgotPassword(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Exception occurred during forgot password request", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Reset password", description = "Resets user password using valid token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="RESET_PASSWORD", description="User reset password")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto dto) {
        try {
            String response = passwordResetService.resetPassword(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Exception occurred during password reset", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
