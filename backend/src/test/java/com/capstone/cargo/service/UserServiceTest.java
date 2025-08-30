package com.capstone.cargo.service;

import com.capstone.cargo.dto.JwtResponseDto;
import com.capstone.cargo.dto.UserLoginDto;
import com.capstone.cargo.dto.UserRegistrationDto;
import com.capstone.cargo.jwt.JwtUtil;
import com.capstone.cargo.mapper.UserMapper;
import com.capstone.cargo.model.User;
import com.capstone.cargo.repository.UserRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    private User user;

    private UserRegistrationDto userRegisterDto;

    private UserLoginDto validUserLoginDto;

    private UserLoginDto invalidUserloginDto;

    @BeforeEach
    void setUp() {
        userRegisterDto = new UserRegistrationDto();
        userRegisterDto.setUsername("AlexaSiri00");
        userRegisterDto.setPassword("Alex@Sir!123");
        userRegisterDto.setEmailAddress("alexa.siri@gmail.com)");
        userRegisterDto.setFirstName("Alexa");
        userRegisterDto.setLastName("Siri");
        userRegisterDto.setCompanyName("OOCL");
        userRegisterDto.setUserRole("USER");

        user = new User();
        user.setId(1L);
        user.setUsername(userRegisterDto.getUsername());
        user.setPassword(userRegisterDto.getPassword());
        user.setEmailAddress(userRegisterDto.getEmailAddress());
        user.setUserRole(userRegisterDto.getUserRole());
        user.setFirstName(userRegisterDto.getFirstName());
        user.setLastName(userRegisterDto.getLastName());
        user.setCompanyName(userRegisterDto.getCompanyName());
        user.setCreatedAt("2023-05-20 13:45:30");
        user.setUpdatedAt("2023-05-20 13:45:30");

        validUserLoginDto = new UserLoginDto("AlexaSiri00", "Alex@Sir!123");
        invalidUserloginDto = new UserLoginDto("wrongUser", "wrongPassword");

    }

    @Test
    void test_givenContext_whenSpringContextIsLoaded_thenNoExceptions() {
        assertNotNull(userService);
        assertNotNull(userRepository);
        assertNotNull(passwordEncoder);
    }

    @Test
    void test_givenUser_whenGetAllUsers_thenReturnUserList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> userList = getUserList();

        assertNotNull(userList);
        assertEquals(1, userList.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void test_givenNoUsers_whenGetAllUsers_thenReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> userList = getUserList();

        assertNotNull(userList);
        assertTrue(userList.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    private List<User> getUserList() {
        return userService.getAllUsers();
    }

    @Test
    void test_givenUser_whenRegisterUser_thenReturnSuccessMessage() {
        when(userMapper.fromRegistrationDtoToModel(userRegisterDto)).thenReturn(user);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmailAddress(user.getEmailAddress())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        String registeredUser = userService.registerUser(userRegisterDto);

        assertNotNull(registeredUser);
        assertEquals("User created successfully!", registeredUser);
        verify(userRepository, times(1)).existsByUsername(user.getUsername());
        verify(userRepository, times(1)).existsByEmailAddress(user.getEmailAddress());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void test_givenExistingUsername_whenRegisterUser_thenReturnErrorMessage() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        String registeredUser = userService.registerUser(userRegisterDto);

        assertNotNull(registeredUser);
        assertEquals("Username already exists!", registeredUser);
        verify(userRepository, times(1)).existsByUsername(user.getUsername());
        verify(userRepository, never()).existsByEmailAddress(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void test_givenExistingEmailAddress_whenRegisterUser_thenReturnErrorMessage() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmailAddress(user.getEmailAddress())).thenReturn(true);

        String registeredUser = userService.registerUser(userRegisterDto);

        assertNotNull(registeredUser);
        assertEquals("Email already exists!", registeredUser);
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
        when(jwtUtil.generateToken("AlexaSiri00")).thenReturn("test-jwt-token");

        JwtResponseDto response = userService.loginUser(validUserLoginDto);

        assertNotNull(response);
        assertEquals("AlexaSiri00", response.getUsername());
        assertEquals("test-jwt-token", response.getToken());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateToken("AlexaSiri00");
    }

    @Test
    void test_givenInvalidCredentials_whenLoginUser_thenThrowRuntimeException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.loginUser(invalidUserloginDto));

        assertTrue(exception.getCause() instanceof BadCredentialsException);
        assertEquals("Invalid credentials", exception.getCause().getMessage());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void test_givenExistingUserId_whenGetUserById_thenReturnUser() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));

        User foundUser = userService.getUserById(1);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void test_givenNonExistingUserId_whenGetUserById_thenThrowException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.getUserById(1));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void test_givenExistingUsername_whenGetUserByUsername_thenReturnUser() {
        when(userRepository.findByUsername("AlexaSiri00")).thenReturn(Optional.ofNullable(user));

        User foundUser = userService.getUserByUsername("AlexaSiri00");

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("AlexaSiri00");
    }

    @Test
    void test_givenNonExistingUsername_whenGetUserByUsername_thenThrowException() {
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.getUserByUsername("nonExistingUser"));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("nonExistingUser");
    }

    @Test
    void test_givenExistingUserId_whenDeleteUser_thenUserIsDeleted() {
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        assertDoesNotThrow(() -> userService.deleteUser(1));

        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void test_givenNonExistingUserId_whenDeleteUser_thenThrowException() {
        when(userRepository.existsById(1)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(1));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).existsById(1);
        verify(userRepository, never()).deleteById(anyInt());
        }
}