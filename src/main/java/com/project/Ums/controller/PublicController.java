package com.project.Ums.controller;

import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.entity.User;
import com.project.Ums.logging.LogActivity;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    
    private static final String UPLOAD_DIR = "uploads/profile-photos/";

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
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new ResponseEntity<>(UserMapper.toProfile(user), HttpStatus.OK);
    }

    @Operation(summary = "Update user profile", description = "Updates the current authenticated user's profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="UPDATE_PROFILE", description="Updated own profile")
    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(
            @Parameter(description = "Updated user profile information", required = true)
            @RequestBody UserRequestDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
            userInDb.setUserName(dto.getUserName());
            userInDb.setPassword(passwordEncoder.encode(dto.getPassword()));
            userInDb.setEmail(dto.getEmail());
        User updatedUser = userRepository.save(userInDb);
        return new ResponseEntity<>(UserMapper.toProfile(updatedUser), HttpStatus.ACCEPTED);
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
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
                return ResponseEntity.badRequest().body("File size should be less than 5MB");
            }

            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : "";
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            
            // Save file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            // Update user profile with photo path
            String photoUrl = "http://localhost:8080/public/profile-photos/" + uniqueFilename;
            user.setProfilePhoto(photoUrl);
            userRepository.save(user);

            return ResponseEntity.ok(UserMapper.toProfile(user));
        } catch (IOException e) {
            log.error("Error uploading profile photo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload profile photo");
        }
    }

    @Operation(summary = "Get profile photo", description = "Serves profile photo by filename")
    @GetMapping("/profile-photos/{filename}")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }
            
            byte[] fileContent = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            
            return ResponseEntity.ok()
                    .header("Content-Type", contentType != null ? contentType : "image/jpeg")
                    .body(fileContent);
        } catch (IOException e) {
            log.error("Error serving profile photo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
        userRepository.deleteUserByUserName(userName);
        return ResponseEntity.ok("User Deleted successfully");
    }

}
