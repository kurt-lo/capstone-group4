package com.capstone.cargo.model;

import com.capstone.cargo.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "password", nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at")
    private String createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "company_name")
    private String companyName;

}

