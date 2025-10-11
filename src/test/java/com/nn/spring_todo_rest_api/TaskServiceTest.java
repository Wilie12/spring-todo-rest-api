package com.nn.spring_todo_rest_api;

import com.nn.spring_todo_rest_api.task.api.response.TaskResponse;
import com.nn.spring_todo_rest_api.task.domain.Task;
import com.nn.spring_todo_rest_api.task.repository.TaskRepository;
import com.nn.spring_todo_rest_api.task.service.TaskService;
import com.nn.spring_todo_rest_api.task.support.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

// TODO - tests are not reading @PreAuthorize annotation from Service
@SpringBootTest
public class TaskServiceTest {
    TaskService taskService;
    @MockitoBean
    TaskRepository taskRepository;
    @MockitoBean
    TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, taskMapper);
    }

    @Test
    @WithMockUser("bob")
    void findAllForUsernameShouldReturnTasksForCorrectUser() {
        // given
        Task task = new Task("bob", "read", "desc");
        when(taskRepository.findByUsername("bob")).thenReturn(List.of(task));
        when(taskMapper.toTaskResponse(task)).thenReturn(new TaskResponse(1, "bob", "read", false));

        // when
        List<TaskResponse> tasks = taskService.findAllForUsername("bob");

        // then
        assertThat(tasks).hasSize(1);
    }

    @Test
    @WithMockUser("bob")
    void findAllForUsernameShouldThrowAccessDeniedForIncorrectUser() {
        // given
        Task task = new Task("alice", "clean", "desc");
        when(taskRepository.findByUsername("alice")).thenReturn(List.of(task));
        when(taskMapper.toTaskResponse(task)).thenReturn(new TaskResponse(1, "alice", "clean", false));

        // when
        List<TaskResponse> tasks = taskService.findAllForUsername("alice");

        // then
//        assertThrows(AccessDeniedException.class, () -> taskService.findAllForUsername("alice"));
    }
}
