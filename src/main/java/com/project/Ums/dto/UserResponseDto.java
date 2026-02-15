package com.project.Ums.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String id;
    private String userName;
    private String email;
}
