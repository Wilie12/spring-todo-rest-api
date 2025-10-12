package com.nn.spring_todo_rest_api.task.service;

import com.nn.spring_todo_rest_api.task.api.request.TaskRequest;
import com.nn.spring_todo_rest_api.task.api.response.TaskResponse;
import com.nn.spring_todo_rest_api.task.domain.Task;
import com.nn.spring_todo_rest_api.task.repository.TaskRepository;
import com.nn.spring_todo_rest_api.task.support.TaskMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @PreAuthorize("#username == authentication.name")
    public List<TaskResponse> findAllForUsername(String username) {
        return taskRepository.findByUsername(username)
                .stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @PreAuthorize("hasRole('USER') && #username == authentication.name")
    public TaskResponse create(TaskRequest taskRequest, String username) {
        Task task = taskRepository.save(taskMapper.toTask(taskRequest, username));
        return taskMapper.toTaskResponse(task);
    }
}
