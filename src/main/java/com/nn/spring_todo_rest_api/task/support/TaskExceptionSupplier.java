package com.nn.spring_todo_rest_api.task.support;

import com.nn.spring_todo_rest_api.task.support.exception.TaskNotFoundException;

import java.util.function.Supplier;

public class TaskExceptionSupplier {

    public static Supplier<TaskNotFoundException> taskNotFound(Long taskId) {
        return () -> new TaskNotFoundException(taskId);
    }
}
