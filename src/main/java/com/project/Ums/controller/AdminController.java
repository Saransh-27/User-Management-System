package com.project.Ums.controller;

import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.logging.LogActivity;
import com.project.Ums.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Management", description = "APIs for administrative user management operations")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "Add new user", description = "Creates a new user account and sends credentials via email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="CREATE_USER", description="Created a new user")
    @PostMapping("/add")
    public ResponseEntity<?> addUser(
            @Parameter(description = "User details for creating new account", required = true)
            @Valid @RequestBody UserRequestDto dto) {
        adminService.addUser(dto);
        return ResponseEntity.ok("User added successfully. Account creation info sent to user's email for verification.");
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user list"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="VIEW_ALL_USERS", description="Retrieved all users")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(adminService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Get user by ID", description = "Retrieves specific user details by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user details"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="VIEW_USER", description="Retrieved user by ID")
    @GetMapping("/User/{id}")
    public ResponseEntity<?> getUserById(
            @Parameter(description = "Unique identifier of the user", required = true)
            @PathVariable String id){
        return new ResponseEntity<>(adminService.getUserById(id), HttpStatus.OK);
    }

    @Operation(summary = "Delete user", description = "Permanently removes a user account from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="DELETE_USER", description="Deleted user account")
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUserById(
            @Parameter(description = "Unique identifier of the user to delete", required = true)
            @PathVariable String id){
        adminService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

}
