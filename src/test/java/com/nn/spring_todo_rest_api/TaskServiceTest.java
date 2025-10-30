package com.nn.spring_todo_rest_api;

import com.nn.spring_todo_rest_api.task.api.request.TaskRequest;
import com.nn.spring_todo_rest_api.task.api.request.TaskUpdateRequest;
import com.nn.spring_todo_rest_api.task.api.response.TaskResponse;
import com.nn.spring_todo_rest_api.task.domain.Task;
import com.nn.spring_todo_rest_api.task.repository.TaskRepository;
import com.nn.spring_todo_rest_api.task.service.TaskService;
import com.nn.spring_todo_rest_api.task.support.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSecurityConfig.class)
public class TaskServiceTest {
    @Autowired
    TaskService taskService;
    @MockitoBean
    TaskRepository taskRepository;
    @MockitoBean
    TaskMapper taskMapper;

    @Test
    @WithMockUser(username = "bob", authorities = "USER")
    void findAllForUsernameShouldReturnTasksForCorrectUser() {
        // given
        Task task = new Task("bob", "read", "desc");
        when(taskRepository.findByUsername("bob")).thenReturn(List.of(task));
        when(taskMapper.toTaskResponse(task))
                .thenReturn(new TaskResponse(1, "bob", "read", false));

        // when
        List<TaskResponse> tasks = taskService.findAllForUsername("bob");

        // then
        assertThat(tasks).hasSize(1);
    }

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void findOneShouldReturnCorrectTask() {
        // given
        Task task = new Task("alice", "clean", "desc");
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(taskMapper.toTaskResponse(any()))
                .thenReturn(new TaskResponse(1, "clean", "desc", false));

        // when
        TaskResponse taskResponse = taskService.findOne(task.getId(), "alice");

        // then
        assertThat(taskResponse.title()).isEqualTo(task.getTitle());
        assertThat(taskResponse.description()).isEqualTo(task.getDescription());
    }

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void createTaskShouldReturnTheSameData() {
        // given
        Task task = new Task("alice", "clean", "desc");
        when(taskRepository.save(any())).thenReturn(task);
        when(taskMapper.toTask(any(TaskRequest.class), any(String.class))).thenReturn(task);
        when(taskMapper.toTaskResponse(any()))
                .thenReturn(new TaskResponse(1L, "clean", "desc", false));

        // when
        TaskResponse taskCreated = taskService.create(new TaskRequest("clean", "desc"), "alice");

        // then
        assertThat(taskCreated.title()).isEqualTo(task.getTitle());
        assertThat(taskCreated.description()).isEqualTo(task.getDescription());
    }

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void deleteTaskShouldWork() {
        // given
        Task taskToDelete = new Task("alice", "title", "desc");
        when(taskRepository.findById(any())).thenReturn(Optional.of(taskToDelete));

        // when
        taskService.delete(taskToDelete.getId(), "alice");

        // then
        verify(taskRepository).findById(taskToDelete.getId());
        verify(taskRepository).deleteById(taskToDelete.getId());
    }

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void updateTaskShouldReturnUpdatedData() {
        // given
        Task taskToUpdate = new Task("alice", "wrongTitle", "wrongDesc");
        when(taskRepository.findById(any())).thenReturn(Optional.of(taskToUpdate));
        when(taskMapper.toTask(any(Task.class), any(TaskUpdateRequest.class)))
                .thenReturn(new Task("alice", "correctTitle", "correctDesc"));
        when(taskMapper.toTaskResponse(any()))
                .thenReturn(new TaskResponse(1L, "correctTitle", "correctDesc", false));

        // when
        TaskResponse taskResponse = taskService.update(
                1L,
                new TaskUpdateRequest("correctTitle", "correctDesc", false),
                "alice"
        );

        // then
        assertThat(taskResponse.title()).isEqualTo("correctTitle");
        assertThat(taskResponse.description()).isEqualTo("correctDesc");
    }
}
