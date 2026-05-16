package com.project.Ums.service;

import com.project.Ums.dto.TaskCreateRequest;
import com.project.Ums.dto.TaskResponse;
import com.project.Ums.dto.UpdateTaskStatusRequest;
import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.dto.UserResponseDto;
import com.project.Ums.entity.Task;
import com.project.Ums.entity.User;
import com.project.Ums.enums.TaskStatus;
import com.project.Ums.exception.UserNotFoundException;
import com.project.Ums.exception.TaskNotFoundException;
import com.project.Ums.mapper.TaskMapper;
import com.project.Ums.mapper.UserMapper;
import com.project.Ums.repository.TaskRepository;
import com.project.Ums.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;
    @Autowired
    private TaskRepository taskRepository;

    public String addUser(UserRequestDto dto) {
        User user = UserMapper.toEntity(dto);
        String rawPassword = dto.getPassword(); // Keep original before hashing
        user.setPassword(passwordEncoder.encode(rawPassword));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of("USER"));
        }
        user.setStatus("PENDING");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        // Create verification token and send verification email
        // Pass raw password so it can be included in welcome email after verification
        // Returns the verification link for display in the API response
        String verificationLink = verificationService.createVerificationToken(user, rawPassword);
        
        log.info("User created: {} with roles: {}", user.getUserName(), user.getRoles());
        return verificationLink;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public List<UserResponseDto> searchUsers(String query, String searchType) {
        List<User> users;
        
        switch (searchType.toLowerCase()) {
            case "name":
            case "username":
                users = userRepository.findByUserNameContainingIgnoreCase(query);
                break;
            case "email":
                users = userRepository.findByEmailContainingIgnoreCase(query);
                break;
            case "id":
                Optional<User> user = userRepository.findById(query);
                users = user.isPresent() ? List.of(user.get()) : List.of();
                break;
            case "all":
            default:
                users = userRepository.findByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
                break;
        }
        
        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public Optional<UserResponseDto> getUserById(String id){
        return userRepository.findById(id)
                .map(UserMapper::toResponse);
    }

    public void deleteUserById(String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID:" + id));
        userRepository.delete(user);
    }

    public void deleteTaskById(String id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID:" + id));
        taskRepository.delete(task);
    }


//     tasks Service ----->

    public TaskResponse createTask(TaskCreateRequest Request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userRepository.findById(Request.getAssignedTo())
                .orElseThrow(() -> new UserNotFoundException("Assigned user not found with ID:" + Request.getAssignedTo()));
        Task task = TaskMapper.toEntity(Request);
        task.setCreatedAt(LocalDateTime.now());
        task.setStatus(TaskStatus.TODO);
        task.setCreatedBy(userName);
        Task savedTask = taskRepository.save(task);
        log.info("Task created: {} assigned to: {} by admin: {}", task.getTitle(), task.getAssignedTo(), userName);
        return TaskMapper.toResponse(savedTask);
    }

    public List<TaskResponse> getAllTasks(){
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    public TaskResponse updateTaskStatus(String id, UpdateTaskStatusRequest request){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID:" + id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        task.setStatus(request.getStatus());
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);
        log.info("Task status updated: {} to {} by {}", task.getTitle(), request.getStatus(), userName);
        return TaskMapper.toResponse(task);
    }

    public List<TaskResponse> searchTasks(String query, String searchType) {
        List<Task> tasks;
        switch (searchType.toUpperCase()) {
            case "TODO":
                tasks = taskRepository.findByStatusContaining("TODO");
                break;
            case "IN_PROGRESS":
                tasks = taskRepository.findByStatusContaining("IN_PROGRESS");;
                break;
            case "COMPLETED":
                tasks = taskRepository.findByStatusContaining("COMPLETED");
                break;
            case "ALL":
            default:
                tasks = taskRepository.findByStatusContaining("TODO");
                 tasks.addAll(taskRepository.findByStatusContaining("IN_PROGRESS"));
                 tasks.addAll(taskRepository.findByStatusContaining("COMPLETED"));
                break;
        }

        return tasks.stream()
                .map(TaskMapper::toResponse)
                .toList();
    }
}

