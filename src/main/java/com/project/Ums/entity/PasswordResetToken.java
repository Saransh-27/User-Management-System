package com.project.Ums.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "password_reset_tokens")
public class PasswordResetToken {
    @Id
    private String id;

    @Indexed(unique = true)
    @Field("token")
    private String token;

    @Field("user_id")
    private String userId; // Store user ID as String since MongoDB uses String IDs

    @Field("expiry_date")
    private LocalDateTime expiryDate;

    @Field("used")
    private Boolean used = false;

    @Field("created_at")
    private LocalDateTime createdAt;

    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
