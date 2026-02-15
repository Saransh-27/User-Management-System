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
        return user; // password handled in service
    }

    // Entity → Response
    public static UserResponseDto toResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId().toString())
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();
    }
}
