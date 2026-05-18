package com.project.Ums.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    private String userName;
    private String email;
    private String password;
}
