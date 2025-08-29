package com.capstone.cargo.service;

import com.capstone.cargo.dto.JwtResponseDto;
import com.capstone.cargo.dto.UserLoginDto;
import com.capstone.cargo.dto.UserRegistrationDto;
import com.capstone.cargo.jwt.JwtUtil;
import com.capstone.cargo.mapper.UserMapper;
import com.capstone.cargo.model.User;
import com.capstone.cargo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
//@RequiredArgsConstructor // used to generate a constructor with required arguments
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String registerUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            log.error("User registration failed: Username already exists.");
            return "Username already exists!";
        }

        if (userRepository.existsByEmailAddress(userRegistrationDto.getEmailAddress())) {
            log.error("User registration failed: Email already exists.");
            return "Email already exists!";
        }

        User user = userMapper.fromRegistrationDtoToModel(userRegistrationDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());
        return "User created successfully!";
    }

    public JwtResponseDto loginUser(UserLoginDto userLoginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));

            String token = jwtUtil.generateToken(authentication.getName());
            return new JwtResponseDto(token);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }
}