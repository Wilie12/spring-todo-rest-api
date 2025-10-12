package com.nn.spring_todo_rest_api.task.api.request;

public record TaskRequest(
        String title,
        String description
) {}
