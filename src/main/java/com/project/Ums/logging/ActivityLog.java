package com.project.Ums.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
@Document(collection = "activity_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {

    @Id
    private String id;

    private String userId;
    private String username;
    private String action;
    private String methodName;
    private String description;

    private String ipAddress;
    private String userAgent;

    private boolean success;
    private String errorMessage;

    private Instant timestamp;
}