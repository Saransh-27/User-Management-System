package com.project.Ums.entity;

import com.project.Ums.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    private String id;

    private String title;
    private String description;

    private TaskStatus status;

    private LocalDateTime dueDate;

    private String assignedTo; // userId
    private String createdBy;  // adminId

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
