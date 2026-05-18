package com.project.Ums.service;

import com.project.Ums.dto.TaskCreateRequest;
import com.project.Ums.dto.TaskResponse;
import com.project.Ums.dto.UpdateTaskStatusRequest;
import com.project.Ums.entity.Task;
import com.project.Ums.enums.TaskStatus;
import com.project.Ums.exception.FileStorageException;
import com.project.Ums.exception.FileUploadException;
import com.project.Ums.exception.TaskNotFoundException;
import com.project.Ums.exception.UserNotFoundException;
import com.project.Ums.mapper.TaskMapper;
import com.project.Ums.repository.TaskRepository;
import com.project.Ums.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TaskService {


//     tasks Service ----->

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;


    public TaskResponse createTaskWithFileAttach(TaskCreateRequest Request, MultipartFile file) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userRepository.findById(Request.getAssignedTo())
                .orElseThrow(() -> new UserNotFoundException("Assigned user not found with ID:" + Request.getAssignedTo()));
        Task task = TaskMapper.toEntity(Request);
        task.setCreatedAt(LocalDateTime.now());
        task.setStatus(TaskStatus.TODO);
        task.setCreatedBy(userName);
        // Handle optional file attachment (same as PublicService)
        if (file != null && !file.isEmpty()) {
            try {
                validateFile(file);

                byte[] fileContent = file.getBytes();
                String base64File = Base64.getEncoder().encodeToString(fileContent);
                String mimeType = file.getContentType();
                String dataUrl = "data:" + mimeType + ";base64," + base64File;

                task.setAttachmentFileName(file.getOriginalFilename());
                task.setAttachmentFile(dataUrl);

                log.info("File attachment added to task: {}", file.getOriginalFilename());
            } catch (IOException e) {
                log.error("Error reading file", e);
                throw new FileUploadException("Failed to process file. Please try again.");
            }
        }

        Task savedTask = taskRepository.save(task);
        com.project.Ums.entity.User assignedUser = userRepository.findById(task.getAssignedTo()).orElse(null);
        log.info("Task created: {} assigned to: {} by admin: {}", task.getTitle(), task.getAssignedTo(), userName);
        return TaskMapper.toResponse(savedTask, assignedUser);
    }

    public List<TaskResponse> getAllTasks(){
        List<Task> tasks = taskRepository.findAll();
        List<com.project.Ums.entity.User> allUsers = userRepository.findAll();
        java.util.Map<String, com.project.Ums.entity.User> userMap = allUsers.stream()
                .collect(java.util.stream.Collectors.toMap(com.project.Ums.entity.User::getId, u -> u));
                
        return tasks.stream()
                .map(t -> TaskMapper.toResponse(t, userMap.get(t.getAssignedTo())))
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
        com.project.Ums.entity.User assignedUser = userRepository.findById(task.getAssignedTo()).orElse(null);
        log.info("Task status updated: {} to {} by {}", task.getTitle(), request.getStatus(), userName);
        return TaskMapper.toResponse(task, assignedUser);
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

        List<com.project.Ums.entity.User> allUsers = userRepository.findAll();
        java.util.Map<String, com.project.Ums.entity.User> userMap = allUsers.stream()
                .collect(java.util.stream.Collectors.toMap(com.project.Ums.entity.User::getId, u -> u));

        return tasks.stream()
                .map(t -> TaskMapper.toResponse(t, userMap.get(t.getAssignedTo())))
                .toList();
    }

    public void deleteTaskById(String id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID:" + id));
        taskRepository.delete(task);
    }

    public TaskResponse getTaskById(String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID:" + id));
        com.project.Ums.entity.User assignedUser = userRepository.findById(task.getAssignedTo()).orElse(null);
        return TaskMapper.toResponse(task, assignedUser);
    }

    public TaskService() {
        try {
            Path uploadDir = Paths.get("uploads/task-files");
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new FileStorageException("Could not create upload directory", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > 10L * 1024 * 1024) { // 10MB limit
            throw new FileUploadException("File size should be less than 10MB");
        }

        String contentType = Objects.requireNonNull(file.getContentType()).toLowerCase();

        // Allow PNG, JPG, JPEG, PDF and other common types
        List<String> allowedTypes = List.of(
                "image/png",
                "image/jpeg",
                "image/jpg",
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "text/plain"
        );

        if (!allowedTypes.contains(contentType)) {
            throw new FileUploadException("File type not allowed. Allowed: PNG, JPG, PDF, DOC, XLS, TXT");
        }
    }


}
