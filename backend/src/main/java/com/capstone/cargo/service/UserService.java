package com.capstone.cargo.service;

import com.capstone.cargo.model.Container;
import com.capstone.cargo.model.User;
import com.capstone.cargo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    public List<User> findAll() {
//        return userRepository.findAll();
//    }

    /** Add/Register New User **/
    public User addUser(User user) {
        User newUser = userRepository.save(user);
        //kafkaProducer.sendMessage(newUser); // Uncomment if Kafka integration is needed

        if(userRepository.existsUserByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return newUser;
    }


}
