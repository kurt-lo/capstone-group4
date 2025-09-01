package com.capstone.cargo.service;

import com.capstone.cargo.dto.AuthLoginDto;
import com.capstone.cargo.dto.AuthRegistrationDto;
import com.capstone.cargo.dto.JwtResponseDto;
import com.capstone.cargo.exception.InvalidCredentialsException;
import com.capstone.cargo.exception.ResourceAlreadyExistsException;
import com.capstone.cargo.jwt.JwtUtil;
import com.capstone.cargo.mapper.AuthMapper;
import com.capstone.cargo.model.User;
import com.capstone.cargo.repository.UserRepository;
import com.capstone.cargo.role.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       AuthMapper authMapper,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authMapper = authMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public String register(AuthRegistrationDto authRegistrationDto, Role role) {

        if (userRepository.existsByUsername(authRegistrationDto.getUsername())) {
            log.error("Registration failed: Username already exists.");
            throw new ResourceAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmailAddress(authRegistrationDto.getEmailAddress())) {
            log.error("Registration failed: Email already exists.");
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        User user = authMapper.fromRegistrationDtoToUser(authRegistrationDto, role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userRepository.save(user);

        log.info("Registered successfully: {} as {}", user.getUsername(), user.getRole());
        return "Created successfully!";
    }

    public JwtResponseDto login(AuthLoginDto authLoginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authLoginDto.getUsername(),
                            authLoginDto.getPassword()
                    )
            );

            String username = authentication.getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

            String token = jwtUtil.generateToken(
                    username,
                    user.getRole().name(),
                    null
            );

            return new JwtResponseDto(username, token);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid username or password", e);
        }
    }

}