package com.project.Ums.mapper;

import com.project.Ums.dto.TaskCreateRequest;
import com.project.Ums.dto.TaskResponse;
import com.project.Ums.entity.Task;

public class TaskMapper {
    private TaskMapper() {}

    public static Task toEntity(TaskCreateRequest dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setAssignedTo(dto.getAssignedTo());
        return task;
    }

    // Entity → Response
    public static TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .assignedTo(task.getAssignedTo())
                .createdBy(task.getCreatedBy())
                .build();
    }
}
