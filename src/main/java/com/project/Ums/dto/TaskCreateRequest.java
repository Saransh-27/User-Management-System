package com.project.Ums.dto;

import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate; //YYYY-MM-DDTHH:MM:SS
    private String assignedTo; //userId
}