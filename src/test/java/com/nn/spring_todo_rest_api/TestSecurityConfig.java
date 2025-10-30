package com.nn.spring_todo_rest_api;

import com.nn.spring_todo_rest_api.task.repository.TaskRepository;
import com.nn.spring_todo_rest_api.task.service.TaskService;
import com.nn.spring_todo_rest_api.task.support.TaskMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class TestSecurityConfig {

    @Bean
    public TaskService taskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        return new TaskService(taskRepository, taskMapper);
    }
}
