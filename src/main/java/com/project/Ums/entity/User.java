package com.project.Ums.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    @Indexed
    private String userName;
    @Indexed(unique = true)
    private String email;
    private String password;
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    @Indexed
    private String status;
    @Indexed
    private String otp;
    private LocalDateTime otpExpiry;
    private String profilePhoto; // Base64 encoded image data
    private LocalDateTime createdAt;
}
