package com.capstone.cargo.service;

import com.capstone.cargo.model.User;
import com.capstone.cargo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomerUserDetailService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userModel = userRepository.findByUsername(username);
        if (userModel.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                userModel.get().getUsername(),
                userModel.get().getPassword(),
                Collections.emptyList()
        );
    }

}

