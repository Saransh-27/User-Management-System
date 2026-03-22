package com.project.Ums.mapper;
import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.dto.UserResponseDto;
import com.project.Ums.dto.UserProfileDto;
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
                .status(user.getStatus())
                .profilePhoto(user.getProfilePhoto())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // Entity → User Profile (no password exposed)
    public static UserProfileDto toProfile(User user) {
        return UserProfileDto.builder()
                .id(user.getId().toString())
                .userName(user.getUserName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .status(user.getStatus())
                .profilePhoto(user.getProfilePhoto())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
