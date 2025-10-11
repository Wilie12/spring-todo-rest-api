package com.nn.spring_todo_rest_api.task.api.response;

public record TaskResponse(
        long id,
        String title,
        String description,
        boolean isCompleted
) {
}
