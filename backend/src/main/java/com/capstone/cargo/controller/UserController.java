package com.capstone.cargo.controller;


import com.capstone.cargo.dto.JwtResponseDto;
import com.capstone.cargo.dto.AuthLoginDto;
import com.capstone.cargo.dto.AuthRegistrationDto;
import com.capstone.cargo.model.User;
import com.capstone.cargo.role.Role;
import com.capstone.cargo.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, User!");
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = authService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthRegistrationDto authRegistrationDto) {
        String response = authService.register(authRegistrationDto, Role.USER);
        log.info("User created successfully: {}", authRegistrationDto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> loginUser(@RequestBody AuthLoginDto authLoginDto) {
        JwtResponseDto jwtResponseDto = authService.login(authLoginDto, Role.USER);
        log.info("User logged in successfully: {}", authLoginDto.getUsername());
        return new ResponseEntity<>(jwtResponseDto, HttpStatus.OK);
    }
}
