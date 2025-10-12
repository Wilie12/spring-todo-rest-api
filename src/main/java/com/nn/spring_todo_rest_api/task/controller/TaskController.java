package com.nn.spring_todo_rest_api.task.controller;

import com.nn.spring_todo_rest_api.task.api.request.TaskRequest;
import com.nn.spring_todo_rest_api.task.api.response.TaskResponse;
import com.nn.spring_todo_rest_api.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAll(
            Authentication authentication
    ) {
        List<TaskResponse> tasks = taskService.findAllForUsername(authentication.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(
            @RequestBody TaskRequest taskRequest,
            Authentication authentication
    ) {
        TaskResponse task = taskService.create(taskRequest, authentication.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(task);
    }
}
