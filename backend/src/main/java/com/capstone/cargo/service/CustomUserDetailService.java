package com.capstone.cargo.service;

import com.capstone.cargo.model.User;
import com.capstone.cargo.repository.UserRepository;
import com.capstone.cargo.security.CustomerUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new CustomerUserDetails(user.get());
        }

        throw new UsernameNotFoundException("No User or Admin found with username: " + username);
    }

}

