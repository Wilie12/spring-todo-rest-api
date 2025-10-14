package com.nn.spring_todo_rest_api.user.support;

import com.nn.spring_todo_rest_api.user.support.exception.UserAlreadyExistsException;

import java.util.function.Supplier;

public class UserExceptionSupplier {

    public static Supplier<UserAlreadyExistsException> userAlreadyExists(String username) {
        return () -> new UserAlreadyExistsException(username);
    }
}
