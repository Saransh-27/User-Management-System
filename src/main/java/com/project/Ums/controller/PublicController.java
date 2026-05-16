package com.project.Ums.controller;

import com.project.Ums.dto.UpdateTaskStatusRequest;
import com.project.Ums.dto.UserProfileDto;
import com.project.Ums.dto.UserUpdateDto;
import com.project.Ums.entity.User;
import com.project.Ums.exception.UserNotFoundException;
import com.project.Ums.logging.LogActivity;
import com.project.Ums.repository.UserRepository;
import com.project.Ums.service.AdminService;
import com.project.Ums.service.PublicService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "User Profile", description = "APIs for user profile management and self-service operations")
@SecurityRequirement(name = "bearerAuth")
public class PublicController {

    @Autowired
    private PublicService publicService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminService adminService;
    
    
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
        UserProfileDto profile = publicService.getUserProfile(userName);
        return new ResponseEntity<>(profile, HttpStatus.OK);
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
        Map<String, Object> response = publicService.updateUser(userName, dto);
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
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");
        Map<String, String> response = publicService.changePassword(userName, currentPassword, newPassword);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        UserProfileDto response = publicService.uploadProfilePhoto(userName, file);
        return ResponseEntity.ok(response);
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
        Map<String, String> response = publicService.deleteUser(userName);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get my activity logs", description = "Retrieves the current user's own activity logs")
    @GetMapping("/my-logs")
    public ResponseEntity<?> getMyLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        var logs = publicService.getMyLogs(userName, page, size);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/view-task")
    public ResponseEntity<?> viewTask() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        var tasks = publicService.getMyTasks(user.getId());
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/update-task/{taskId}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable String taskId, @RequestBody UpdateTaskStatusRequest request) {
       return new ResponseEntity<>(adminService.updateTaskStatus(taskId, request), HttpStatus.OK);
    }
}
