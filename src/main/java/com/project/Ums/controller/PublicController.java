package com.project.Ums.controller;

import com.project.Ums.dto.UserUpdateDto;
import com.project.Ums.entity.User;
import com.project.Ums.exception.FileUploadException;
import com.project.Ums.exception.InvalidPasswordException;
import com.project.Ums.exception.UserNotFoundException;
import com.project.Ums.logging.LogActivity;
import com.project.Ums.logging.ActivityLog;
import com.project.Ums.logging.ActivityLogRepository;
import com.project.Ums.mapper.UserMapper;
import com.project.Ums.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.project.Ums.utils.JwtUtil;
import java.util.Base64;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "User Profile", description = "APIs for user profile management and self-service operations")
@SecurityRequirement(name = "bearerAuth")
public class PublicController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ActivityLogRepository activityLogRepository;
    @Autowired
    private JwtUtil jwtUtil;
    
    
    @Operation(summary = "View user profile", description = "Retrieves the current authenticated user's profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="VIEW_PROFILE", description="Viewed own profile")
    @GetMapping("/view-profile")
    public ResponseEntity<?> userByUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return new ResponseEntity<>(UserMapper.toProfile(user), HttpStatus.OK);
    }

    @Operation(summary = "Update user profile", description = "Partially updates the current authenticated user's profile. Only non-null fields are updated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate email or username"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="UPDATE_PROFILE", description="Updated own profile")
    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(
            @Parameter(description = "Updated user profile information", required = true)
            @RequestBody UserUpdateDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Partial update: only update fields that are provided (non-null and non-blank)
        boolean usernameChanged = false;
        if (dto.getUserName() != null && !dto.getUserName().isBlank()) {
            if (!dto.getUserName().equals(userInDb.getUserName())) {
                usernameChanged = true;
            }
            userInDb.setUserName(dto.getUserName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            userInDb.setEmail(dto.getEmail());
        }
        // Password update requires currentPassword verification
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (dto.getCurrentPassword() == null || dto.getCurrentPassword().isBlank()) {
                throw new InvalidPasswordException("Current password is required to set a new password");
            }
            if (!passwordEncoder.matches(dto.getCurrentPassword(), userInDb.getPassword())) {
                throw new InvalidPasswordException("Current password is incorrect");
            }
            userInDb.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User updatedUser = userRepository.save(userInDb);

        // Build response with profile data
        Map<String, Object> response = new HashMap<>();
        response.put("user", UserMapper.toProfile(updatedUser));

        // If username changed, issue a new JWT token so the session doesn't break
        if (usernameChanged) {
            String newToken = jwtUtil.generateToken(updatedUser.getUserName());
            response.put("token", newToken);
        }

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Change password", description = "Changes the current user's password after verifying the current password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid current password or weak new password"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @LogActivity(action="CHANGE_PASSWORD", description="Changed password")
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");

        if (currentPassword == null || currentPassword.isBlank()) {
            throw new IllegalArgumentException("Current password is required");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }
        if (newPassword.length() < 6) {
            throw new InvalidPasswordException("New password must be at least 6 characters");
        }
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Upload profile photo", description = "Uploads a profile photo for the current authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile photo uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file or upload failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="UPLOAD_PHOTO", description="Uploaded profile photo")
    @PostMapping("/upload-profile-photo")
    public ResponseEntity<?> uploadProfilePhoto(@RequestParam("file") MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            
            User user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            if (file.isEmpty()) {
                throw new FileUploadException("Please select a file to upload");
            }

            if (!file.getContentType().startsWith("image/")) {
                throw new FileUploadException("Only image files are allowed");
            }

            if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
                throw new FileUploadException("File size should be less than 5MB");
            }

            // Convert file to Base64 and store in database
            byte[] fileContent = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(fileContent);
            String mimeType = file.getContentType();
            String dataUrl = "data:" + mimeType + ";base64," + base64Image;
            
            // Update user profile with Base64 image data
            user.setProfilePhoto(dataUrl);
            userRepository.save(user);

            log.info("Profile photo uploaded and stored in database for user: {}", userName);
            return ResponseEntity.ok(UserMapper.toProfile(user));
        } catch (IOException e) {
            log.error("Error uploading profile photo", e);
            throw new FileUploadException("Failed to upload profile photo. Please try again.");
        }
    }

    
    @Operation(summary = "Delete user account", description = "Permanently deletes the current authenticated user's account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="DELETE_ACCOUNT", description="Deleted own account")
    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account deleted successfully");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get my activity logs", description = "Retrieves the current user's own activity logs")
    @GetMapping("/my-logs")
    public ResponseEntity<?> getMyLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<ActivityLog> logs = activityLogRepository.findByUsername(userName, pageable);
        return ResponseEntity.ok(logs);
    }

}
