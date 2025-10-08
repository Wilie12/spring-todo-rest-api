package com.nn.spring_todo_rest_api.user.controller;

import com.nn.spring_todo_rest_api.user.api.request.UserRequest;
import com.nn.spring_todo_rest_api.user.domain.UserAccount;
import com.nn.spring_todo_rest_api.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest) {
        UserAccount userAccount = userService.create(userRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }
}
