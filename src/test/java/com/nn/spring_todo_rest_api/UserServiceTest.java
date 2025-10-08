package com.nn.spring_todo_rest_api;

import com.nn.spring_todo_rest_api.user.api.request.UserRequest;
import com.nn.spring_todo_rest_api.user.domain.UserAccount;
import com.nn.spring_todo_rest_api.user.repository.UserRepository;
import com.nn.spring_todo_rest_api.user.service.UserService;
import com.nn.spring_todo_rest_api.user.support.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository, userMapper);
    }

    @Test
    void createUserShouldReturnTheSameUserWithHashedPassword() {
        // given
        UserRequest userToCreate = new UserRequest("alice", "password");
        when(userMapper.toUserAccount(any(UserRequest.class)))
                .thenReturn(new UserAccount("alice", "hashed_password"));
        when(userRepository.save(any(UserAccount.class)))
                .thenReturn(new UserAccount("alice", "hashed_password"));

        // when
        UserAccount createdUser = userService.create(userToCreate);

        // then
        assertThat(createdUser.getUsername()).isEqualTo(userToCreate.username());
        assertThat(createdUser.getPassword()).isNotEqualTo(userToCreate.password());
    }
}
