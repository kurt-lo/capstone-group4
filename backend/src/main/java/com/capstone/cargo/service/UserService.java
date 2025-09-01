package com.capstone.cargo.service;

import com.capstone.cargo.dto.UserDTO;
import com.capstone.cargo.exception.ResourceAlreadyExistsException;
import com.capstone.cargo.exception.ResourceNotFoundException;
import com.capstone.cargo.exception.UsersNotFoundException;
import com.capstone.cargo.model.User;
import com.capstone.cargo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UsersNotFoundException("No users found");
        }
        return users;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.error("User not found with id: {}", id);
            return new ResourceNotFoundException("User not found");
        });
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            log.error("User not found with username: {}", username);
            return new ResourceNotFoundException("User not found");
        });
    }

    public UserDTO updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> {
            log.error("User update failed: User not found with id: {}", id);
            return new ResourceNotFoundException("User not found");
        });

        if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }

        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        if (updatedUser.getUsername() != null) {
            if (userRepository.existsByUsername(updatedUser.getUsername())
                    && (!existingUser.getUsername().equals(updatedUser.getUsername()))) {
                log.error("Failed to update: Username already exists.");
                throw new ResourceAlreadyExistsException("Username already exists");
            }

            existingUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getEmailAddress() != null) {
            if (userRepository.existsByEmailAddress(updatedUser.getEmailAddress())
                    && (!existingUser.getEmailAddress().equals(updatedUser.getEmailAddress()))) {
                log.error("Failed to update: Email already exists.");
                throw new ResourceAlreadyExistsException("Email already exists");
            }
            existingUser.setEmailAddress(updatedUser.getEmailAddress());
        }

        User savedUser = userRepository.save(existingUser);
//        UserDTO userDTO = new UserDTO(savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmailAddress(), savedUser.getRole().name(), savedUser.getCompanyName());
        log.info("User updated successfully with id: {}", id);
        return new UserDTO(savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmailAddress(), savedUser.getRole().name(), savedUser.getCompanyName());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            log.error("User deletion failed: User not found with id: {}", id);
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }


}