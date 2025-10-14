package com.nn.spring_todo_rest_api.task.support;

import com.nn.spring_todo_rest_api.task.api.request.TaskRequest;
import com.nn.spring_todo_rest_api.task.api.request.TaskUpdateRequest;
import com.nn.spring_todo_rest_api.task.api.response.TaskResponse;
import com.nn.spring_todo_rest_api.task.domain.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );
    }

    public Task toTask(TaskRequest taskRequest, String username) {
        return new Task(
                username,
                taskRequest.title(),
                taskRequest.description()
        );
    }

    public Task toTask(Task task, TaskUpdateRequest taskUpdateRequest) {
        task.setTitle(taskUpdateRequest.title());
        task.setDescription(taskUpdateRequest.description());
        task.setIsCompleted(taskUpdateRequest.isCompleted());
        return task;
    }
}
