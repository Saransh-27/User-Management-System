package com.project.Ums.controller;

import com.project.Ums.dto.UserRequestDto;
import com.project.Ums.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/delete-user/{ID}")
    public ResponseEntity<?> deleteUserById(@PathVariable String ID){
        userService.deleteUserById(ID);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestDto dto){
        userService.updateUser(dto);
        return ResponseEntity.ok("User updated successfully");
    }
}
