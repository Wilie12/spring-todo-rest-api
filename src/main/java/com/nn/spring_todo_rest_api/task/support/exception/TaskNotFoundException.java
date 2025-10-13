package com.nn.spring_todo_rest_api.task.support.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(long taskId) {
        super(String.format("Task with id %d not found", taskId));
    }
}