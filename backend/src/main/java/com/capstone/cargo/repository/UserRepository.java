package com.capstone.cargo.repository;

import com.capstone.cargo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String username);
}