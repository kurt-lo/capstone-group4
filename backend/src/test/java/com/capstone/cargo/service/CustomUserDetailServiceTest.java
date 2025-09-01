package com.capstone.cargo.service;

import com.capstone.cargo.model.User;
import com.capstone.cargo.repository.UserRepository;
import com.capstone.cargo.role.Role;
import com.capstone.cargo.security.CustomerUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("caoal4");
        user.setPassword("password123");
        user.setRole(Role.USER);
    }

    @Test
    void test_givenExistingUser_whenLoadUserByUsername_thenReturnUserDetails() {
        when(userRepository.findByUsername("caoal4")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailService.loadUserByUsername("caoal4");

        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CustomerUserDetails);
        assertEquals("caoal4", userDetails.getUsername());
        verify(userRepository, times(1)).findByUsername("caoal4");
    }

    @Test
    void test_givenNonExistingUser_whenLoadUserByUsername_thenThrowUsernameNotFoundException() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailService.loadUserByUsername("unknownUser")
        );

        assertEquals("No User or Admin found with username: unknownUser", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("unknownUser");
    }
}