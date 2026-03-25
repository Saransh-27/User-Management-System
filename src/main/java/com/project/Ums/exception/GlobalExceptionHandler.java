package com.project.Ums.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Custom Exception Handlers
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordResetException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordReset(PasswordResetException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidToken(InvalidTokenException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleTokenExpired(TokenExpiredException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailServiceException.class)
    public ResponseEntity<Map<String, Object>> handleEmailService(EmailServiceException ex) {
        return buildResponse("Failed to send email. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<Map<String, Object>> handleFileUpload(FileUploadException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPassword(InvalidPasswordException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountVerificationException.class)
    public ResponseEntity<Map<String, Object>> handleAccountVerification(AccountVerificationException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientPermissionException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientPermission(InsufficientPermissionException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // Authentication & Authorization Exception Handlers
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse("Access denied. You don't have permission to perform this action.", HttpStatus.FORBIDDEN);
    }

    // File Upload Exception Handlers
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        return buildResponse("File size exceeds the maximum allowed limit (5MB)", HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Map<String, Object>> handleMultipart(MultipartException ex) {
        return buildResponse("File upload failed. Please check the file format and size.", HttpStatus.BAD_REQUEST);
    }

    // File System Exception Handlers
    @ExceptionHandler(java.nio.file.AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleFileAccessDenied(java.nio.file.AccessDeniedException ex) {
        return buildResponse("File access denied. Please check file permissions.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIO(IOException ex) {
        return buildResponse("File operation failed. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Validation Exception Handlers
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Validation failed");
        body.put("errors", errors);
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
        
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Validation failed");
        body.put("errors", errors);
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Database Exception Handlers
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateKey(DuplicateKeyException ex) {
        String message = "A record with this value already exists.";
        if (ex.getMessage() != null) {
            String msg = ex.getMessage().toLowerCase();
            if (msg.contains("email")) {
                message = "This email address is already in use.";
            } else if (msg.contains("username") || msg.contains("username")) {
                message = "This username is already taken.";
            } else if (msg.contains("token")) {
                message = "This reset token has already been used.";
            }
        }
        return buildResponse(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccess(DataAccessException ex) {
        return buildResponse("Database operation failed. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Network & Service Exception Handlers
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAccess(ResourceAccessException ex) {
        return buildResponse("Service temporarily unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    // JWT & Token Exception Handlers
    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredJwt(io.jsonwebtoken.ExpiredJwtException ex) {
        return buildResponse("Token has expired. Please login again.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(io.jsonwebtoken.MalformedJwtException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedJwt(io.jsonwebtoken.MalformedJwtException ex) {
        return buildResponse("Invalid token format.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(io.jsonwebtoken.SignatureException.class)
    public ResponseEntity<Map<String, Object>> handleJwtSignature(io.jsonwebtoken.SignatureException ex) {
        return buildResponse("Invalid token signature.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(io.jsonwebtoken.UnsupportedJwtException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedJwt(io.jsonwebtoken.UnsupportedJwtException ex) {
        return buildResponse("Unsupported token.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(io.jsonwebtoken.security.SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleJwtSecurity(io.jsonwebtoken.security.SecurityException ex) {
        return buildResponse("Invalid token security.", HttpStatus.UNAUTHORIZED);
    }

    // General Exception Handlers
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointer(NullPointerException ex) {
        return buildResponse("A null value was encountered. Please check your request.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        // Log the full exception for debugging
        ex.printStackTrace();
        return buildResponse(
            ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred", 
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        // Log the full exception for debugging
        ex.printStackTrace();
        return buildResponse("An internal server error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("status", status.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("error", status.getReasonPhrase());
        return new ResponseEntity<>(body, status);
    }
}
