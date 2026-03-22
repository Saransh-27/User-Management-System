package com.project.Ums.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String userName;
    private String email;
    private String password;
    private String currentPassword;
}
