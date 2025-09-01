package com.capstone.cargo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    String firstName;
    String lastName;
    String emailAddress;
    String userRole;
    String companyName;
}
