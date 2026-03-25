package com.project.Ums.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private String error;
    private LocalDateTime timestamp;
    private Map<String, String> errors;
    
    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }
}
