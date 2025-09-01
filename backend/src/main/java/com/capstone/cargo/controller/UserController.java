package com.capstone.cargo.controller;

import com.capstone.cargo.dto.AuthLoginDto;
import com.capstone.cargo.dto.AuthRegistrationDto;
import com.capstone.cargo.dto.JwtResponseDto;
import com.capstone.cargo.model.User;
import com.capstone.cargo.role.Role;
import com.capstone.cargo.service.AuthService;
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

    private final AuthService authService;

    private final UserService userService;

    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
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

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        log.info("User updated successfully: {}", user.getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
