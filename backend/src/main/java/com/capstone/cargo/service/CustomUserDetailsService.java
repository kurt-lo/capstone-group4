package com.capstone.cargo.service;

import com.capstone.cargo.model.User;
import com.capstone.cargo.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // Your JPA repository for users

    public CustomUserDetailsService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Build Spring Security's UserDetails object from your User entity
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // Hashed password from the database
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}

