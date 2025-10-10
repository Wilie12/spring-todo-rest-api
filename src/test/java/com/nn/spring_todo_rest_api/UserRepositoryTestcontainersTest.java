package com.nn.spring_todo_rest_api;

import com.nn.spring_todo_rest_api.user.domain.UserAccount;
import com.nn.spring_todo_rest_api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
public class UserRepositoryTestcontainersTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(
                List.of(
                        new UserAccount("alice", "password"),
                        new UserAccount("bob", "password"),
                        new UserAccount("eva", "password")
                )
        );
    }

    @Test
    void findAllShouldReturnAllUsers() {
        List<UserAccount> users = userRepository.findAll();
        assertThat(users).hasSize(3);
    }

    @Test
    void findByUsernameShouldReturnCorrectUser() {
        UserAccount user = userRepository.findByUsername("alice");
        assertThat(user.getUsername()).isEqualTo("alice");
    }
}