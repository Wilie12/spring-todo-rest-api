package com.nn.spring_todo_rest_api;

import com.nn.spring_todo_rest_api.user.domain.UserAccount;
import com.nn.spring_todo_rest_api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = UserRepositoryTest.DataSourceInitializer.class)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Container
    static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:9.6.12")
            .withUsername("postgres");

    static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + database.getJdbcUrl(),
                    "spring.datasource.username=" + database.getUsername(),
                    "spring.datasource.password=" + database.getPassword(),
                    "spring.jpa.hibernate.ddl-auto=create-drop"
            );
        }
    }

    @BeforeEach
    void setUp() {
        userRepository.saveAll(
                List.of(
                        new UserAccount("eva", "password"),
                        new UserAccount("alice", "password"),
                        new UserAccount("bob", "password")
                )
        );
    }

    @Test
    void findByUsernameShouldWork() {
        UserAccount user = userRepository.findByUsername("alice");
        assertThat(user.getUsername()).isEqualTo("alice");
    }
}
