package com.capstone.cargo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userRole;

    @NotNull(message = "First name cannot be empty")
    private String firstName;

    @NotNull(message = "Last name cannot be empty")
    private String lastName;

    @NotNull(message = "Company name cannot be empty")
    private String companyName;

    @Email
    @NotNull(message = "Email Address cannot be empty")
    private String email;

    @NotNull(message = "Username cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,}$", message = "Username must be at least 3 characters and contain only letters, numbers, or underscores.")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, include an uppercase letter, a lowercase letter, a number, and a special characters (@$!%*?&).")
    @NotNull(message = "Password cannot be empty")
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
