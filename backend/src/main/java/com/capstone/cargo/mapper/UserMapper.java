package com.capstone.cargo.mapper;

import com.capstone.cargo.dto.UserRegistrationDto;
import com.capstone.cargo.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // DTO to Model
    public User fromRegistrationDtoToModel(UserRegistrationDto registrationDto) {
        if (registrationDto == null) return null;

        return User.builder()
                .id(null)
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .companyName(registrationDto.getCompanyName())
                .emailAddress(registrationDto.getEmailAddress())
                .username(registrationDto.getUsername())
                .userRole("USER")
                .password(registrationDto.getPassword())
                .build();
    }

    // Model to DTO
    public UserRegistrationDto fromUserToDto(User user) {
        if (user == null) return null;

        return UserRegistrationDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .companyName(user.getCompanyName())
                .emailAddress(user.getEmailAddress())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

}
