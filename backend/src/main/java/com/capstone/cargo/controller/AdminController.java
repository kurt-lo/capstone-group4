package com.capstone.cargo.controller;

import com.capstone.cargo.dto.AuthLoginDto;
import com.capstone.cargo.dto.AuthRegistrationDto;
import com.capstone.cargo.dto.JwtResponseDto;
import com.capstone.cargo.dto.UserDTO;
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
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final AuthService authService;

    private final UserService userService;

    public AdminController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
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

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        UserDTO user = userService.updateUser(id, updatedUser);
        log.info("User updated successfully: {}", user.getFirstName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        log.info("User deleted successfully with id: {}", id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }
    
}
