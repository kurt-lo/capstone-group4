package com.capstone.cargo.repository;

import com.capstone.cargo.model.User;
import jakarta.persistence.MapKeyEnumerated;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("AlexaSiri00");
        user.setEmailAddress("alexa.siri@gmail.com");
        user.setPassword("Alex@Sir!123");
        user.setUserRole("USER");
        user.setFirstName("Alexa");
        user.setLastName("Siri");
        user.setCompanyName("OOCL");
        user.setCreatedAt("2023-05-20 13:45:30");
        user.setUpdatedAt("2023-05-20 13:45:30");
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void test_givenUserExists_whenFindByUsername_thenReturnUser() {
        User foundUser = userRepository.findByUsername(user.getUsername()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    void test_givenUserDoesNotExist_whenFindByUsername_thenReturnNull() {
        User foundUser = userRepository.findByUsername("dummyUsername").orElse(null);

        assertNull(foundUser);
    }

    @Test
    void test_givenUserExists_whenFindByEmailAddress_thenReturnUser() {
        User foundUser = userRepository.findByEmailAddress(user.getEmailAddress()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(user.getEmailAddress(), foundUser.getEmailAddress());
    }

    @Test
    void test_givenUserDoesNotExist_whenFindByEmailAddress_thenReturnNull() {
        User foundUser = userRepository.findByEmailAddress("dummy@gmail,com").orElse(null);

        assertNull(foundUser);
    }

    @Test
    void test_givenUserExists_whenExistsByUsername_thenReturnTrue() {
        boolean exists = userRepository.existsByUsername(user.getUsername());

        assertTrue(exists);
    }

    @Test
    void test_givenUserDoesNotExist_whenExistsByUsername_thenReturnFalse() {
        boolean exists = userRepository.existsByUsername("dummyUsername");

        assertFalse(exists);
    }

    @Test
    void test_givenUserExists_whenExistsByEmail_thenReturnTrue() {
        boolean exists = userRepository.existsByEmailAddress(user.getEmailAddress());

        assertTrue(exists);
    }

    @Test
    void test_givenUserDoesNotExist_whenExistsByEmail_thenReturnFalse() {
        boolean exists = userRepository.existsByEmailAddress("dummy@gmail.com");

        assertFalse(exists);
    }
}