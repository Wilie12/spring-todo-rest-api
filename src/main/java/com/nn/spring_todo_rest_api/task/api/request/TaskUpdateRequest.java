package com.nn.spring_todo_rest_api.task.api.request;

public record TaskUpdateRequest(
        String title,
        String description,
        boolean isCompleted
) {
}
