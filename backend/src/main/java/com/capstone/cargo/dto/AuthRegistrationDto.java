package com.capstone.cargo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRegistrationDto {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String userRole;
    private String companyName;
    private String username;
    private String password;
}