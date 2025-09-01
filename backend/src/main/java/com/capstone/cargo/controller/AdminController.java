package com.capstone.cargo.controller;

import com.capstone.cargo.dto.AuthLoginDto;
import com.capstone.cargo.dto.AuthRegistrationDto;
import com.capstone.cargo.dto.JwtResponseDto;
import com.capstone.cargo.role.Role;
import com.capstone.cargo.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AuthRegistrationDto authRegistrationDto) {
        String response = authService.register(authRegistrationDto, Role.ADMIN);
        log.info("Admin created successfully: {}", authRegistrationDto.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> loginUser(@RequestBody AuthLoginDto authLoginDto) {
        JwtResponseDto jwtResponseDto = authService.login(authLoginDto, Role.ADMIN);
        log.info("Admin logged in successfully: {}", authLoginDto.getUsername());
        return new ResponseEntity<>(jwtResponseDto, HttpStatus.OK);
    }
}
