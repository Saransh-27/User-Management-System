package com.project.Ums.dto;

import com.project.Ums.dto.UserProfileDto;

public class LoginResponse {
    private String token;
    private UserProfileDto user;

    public LoginResponse(String token, UserProfileDto user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserProfileDto getUser() {
        return user;
    }

    public void setUser(UserProfileDto user) {
        this.user = user;
    }
}
