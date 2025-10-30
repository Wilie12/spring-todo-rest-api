package com.nn.spring_todo_rest_api;

import com.nn.spring_todo_rest_api.user.api.request.UserRequest;
import com.nn.spring_todo_rest_api.user.config.SecurityConfig;
import com.nn.spring_todo_rest_api.user.controller.UserController;
import com.nn.spring_todo_rest_api.user.service.UserAccountDetailsService;
import com.nn.spring_todo_rest_api.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {
    @Autowired
    MockMvc mvc;
    @MockitoBean
    UserService userService;

    @Test
    void registerUserShouldWork() throws Exception {
        mvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "elise1",
                                    "password": "password"
                                }""")
                )

                .andExpect(status().isCreated());

        verify(userService).create(new UserRequest("elise1", "password"));
    }
}
