package com.project.Ums.service;

import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.dto.UserResponseDto;
import com.project.Ums.entity.User;
import com.project.Ums.mapper.UserMapper;
import com.project.Ums.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addUser(@RequestBody UserRequestDto dto) {
        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of("USER"));
        }
        userRepository.save(user);
        log.info("User created: {} with roles: {}", user.getUserName(), user.getRoles());
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public Optional<UserResponseDto> getUserById(String id){
        return userRepository.findById(id)
                .map(UserMapper::toResponse);
    }

    public void deleteUserById(String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID:" + id));
        userRepository.delete(user);
    }
}

