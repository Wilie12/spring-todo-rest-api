package com.nn.spring_todo_rest_api;

import com.nn.spring_todo_rest_api.task.domain.Task;
import com.nn.spring_todo_rest_api.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
public class TaskRepositoryTestcontainersTest {

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.saveAll(
                List.of(
                        new Task("alice", "Prepare", "desc"),
                        new Task("alice", "Clean", "desc"),
                        new Task("bob", "Cook", "desc"),
                        new Task("eva", "Read", "desc")
                )
        );
    }

    @Test
    void findByUsernameShouldReturnOnlyTasksForUser() {
        List<Task> tasks = taskRepository.findByUsername("alice");
        assertThat(tasks).hasSize(2);
    }
}
