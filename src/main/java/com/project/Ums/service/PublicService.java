package com.project.Ums.service;

import com.project.Ums.dto.TaskResponse;
import com.project.Ums.dto.UserProfileDto;
import com.project.Ums.dto.UserUpdateDto;
import com.project.Ums.entity.Task;
import com.project.Ums.entity.User;
import com.project.Ums.exception.FileUploadException;
import com.project.Ums.exception.InvalidPasswordException;
import com.project.Ums.exception.TaskNotFoundException;
import com.project.Ums.exception.UserNotFoundException;
import com.project.Ums.logging.ActivityLog;
import com.project.Ums.mapper.TaskMapper;
import com.project.Ums.mapper.UserMapper;
import com.project.Ums.logging.ActivityLogRepository;
import com.project.Ums.repository.TaskRepository;
import com.project.Ums.repository.UserRepository;
import com.project.Ums.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublicService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TaskRepository taskRepository;

    public UserProfileDto getUserProfile(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserMapper.toProfile(user);
    }

    public Map<String, Object> updateUser(String userName, UserUpdateDto dto) {
        User userInDb = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

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

        Map<String, Object> response = new HashMap<>();
        response.put("user", UserMapper.toProfile(updatedUser));

        if (usernameChanged) {
            String newToken = jwtUtil.generateToken(updatedUser.getUserName());
            response.put("token", newToken);
        }

        return response;
    }

    public Map<String, String> changePassword(String userName, String currentPassword, String newPassword) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

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
        return response;
    }

    public UserProfileDto uploadProfilePhoto(String userName, MultipartFile file) {
        try {
            User user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            if (file.isEmpty()) {
                throw new FileUploadException("Please select a file to upload");
            }

            if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
                throw new FileUploadException("Only image files are allowed");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                throw new FileUploadException("File size should be less than 5MB");
            }

            byte[] fileContent = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(fileContent);
            String mimeType = file.getContentType();
            String dataUrl = "data:" + mimeType + ";base64," + base64Image;

            user.setProfilePhoto(dataUrl);
            userRepository.save(user);

            log.info("Profile photo uploaded and stored in database for user: {}", userName);
            return UserMapper.toProfile(user);
        } catch (IOException e) {
            log.error("Error uploading profile photo", e);
            throw new FileUploadException("Failed to upload profile photo. Please try again.");
        }
    }

    public Map<String, String> deleteUser(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account deleted successfully");
        log.info("User {} deleted their account", userName);
        return response;
    }

    public Page<ActivityLog> getMyLogs(String userName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return activityLogRepository.findByUsername(userName, pageable);
    }

    public List<TaskResponse> getMyTasks(String userId) {
        List<Task> tasks = taskRepository.findByAssignedTo(userId);
        if (tasks == null || tasks.isEmpty()) {
            throw new TaskNotFoundException("No tasks found for user: " + userId);
        }
        User assignedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        return tasks.stream()
                .map(task -> TaskMapper.toResponse(task, assignedUser))
                .collect(Collectors.toList());
    }

//    AdminService updateTaskStatus method is used instead to this : ---> bcz same work
//    public TaskResponse updateTaskStatus(String userName, String taskId, String newStatus) {
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
//        if (!task.getAssignedTo().equals(userName)) {
//            throw new TaskNotFoundException("Task not assigned to user: " + userName);
//        }
//        task.setStatus(TaskStatus.valueOf(newStatus));
//        taskRepository.save(task);
//        log.info("User {} updated task {} status to {}", userName, task.getTitle(), newStatus);
//        return TaskMapper.toResponse(task);
//    }
}
