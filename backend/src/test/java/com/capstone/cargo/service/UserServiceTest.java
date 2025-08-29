package com.capstone.cargo.service;

import com.capstone.cargo.model.User;
import com.capstone.cargo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUserRole("USER");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setCompanyName("OOCL");
        user.setEmail("oocl@gmail.com");
        user.setUsername("username001");
        user.setPassword("Longpassword123");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.existsUserByUsername(user.getUsername())).thenReturn(false);
        when(passwordEncoder.encode("Longpassword123")).thenReturn("hashedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        assertEquals("username001", savedUser.getUsername());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowErrorIfEmailExists() {
        when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user);
        });

        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void shouldThrowErrorIfUsernameExists() {
        when(userRepository.existsUserByUsername(user.getUsername())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user);
        });

        assertEquals("Username already exists", ex.getMessage());
    }
}