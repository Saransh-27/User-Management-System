package com.project.Ums.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private int status;
    private T data;
    private LocalDateTime timestamp;
    private boolean success;
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(200)
                .data(data)
                .timestamp(LocalDateTime.now())
                .success(true)
                .build();
    }
    
    public static <T> ApiResponse<T> success(String message) {
        return success(message, null);
    }
    
    public static <T> ApiResponse<T> error(String message, int status) {
        return ApiResponse.<T>builder()
                .message(message)
                .status(status)
                .data(null)
                .timestamp(LocalDateTime.now())
                .success(false)
                .build();
    }
}
