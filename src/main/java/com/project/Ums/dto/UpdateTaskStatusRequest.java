package com.project.Ums.dto;

import com.project.Ums.enums.TaskStatus;
import lombok.Data;

@Data
public class UpdateTaskStatusRequest {
    private TaskStatus status;
}
