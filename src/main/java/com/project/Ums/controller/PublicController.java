package com.project.Ums.controller;

import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.dto.UserProfileDto;
import com.project.Ums.entity.User;
import com.project.Ums.mapper.UserMapper;
import com.project.Ums.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/view-profile")
    public ResponseEntity<?> userByUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new ResponseEntity<>(UserMapper.toProfile(user), HttpStatus.OK);
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
            userInDb.setUserName(dto.getUserName());
            userInDb.setPassword(passwordEncoder.encode(dto.getPassword()));
            userInDb.setEmail(dto.getEmail());
        User updatedUser = userRepository.save(userInDb);
        return new ResponseEntity<>(UserMapper.toProfile(updatedUser), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userRepository.deleteUserByUserName(userName);
        return ResponseEntity.ok("User Deleted successfully");
    }

}
