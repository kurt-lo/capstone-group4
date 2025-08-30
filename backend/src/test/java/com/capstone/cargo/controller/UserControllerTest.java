package com.capstone.cargo.controller;

import com.capstone.cargo.dto.JwtResponseDto;
import com.capstone.cargo.dto.UserLoginDto;
import com.capstone.cargo.dto.UserRegistrationDto;
import com.capstone.cargo.jwt.JwtFilter;
import com.capstone.cargo.model.User;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // disables Spring Security filters
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtFilter jwtFilter;

    private User user;

    private UserRegistrationDto userRegisterDto;

    private UserLoginDto validUserLoginDto;

    private UserLoginDto invalidUserloginDto;

    private JwtResponseDto jwtResponseDto;

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
        jwtResponseDto = new JwtResponseDto("AlexaSiri00", "mock-jwt-token");
    }

    @Test
    void test_HelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/user/hello"))
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals("Hello, User!", result.getResponse().getContentAsString()));
    }

    @Test
    void test_givenUsers_whenGetAllUsers_thenReturnUserList() throws Exception {
        List<User> userlist = List.of(
                user,
                new User(2L, "ADMIN", "P@ssw0rd", "admin@gmail.com", "ADMIN",
                        "John", "Doe", "COSCO", "2023-05-20 13:45:30", "2023-05-20 13:45:30")
        );

        when(userService.getAllUsers()).thenReturn(userlist);

        mockMvc.perform(get("/api/user").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].username").value("AlexaSiri00"))
                .andExpect(jsonPath("$[1].username").value("ADMIN"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void test_givenNoUsers_whenGetAllUsers_thenReturnNoContent() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/user").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andDo(print())
                .andExpect(content().string(""));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void test_givenError_whenGetAllUsers_thenReturnException() throws Exception {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Server error"));

        mockMvc.perform(get("/api/user").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to fetch users"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void test_givenUser_whenRegisterUser_thenReturnSuccessMessage() throws Exception {
        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn("User registered successfully!");

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRegisterDto)))
                .andExpect(status().isCreated()).andDo(print())
                .andExpect(result ->
                        assertEquals("User registered successfully!", result.getResponse().getContentAsString()));

        verify(userService, times(1)).registerUser(any(UserRegistrationDto.class));
    }

    @Test
    void test_givenUsernameExists_whenRegisterUser_thenReturnFailureMessage() throws Exception {
        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn("Username already exists!");

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRegisterDto)))
                .andExpect(status().isBadRequest()).andDo(print())
                .andExpect(result ->
                        assertEquals("Username already exists!", result.getResponse().getContentAsString()));

        verify(userService, times(1)).registerUser(any(UserRegistrationDto.class));
    }

    @Test
    void test_givenEmailExists_whenRegisterUser_thenReturnFailureMessage() throws Exception {
        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn("Email already exists!");

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRegisterDto)))
                .andExpect(status().isBadRequest()).andDo(print())
                .andExpect(result ->
                        assertEquals("Email already exists!", result.getResponse().getContentAsString()));

        verify(userService, times(1)).registerUser(any(UserRegistrationDto.class));
    }

    @Test
    void test_givenValidUser_whenLoginUser_thenReturnJwtResponse() throws Exception {
        when(userService.loginUser(any(UserLoginDto.class))).thenReturn(jwtResponseDto);
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validUserLoginDto)))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.username").value("AlexaSiri00"))
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));

        verify(userService, times(1)).loginUser(any(UserLoginDto.class));
    }

    @Test
    void test_givenInvalidUser_whenLoginUser_thenReturnErrorMessage() throws Exception {
        when(userService.loginUser(any(UserLoginDto.class))).thenThrow(new RuntimeException("Invalid username or password"));
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidUserloginDto)))
                .andExpect(status().isBadRequest()).andDo(print())
                .andExpect(result ->
                        assertEquals("Invalid username or password", result.getResponse().getContentAsString()));

        verify(userService, times(1)).loginUser(any(UserLoginDto.class));
    }


}