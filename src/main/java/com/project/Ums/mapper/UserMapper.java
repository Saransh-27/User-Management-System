package com.project.Ums.mapper;
import com.project.Ums.dto.*;
import com.project.Ums.entity.User;

public class UserMapper {

    private UserMapper() {}

    // Request → Entity
    public static User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setRoles(dto.getRoles());
        return user; // password handled in service
    }

    // Entity → Response
    public static UserResponseDto toResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId().toString())
                .userName(user.getUserName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
