package com.nn.spring_todo_rest_api.user.service;

import com.nn.spring_todo_rest_api.user.api.request.UserRequest;
import com.nn.spring_todo_rest_api.user.domain.UserAccount;
import com.nn.spring_todo_rest_api.user.repository.UserRepository;
import com.nn.spring_todo_rest_api.user.support.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    public final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserAccount create(UserRequest userRequest) {
        return userRepository.save(userMapper.toUserAccount(userRequest));
    }
}
