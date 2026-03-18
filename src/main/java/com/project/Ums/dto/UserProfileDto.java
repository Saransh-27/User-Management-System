package com.project.Ums.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private String id;
    private String userName;
    private String email;
    private String password;
    private List<String> roles = new ArrayList<>();
    private String status;
    private String profilePhoto;
}
