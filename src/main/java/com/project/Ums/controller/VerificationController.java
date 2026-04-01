package com.project.Ums.controller;

import com.project.Ums.dto.VerificationResponse;
import com.project.Ums.entity.User;
import com.project.Ums.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @Value("${app.environment}")
    private String environment;

    @Value("${app.email.enabled}")
    private boolean emailEnabled;

    @Operation(summary = "Get verification link (production only)", description = "Returns verification link instead of sending email in production environment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification link returned"),
            @ApiResponse(responseCode = "400", description = "Not in production environment"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/verification-link")
    public ResponseEntity<?> getVerificationLink(@RequestBody User user) {
        try {
            // Allow endpoint in both production and when email is disabled
            if (!"production".equalsIgnoreCase(environment) && emailEnabled) {
                return ResponseEntity.badRequest().body("This endpoint is only available in production environment or when email is disabled");
            }
            
            // Create verification token and get link
            String verificationLink = verificationService.createVerificationToken(user, null);
            
            if (verificationLink != null) {
                return ResponseEntity.ok(new VerificationResponse(true, verificationLink, null));
            } else {
                return ResponseEntity.badRequest().body(new VerificationResponse(false, null, "Failed to generate verification link"));
            }
        } catch (Exception e) {
            log.error("Failed to generate verification link: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new VerificationResponse(false, null, "Failed to generate verification link: " + e.getMessage()));
        }
    }

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
            @RequestParam String token,
            @RequestHeader(value = "Accept", defaultValue = "application/json") String acceptHeader) {
        try {
            String result = verificationService.verifyToken(token);
            log.info("Email verified successfully for token: {}", token);
            
            // Return HTML response for browser requests
            if (acceptHeader.contains("text/html")) {
                String htmlResponse = createSuccessHtmlResponse(result);
                return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(htmlResponse);
            }
            
            // Return JSON response for API requests
            return ResponseEntity.ok(new VerificationResponse(true, result, null));
        } catch (RuntimeException e) {
            log.error("Email verification failed for token: {}", token, e);
            
            // Return HTML response for browser requests
            if (acceptHeader.contains("text/html")) {
                String htmlResponse = createErrorHtmlResponse(e.getMessage());
                return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_HTML)
                    .body(htmlResponse);
            }
            
            // Return JSON response for API requests
            return ResponseEntity.badRequest().body(new VerificationResponse(false, null, e.getMessage()));
        }
    }

    private String createSuccessHtmlResponse(String message) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Email Verified Successfully</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            min-height: 100vh;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background: white;\n" +
                "            padding: 2rem;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 10px 25px rgba(0,0,0,0.1);\n" +
                "            text-align: center;\n" +
                "            max-width: 500px;\n" +
                "            margin: 20px;\n" +
                "        }\n" +
                "        .success-icon {\n" +
                "            color: #28a745;\n" +
                "            font-size: 4rem;\n" +
                "            margin-bottom: 1rem;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "            margin-bottom: 1rem;\n" +
                "        }\n" +
                "        p {\n" +
                "            color: #666;\n" +
                "            line-height: 1.6;\n" +
                "            margin-bottom: 1.5rem;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            display: inline-block;\n" +
                "            background: #667eea;\n" +
                "            color: white;\n" +
                "            padding: 12px 30px;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            font-weight: bold;\n" +
                "            transition: background 0.3s ease;\n" +
                "        }\n" +
                "        .btn:hover {\n" +
                "            background: #5a6fd8;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"success-icon\">✓</div>\n" +
                "        <h1>Email Verified Successfully!</h1>\n" +
                "        <p>" + message + "</p>\n" +
                "        <p>Your account has been successfully verified and activated. You can now log in to your account.</p>\n" +
                "        <a href=\"http://localhost:3000/login\" class=\"btn\">Go to Login</a>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String createErrorHtmlResponse(String errorMessage) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Verification Failed</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            min-height: 100vh;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background: white;\n" +
                "            padding: 2rem;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 10px 25px rgba(0,0,0,0.1);\n" +
                "            text-align: center;\n" +
                "            max-width: 500px;\n" +
                "            margin: 20px;\n" +
                "        }\n" +
                "        .error-icon {\n" +
                "            color: #dc3545;\n" +
                "            font-size: 4rem;\n" +
                "            margin-bottom: 1rem;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #333;\n" +
                "            margin-bottom: 1rem;\n" +
                "        }\n" +
                "        p {\n" +
                "            color: #666;\n" +
                "            line-height: 1.6;\n" +
                "            margin-bottom: 1.5rem;\n" +
                "        }\n" +
                "        .btn {\n" +
                "            display: inline-block;\n" +
                "            background: #667eea;\n" +
                "            color: white;\n" +
                "            padding: 12px 30px;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            font-weight: bold;\n" +
                "            transition: background 0.3s ease;\n" +
                "        }\n" +
                "        .btn:hover {\n" +
                "            background: #5a6fd8;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"error-icon\">✗</div>\n" +
                "        <h1>Verification Failed</h1>\n" +
                "        <p>We couldn't verify your email address.</p>\n" +
                "        <p><strong>Error:</strong> " + errorMessage + "</p>\n" +
                "        <p>Please contact support or try requesting a new verification email.</p>\n" +
                "        <a href=\"http://localhost:3000/login\" class=\"btn\">Go to Login</a>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

}
