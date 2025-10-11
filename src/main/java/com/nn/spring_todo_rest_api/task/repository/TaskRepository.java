package com.nn.spring_todo_rest_api.task.repository;

import com.nn.spring_todo_rest_api.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUsername(String username);
}
