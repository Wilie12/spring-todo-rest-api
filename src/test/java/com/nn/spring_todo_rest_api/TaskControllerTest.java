package com.nn.spring_todo_rest_api;

import com.nn.spring_todo_rest_api.task.controller.TaskController;
import com.nn.spring_todo_rest_api.task.service.TaskService;
import com.nn.spring_todo_rest_api.user.service.UserAccountDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
@Import(SecurityConfig.class)
public class TaskControllerTest {
    @Autowired
    MockMvc mvc;
    @MockitoBean
    TaskService taskService;
    @MockitoBean
    UserAccountDetailsService userAccountDetailsService;

    @Test
    @WithMockUser(username = "alice")
    void getAllTasksShouldWork() throws Exception {
        mvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk());

        verify(taskService).findAllForUsername("alice");
    }
}
