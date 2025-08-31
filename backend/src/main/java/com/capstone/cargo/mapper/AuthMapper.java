package com.capstone.cargo.mapper;

import com.capstone.cargo.dto.AuthRegistrationDto;
import com.capstone.cargo.model.User;
import com.capstone.cargo.role.Role;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    // DTO to User
    public User fromRegistrationDtoToUser(AuthRegistrationDto registrationDto, Role role) {
        if (registrationDto == null) return null;

        return User.builder()
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .companyName(registrationDto.getCompanyName())
                .emailAddress(registrationDto.getEmailAddress())
                .username(registrationDto.getUsername())
                .role(role) // fixed: match Auth base field
                .password(registrationDto.getPassword())
                .build();
    }

    // Model (User) to DTO
    public AuthRegistrationDto fromUserToDto(User user) {
        if (user == null) return null;

        return AuthRegistrationDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .companyName(user.getCompanyName())
                .emailAddress(user.getEmailAddress())
                .username(user.getUsername())
                .build();
    }

}