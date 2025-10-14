package com.nn.spring_todo_rest_api.user.support.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String username) {
        super(String.format("User with username: %s, already exists", username));
    }
}
