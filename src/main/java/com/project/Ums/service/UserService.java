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
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addUser(UserRequestDto dto) {
        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        log.info("User created: {}", user.getUserName());
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public void updateUser(UserRequestDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userRepository.findByUserName(userName);
            userInDb.setUserName(dto.getUserName());
            userInDb.setPassword(passwordEncoder.encode(dto.getPassword()));
            userInDb.setEmail(dto.getEmail());
        userRepository.save(userInDb);
    }

    public void deleteUserById(String ID){
        SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(ID)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID:" + ID));
        userRepository.delete(user);
    }
}

