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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthMapper authMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    private User user;

    private AuthRegistrationDto authRegistrationDto;

    private AuthLoginDto validUserLoginDto, invalidUserloginDto;

    @BeforeEach
    void setUp() {
        authRegistrationDto = new AuthRegistrationDto();
        authRegistrationDto.setUsername("AlexaSiri00");
        authRegistrationDto.setPassword("Alex@Sir!123");
        authRegistrationDto.setEmailAddress("alexa.siri@gmail.com)");
        authRegistrationDto.setUserRole("USER");

        user = new User();
        user.setId(1L);
        user.setUsername(authRegistrationDto.getUsername());
        user.setPassword(authRegistrationDto.getPassword());
        user.setEmailAddress(authRegistrationDto.getEmailAddress());
        user.setRole(Role.USER);

        validUserLoginDto = new AuthLoginDto("AlexaSiri00", "Alex@Sir!123");
        invalidUserloginDto = new AuthLoginDto("wrongUser", "wrongPassword");
    }

    @Test
    void test_givenContext_whenSpringContextIsLoaded_thenNoExceptions() {
        assertNotNull(authService);
        assertNotNull(userRepository);
        assertNotNull(passwordEncoder);
        assertNotNull(authMapper);
        assertNotNull(authenticationManager);
        assertNotNull(jwtUtil);
    }


    @Test
    void test_givenUser_whenRegisterUser_thenReturnSuccessMessage() {
        when(authMapper.fromRegistrationDtoToUser(authRegistrationDto, Role.USER)).thenReturn(user);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmailAddress(user.getEmailAddress())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String registeredUser = authService.register(authRegistrationDto, Role.USER);

        assertNotNull(registeredUser);
        assertEquals("Created successfully!", registeredUser);
        verify(userRepository, times(1)).existsByUsername(user.getUsername());
        verify(userRepository, times(1)).existsByEmailAddress(user.getEmailAddress());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void test_givenExistingUsername_whenRegisterUser_thenReturnErrorMessage() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> authService.register(authRegistrationDto, Role.USER));

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(user.getUsername());
        verify(userRepository, never()).existsByEmailAddress(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void test_givenExistingEmailAddress_whenRegisterUser_thenReturnErrorMessage() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmailAddress(user.getEmailAddress())).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> authService.register(authRegistrationDto, Role.USER));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(user.getUsername());
        verify(userRepository, times(1)).existsByEmailAddress(user.getEmailAddress());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void test_givenValidCredentials_whenLoginUser_thenReturnJwtResponseDto() {
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("AlexaSiri00");
        when(userRepository.findByUsername("AlexaSiri00")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("AlexaSiri00", "USER", null))
                .thenReturn("test-jwt-token");

        JwtResponseDto response = authService.login(validUserLoginDto);

        assertNotNull(response);
        assertEquals("AlexaSiri00", response.getUsername());
        assertEquals("test-jwt-token", response.getToken());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("AlexaSiri00");
        verify(jwtUtil, times(1)).generateToken("AlexaSiri00", "USER", null);
    }

    @Test
    void test_givenInvalidCredentials_whenLoginUser_thenThrowExceptionMessage() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(invalidUserloginDto)
        );

        assertEquals("Invalid username or password", exception.getMessage());
        assertTrue(exception.getCause() instanceof BadCredentialsException);

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(anyString(), anyString(), any());
    }

    @Test
    void test_givenInvalidUsername_whenLoginUser_thenThrowExceptionMessage() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(new AuthLoginDto("wrongUser", "correctPassword"))
        );

        assertEquals("Invalid username or password", exception.getMessage());
        assertTrue(exception.getCause() instanceof BadCredentialsException);

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(anyString(), anyString(), any());
    }

    @Test
    void test_givenInvalidPassword_whenLoginUser_thenThrowExceptionMessage() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(new AuthLoginDto("correctUsername", "wrongPassword"))
        );

        assertEquals("Invalid username or password", exception.getMessage());
        assertTrue(exception.getCause() instanceof BadCredentialsException);

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(anyString(), anyString(), any());
    }

    @Test
    void test_givenNullUsername_whenLoginUser_thenThrowExceptionMessage() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(new AuthLoginDto(null, "somePassword"))
        );

        assertEquals("Invalid username or password", exception.getMessage());
        assertTrue(exception.getCause() instanceof BadCredentialsException);

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(anyString(), anyString(), any());
    }

    @Test
    void test_givenNullPassword_whenLoginUser_thenThrowExceptionMessage() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(new AuthLoginDto("someUsername", null))
        );

        assertEquals("Invalid username or password", exception.getMessage());
        assertTrue(exception.getCause() instanceof BadCredentialsException);

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(anyString(), anyString(), any());
    }

}