package com.project.Ums.controller;

import com.project.Ums.dto.VerificationResponse;
import com.project.Ums.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @Operation(summary = "Verify user email", description = "Verifies user email using token sent to their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token"),
            @ApiResponse(responseCode = "404", description = "Token not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(
            @Parameter(description = "Verification token sent to user's email", required = true)
            @RequestParam String token) {
        try {
            String result = verificationService.verifyToken(token);
            log.info("Email verified successfully for token: {}", token);
            return ResponseEntity.ok(new VerificationResponse(true, result, null));
        } catch (RuntimeException e) {
            log.error("Email verification failed for token: {}", token, e);
            return ResponseEntity.badRequest().body(new VerificationResponse(false, null, e.getMessage()));
        }
    }

}
