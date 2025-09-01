package com.capstone.cargo.service;

import com.capstone.cargo.exception.ResourceAlreadyExistsException;
import com.capstone.cargo.exception.ResourceNotFoundException;
import com.capstone.cargo.exception.UsersNotFoundException;
import com.capstone.cargo.model.User;
import com.capstone.cargo.repository.UserRepository;
import com.capstone.cargo.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
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

    private User user, user2, updatedInfo;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("AlexaSiri00");
        user.setPassword("Alex@Sir!123");
        user.setEmailAddress("alexa.siri@gmail.com");
        user.setRole(Role.USER);

        user2 = new User();
        user.setId(2L);
        user.setUsername("ADMIN1");
        user.setPassword("ADMIN@123");
        user.setEmailAddress("admin@gmail.com");
        user.setRole(Role.ADMIN);

        updatedInfo = new User();
        updatedInfo.setFirstName("UpdatedAlexa");
        updatedInfo.setLastName("UpdatedSiri");
        updatedInfo.setUsername("UpdatedUsername");
        updatedInfo.setPassword("NewPass@123");
        updatedInfo.setEmailAddress("alexa@siri.com");
    }

    @Test
    void test_givenUser_whenGetAllUsers_thenReturnUserList() {
        when(userRepository.findAll()).thenReturn(List.of(user, user2));

        List<User> userList = userService.getAllUsers();

        assertNotNull(userList);
        assertEquals(2, userList.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void test_givenNoUsers_whenGetAllUsers_thenReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        UsersNotFoundException exception = assertThrows(
                UsersNotFoundException.class,
                () -> userService.getAllUsers()
        );

        assertEquals("No users found", exception.getMessage());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void test_givenExistingUserId_whenGetUserById_thenReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void test_givenNonExistingUserId_whenGetUserById_thenThrowException() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(3L));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(3L);
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

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByUsername("nonExistingUser"));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("nonExistingUser");
    }

    @Test
    void test_givenExistingUser_whenUpdateUser_thenReturnUpdatedUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.existsByUsername("UpdatedUsername")).thenReturn(false);
        when(userRepository.existsByEmailAddress("alexa@siri.com")).thenReturn(false);
        when(passwordEncoder.encode("NewPass@123")).thenReturn("EncodedNewPass@123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(1L, updatedInfo);

        assertNotNull(result);
        assertEquals("UpdatedAlexa", result.getFirstName());
        assertEquals("UpdatedSiri", result.getLastName());
        assertEquals("UpdatedUsername", result.getUsername());
        assertEquals("EncodedNewPass@123", result.getPassword());
        assertEquals("alexa@siri.com", result.getEmailAddress());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).existsByUsername("UpdatedUsername");
        verify(userRepository, times(1)).existsByEmailAddress("alexa@siri.com");
        verify(passwordEncoder, times(1)).encode("NewPass@123");
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    void test_givenNonExistingUser_whenUpdateUser_thenThrowException() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(3L, updatedInfo));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(3L);
        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).existsByEmailAddress(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void test_givenExistingUsername_whenUpdateUser_thenThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.existsByUsername("UpdatedUsername")).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> userService.updateUser(1L, updatedInfo));

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).existsByUsername("UpdatedUsername");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void test_givenExistingEmail_whenUpdateUser_thenThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.existsByEmailAddress("alexa@siri.com")).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> userService.updateUser(1L, updatedInfo));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).existsByEmailAddress("alexa@siri.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void test_givenNoFieldsToUpdate_whenUpdateUser_thenReturnUnchangedUser() {
        User emptyUpdate = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(1L, emptyUpdate);
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getEmailAddress(), result.getEmailAddress());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).existsByEmailAddress(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void test_givenUsernameIsSame_whenUpdateUser_thenReturnUnchangedUser() {
        user.setUsername("AlexaSiri00");
        updatedInfo.setUsername("AlexaSiri00");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("AlexaSiri00")).thenReturn(true);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, updatedInfo);

        assertNotNull(result);
        assertEquals("AlexaSiri00", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).existsByUsername("AlexaSiri00");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void test_givenEmailIsSame_whenUpdateUser_thenReturnUnchangedUser() {
        user.setEmailAddress("alexa@siri.com");
        updatedInfo.setUsername("alexa@siri.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAddress("alexa@siri.com")).thenReturn(true);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, updatedInfo);

        assertNotNull(result);
        assertEquals("alexa@siri.com", result.getEmailAddress());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).existsByEmailAddress("alexa@siri.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void test_givenExistingUserId_whenDeleteUser_thenUserIsDeleted() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void test_givenNonExistingUserId_whenDeleteUser_thenThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(1L));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).existsById(1l);
        verify(userRepository, never()).deleteById(anyLong());
    }
}