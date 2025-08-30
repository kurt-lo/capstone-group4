package com.capstone.cargo.controller;

import com.capstone.cargo.dto.JwtResponseDto;
import com.capstone.cargo.dto.UserLoginDto;
import com.capstone.cargo.dto.UserRegistrationDto;
import com.capstone.cargo.model.User;
import com.capstone.cargo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, User!");
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();

            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch users");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        String response = userService.registerUser(userRegistrationDto);
        if (response.equals("User registered successfully!")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto userLoginDto) {
        try {
            JwtResponseDto jwtResponseDto = userService.loginUser(userLoginDto);
            log.info("User logged in successfully: {}", userLoginDto.getUsername());
            return new ResponseEntity<>(jwtResponseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Login failed: {}", e.getMessage());
            return new ResponseEntity<>("Invalid username or password", HttpStatus.BAD_REQUEST);
        }
    }
}
