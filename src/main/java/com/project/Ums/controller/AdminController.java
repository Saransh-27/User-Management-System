package com.project.Ums.controller;

import com.project.Ums.dto.TaskCreateRequest;
import com.project.Ums.dto.UpdateTaskStatusRequest;
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
import java.util.Map;

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
        String verificationLink = adminService.addUser(dto);
        
        Map<String, Object> response = new java.util.LinkedHashMap<>();
        response.put("message", "User created successfully");
        response.put("userName", dto.getUserName());
        response.put("email", dto.getEmail());
        response.put("verificationLink", verificationLink);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users with essential information")
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

    @Operation(summary = "Search users", description = "Search users by name, email, or other criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved search results"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin only"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @LogActivity(action="SEARCH_USERS", description="Searched for users")
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @Parameter(description = "Search query - can be name, email, or user ID")
            @RequestParam("query") String query,
            @Parameter(description = "Search type - name, email, or all")
            @RequestParam(value = "searchType", defaultValue = "all") String searchType) {
        return new ResponseEntity<>(adminService.searchUsers(query, searchType), HttpStatus.OK);
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
        return adminService.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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

    /**
     * Task controllers
     */
    @PostMapping("/create-task")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskCreateRequest Request) {
        return new ResponseEntity<>(adminService.createTask(Request), HttpStatus.CREATED);
    }

    @GetMapping("/all-tasks")
    public ResponseEntity<?> getAllTasks() {
        return new ResponseEntity<>(adminService.getAllTasks(), HttpStatus.OK);
    }

    @PutMapping("/put-task/{taskId}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable String taskId, @RequestBody UpdateTaskStatusRequest request) {
        return new ResponseEntity<>(adminService.updateTaskStatus(taskId, request), HttpStatus.OK);
    }

    @DeleteMapping("/del-task/{taskId}")
    public ResponseEntity<?> deleteTaskById(@PathVariable String taskId) {
        adminService.deleteTaskById(taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @GetMapping("/task/search")
    public ResponseEntity<?> searchTasks(
            @Parameter(description = "Search query - can be task title, assigned user ID, or task ID")
            @RequestParam("query") String query,
            @Parameter(description = "Search type - title, assignedTo, or all")
            @RequestParam(value = "searchType", defaultValue = "all") String searchType) {
        return new ResponseEntity<>(adminService.searchTasks(query, searchType), HttpStatus.OK);
    }
}
