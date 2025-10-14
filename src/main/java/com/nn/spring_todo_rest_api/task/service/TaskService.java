package com.nn.spring_todo_rest_api.task.service;

import com.nn.spring_todo_rest_api.task.api.request.TaskRequest;
import com.nn.spring_todo_rest_api.task.api.request.TaskUpdateRequest;
import com.nn.spring_todo_rest_api.task.api.response.TaskResponse;
import com.nn.spring_todo_rest_api.task.domain.Task;
import com.nn.spring_todo_rest_api.task.repository.TaskRepository;
import com.nn.spring_todo_rest_api.task.support.TaskExceptionSupplier;
import com.nn.spring_todo_rest_api.task.support.TaskMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @PreAuthorize("hasAuthority('USER') && #username == authentication.name")
    public List<TaskResponse> findAllForUsername(String username) {
        return taskRepository.findByUsername(username)
                .stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

    @PreAuthorize("hasAuthority('USER') && #username == authentication.name")
    public TaskResponse findOne(Long taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(TaskExceptionSupplier.taskNotFound(taskId));

        if (!Objects.equals(task.getUsername(), username)) {
            throw new AccessDeniedException("Unauthorized action");
        }

        return taskMapper.toTaskResponse(task);
    }

    @PreAuthorize("hasAuthority('USER') and #username == authentication.name")
    public TaskResponse create(TaskRequest taskRequest, String username) {
        Task task = taskRepository.save(taskMapper.toTask(taskRequest, username));
        return taskMapper.toTaskResponse(task);
    }

    @PreAuthorize("hasAuthority('USER') && #username == authentication.name")
    public void delete(long taskId, String username) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(TaskExceptionSupplier.taskNotFound(taskId));

        if (!Objects.equals(task.getUsername(), username)) {
            throw new AccessDeniedException("Unauthorized action");
        }

       taskRepository.deleteById(taskId);
    }

    @PreAuthorize("hasAuthority('USER') && #username == authentication.name")
    public TaskResponse update(Long taskId, TaskUpdateRequest taskUpdateRequest, String username) {
        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(TaskExceptionSupplier.taskNotFound(taskId));

        if (!Objects.equals(task.getUsername(), username)) {
            throw new AccessDeniedException("Unauthorized action");
        }

        taskRepository.save(taskMapper.toTask(task, taskUpdateRequest));
        return taskMapper.toTaskResponse(task);
    }
}
