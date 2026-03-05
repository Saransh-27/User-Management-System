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
