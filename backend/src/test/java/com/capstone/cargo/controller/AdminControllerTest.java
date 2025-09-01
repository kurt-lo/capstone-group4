package com.capstone.cargo.controller;

import com.capstone.cargo.dto.AuthLoginDto;
import com.capstone.cargo.dto.AuthRegistrationDto;
import com.capstone.cargo.dto.JwtResponseDto;
import com.capstone.cargo.dto.UserDTO;
import com.capstone.cargo.exception.InvalidCredentialsException;
import com.capstone.cargo.exception.ResourceAlreadyExistsException;
import com.capstone.cargo.exception.ResourceNotFoundException;
import com.capstone.cargo.exception.UsersNotFoundException;
import com.capstone.cargo.jwt.JwtFilter;
import com.capstone.cargo.model.User;
import com.capstone.cargo.role.Role;
import com.capstone.cargo.service.AuthService;
import com.capstone.cargo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtFilter jwtFilter;

    private User user;

    private UserDTO updatedUserDTO;

    private AuthRegistrationDto authRegistrationDto;

    private AuthLoginDto validAdminLoginDto, invalidAdminloginDto;

    private JwtResponseDto jwtResponseDto;

    private AuthLoginDto emptyUserPassLoginDto, emptyUsernameLoginDto, emptyPasswordLoginDto;

    @BeforeEach
    void setUp() {
        authRegistrationDto = new AuthRegistrationDto();
        authRegistrationDto.setEmailAddress("alexa.siri@gmail.com");
        authRegistrationDto.setUserRole("ADMIN");
        authRegistrationDto.setUsername("ADMIN1");
        authRegistrationDto.setPassword("ADMIN@123");

        user = new User();
        user.setId(1L);
        user.setUsername(authRegistrationDto.getUsername());
        user.setPassword(authRegistrationDto.getPassword());
        user.setEmailAddress(authRegistrationDto.getEmailAddress());
        user.setRole(Role.ADMIN);

        updatedUserDTO = new UserDTO(
                "UpdatedFirstName",
                "UpdatedLastName",
                "alexa.siri@gmail.com",
                "ADMIN",
                "COSCO"
        );

        validAdminLoginDto = new AuthLoginDto("ADMIN1", "ADMIN@123");
        invalidAdminloginDto = new AuthLoginDto("wrongUser", "wrongPassword");
        jwtResponseDto = new JwtResponseDto("ADMIN1", "mock-jwt-token");
        emptyUserPassLoginDto = new AuthLoginDto("", "");
        emptyUsernameLoginDto = new AuthLoginDto("", "password");
        emptyPasswordLoginDto = new AuthLoginDto("username", "");

    }

    @Test
    void test_givenValidAdmin_whenRegister_thenReturnSuccessMessage() throws Exception {
        when(authService.register(any(AuthRegistrationDto.class), eq(user.getRole()))).thenReturn("User registered successfully!");

        mockMvc.perform(post("/api/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRegistrationDto)))
                .andExpect(status().isCreated()).andDo(print())
                .andExpect(result ->
                        assertEquals("User registered successfully!", result.getResponse().getContentAsString()));

        verify(authService, times(1)).register(any(AuthRegistrationDto.class), eq(user.getRole()));
    }

    @Test
    void test_givenUsernameExists_whenRegisterAdmin_thenThrowExceptionMessage() throws Exception {
        when(authService.register(any(AuthRegistrationDto.class), eq(user.getRole())))
                .thenThrow(new ResourceAlreadyExistsException("Username already exists"));

        mockMvc.perform(post("/api/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRegistrationDto)))
                .andExpect(status().isConflict()).andDo(print())
                .andExpect(result ->
                        assertEquals("Username already exists", result.getResponse().getContentAsString()));

        verify(authService, times(1)).register(any(AuthRegistrationDto.class), eq(user.getRole()));
    }

    @Test
    void test_givenEmailExists_whenRegisterAdmin_thenThrowExceptionMessage() throws Exception {
        when(authService.register(any(AuthRegistrationDto.class), eq(user.getRole())))
                .thenThrow(new ResourceAlreadyExistsException("Email already exists"));

        mockMvc.perform(post("/api/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRegistrationDto)))
                .andExpect(status().isConflict()).andDo(print())
                .andExpect(result ->
                        assertEquals("Email already exists", result.getResponse().getContentAsString()));

        verify(authService, times(1)).register(any(AuthRegistrationDto.class), eq(user.getRole()));
    }

    @Test
    void test_givenValidAdmin_whenLogin_thenReturnJwtResponse() throws Exception {
        when(authService.login(any(AuthLoginDto.class),eq(Role.ADMIN))).thenReturn(jwtResponseDto);

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validAdminLoginDto)))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.username").value("ADMIN1"))
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));

        verify(authService, times(1)).login(any(AuthLoginDto.class),eq(Role.ADMIN));
    }

    @Test
    void test_givenInvalidUser_whenLogin_thenThrowExceptionMessage() throws Exception {
        when(authService.login(any(AuthLoginDto.class),eq(Role.ADMIN))).thenThrow(new InvalidCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidAdminloginDto)))
                .andExpect(status().isBadRequest()).andDo(print())
                .andExpect(result ->
                        assertEquals("Invalid username or password", result.getResponse().getContentAsString()));

        verify(authService, times(1)).login(any(AuthLoginDto.class),eq(Role.ADMIN));
    }

    @Test
    void test_givenUsers_whenGetAllUsers_thenReturnUserList() throws Exception {
        List<User> userlist = List.of(
                user,
                new User(2L, "ADMIN2", "John", "Doe", "P@ssw0rd", "admin@gmail.com", Role.ADMIN,
                        "2023-05-20 13:45:30", "2023-05-20 13:45:30", "COSCO")
        );

        when(userService.getAllUsers()).thenReturn(userlist);

        mockMvc.perform(get("/api/admin/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].username").value("ADMIN1"))
                .andExpect(jsonPath("$[1].username").value("ADMIN2"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void test_givenNoUser_whenGetAllUsers_thenReturnNoContent() throws Exception {
        when(userService.getAllUsers()).thenThrow(new UsersNotFoundException("No users found"));

        mockMvc.perform(get("/api/admin/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andDo(print())
                .andExpect(result ->
                        assertEquals("No users found", result.getResponse().getContentAsString()));
        ;

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void test_givenUser_whenGetUserById_thenReturnUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/admin/user/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.username").value("ADMIN1"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void test_givenNoUser_whenGetUserById_thenReturnNotFound() throws Exception {
        when(userService.getUserById(2L)).thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/admin/user/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print())
                .andExpect(result ->
                        assertEquals("User not found", result.getResponse().getContentAsString()));

        verify(userService, times(1)).getUserById(2L);
    }

    @Test
    void test_givenUser_whenDeleteUser_thenReturnSuccessMessage() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/admin/user/1"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(result ->
                        assertEquals("User deleted successfully", result.getResponse().getContentAsString()));

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void test_givenNoUser_whenDeleteUser_thenReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("User not found")).when(userService).deleteUser(2L);

        mockMvc.perform(delete("/api/admin/user/2"))
                .andExpect(status().isNotFound()).andDo(print())
                .andExpect(result ->
                        assertEquals("User not found", result.getResponse().getContentAsString()));

        verify(userService, times(1)).deleteUser(2L);
    }

    @Test
    void test_givenEmptyUsername_whenLogin_thenThrowExceptionMessage() throws Exception {

        when(authService.login(any(AuthLoginDto.class),eq(Role.ADMIN))).thenThrow(new InvalidCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(emptyUsernameLoginDto)))
                .andExpect(status().isBadRequest()).andDo(print())
                .andExpect(result ->
                        assertEquals("Invalid username or password", result.getResponse().getContentAsString()));
        verify(authService, times(1)).login(any(AuthLoginDto.class),eq(Role.ADMIN));
    }

    @Test
    void test_givenEmptyPassword_whenLogin_thenThrowExceptionMessage() throws Exception {

        when(authService.login(any(AuthLoginDto.class),eq(Role.ADMIN))).thenThrow(new InvalidCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(emptyPasswordLoginDto)))
                .andExpect(status().isBadRequest()).andDo(print())
                .andExpect(result ->
                        assertEquals("Invalid username or password", result.getResponse().getContentAsString()));
        verify(authService, times(1)).login(any(AuthLoginDto.class),eq(Role.ADMIN));
    }

    @Test
    void test_givenEmptyUsernameAndPassword_whenLogin_thenThrowExceptionMessage() throws Exception {
        when(authService.login(any(AuthLoginDto.class),eq(Role.ADMIN))).thenThrow(new InvalidCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(emptyUserPassLoginDto)))
                .andExpect(status().isBadRequest()).andDo(print())
                .andExpect(result ->
                        assertEquals("Invalid username or password", result.getResponse().getContentAsString()));
        verify(authService, times(1)).login(any(AuthLoginDto.class),eq(Role.ADMIN));
    }

    @Test
    void test_givenUpdatedUser_whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(put("/api/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUserDTO)))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.firstName").value("UpdatedFirstName"))
                .andExpect(jsonPath("$.lastName").value("UpdatedLastName"))
                .andExpect(jsonPath("$.emailAddress").value("alexa.siri@gmail.com"))
                .andExpect(jsonPath("$.userRole").value("ADMIN"));

        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

}