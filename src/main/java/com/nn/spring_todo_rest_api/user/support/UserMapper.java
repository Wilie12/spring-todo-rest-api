package com.nn.spring_todo_rest_api.user.support;

import com.nn.spring_todo_rest_api.user.api.request.UserRequest;
import com.nn.spring_todo_rest_api.user.domain.UserAccount;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount toUserAccount(UserRequest userRequest) {
        return new UserAccount(
                userRequest.username(),
                passwordEncoder.encode(userRequest.password())
        );
    }
}
