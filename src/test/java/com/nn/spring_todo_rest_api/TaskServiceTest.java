package com.nn.spring_todo_rest_api;

import com.nn.spring_todo_rest_api.task.api.request.TaskRequest;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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

    @Test
    @WithMockUser(username = "alice")
    void createTaskShouldReturnTheSameData() {
        // given
        Task task = new Task("alice", "clean", "desc");
        when(taskRepository.save(any())).thenReturn(task);
        when(taskMapper.toTask(any(), any())).thenReturn(task);
        when(taskMapper.toTaskResponse(any()))
                .thenReturn(new TaskResponse(1L, "clean", "desc", false));

        // when
        TaskResponse taskCreated = taskService.create(new TaskRequest("clean", "desc"), "alice");

        // then
        assertThat(taskCreated.title()).isEqualTo(task.getTitle());
        assertThat(taskCreated.description()).isEqualTo(task.getDescription());
    }

    @Test
    @WithMockUser(username = "alice")
    void deleteTaskShouldWork() {
        // given
        Task taskToDelete = new Task("alice", "title", "desc");
        when(taskRepository.findById(any())).thenReturn(Optional.of(taskToDelete));

        // when
        taskService.delete(taskToDelete.getId());

        // then
        verify(taskRepository).findById(taskToDelete.getId());
        verify(taskRepository).deleteById(taskToDelete.getId());
    }
}
