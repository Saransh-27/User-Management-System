package com.project.Ums.service;

import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.dto.UserResponseDto;
import com.project.Ums.entity.User;
import com.project.Ums.mapper.UserMapper;
import com.project.Ums.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;

    public void addUser(UserRequestDto dto) {
        User user = UserMapper.toEntity(dto);
        String rawPassword = dto.getPassword(); // Keep original before hashing
        user.setPassword(passwordEncoder.encode(rawPassword));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of("USER"));
        }
        user.setStatus("PENDING");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        // Create verification token and send verification email
        // Pass raw password so it can be included in welcome email after verification
        verificationService.createVerificationToken(user, rawPassword);
        
        log.info("User created: {} with roles: {}", user.getUserName(), user.getRoles());
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public List<UserResponseDto> searchUsers(String query, String searchType) {
        List<User> users;
        
        switch (searchType.toLowerCase()) {
            case "name":
            case "username":
                users = userRepository.findByUserNameContainingIgnoreCase(query);
                break;
            case "email":
                users = userRepository.findByEmailContainingIgnoreCase(query);
                break;
            case "id":
                Optional<User> user = userRepository.findById(query);
                users = user.isPresent() ? List.of(user.get()) : List.of();
                break;
            case "all":
            default:
                users = userRepository.findByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
                break;
        }
        
        return users.stream()
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

