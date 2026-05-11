package com.project.Ums.dto;

import com.project.Ums.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private String assignedTo;
    private String createdBy;
    private LocalDateTime dueDate;
}
