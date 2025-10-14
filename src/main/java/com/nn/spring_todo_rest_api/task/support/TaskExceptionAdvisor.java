package com.nn.spring_todo_rest_api.task.support;

import com.nn.spring_todo_rest_api.shared.api.response.ErrorMessageResponse;
import com.nn.spring_todo_rest_api.task.support.exception.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class TaskExceptionAdvisor {

    private static final Logger LOG = LoggerFactory.getLogger(TaskExceptionAdvisor.class);

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessageResponse taskNotFound(Exception e) {
        LOG.error(e.getMessage(), e);
        return new ErrorMessageResponse(e.getLocalizedMessage());
    }
}
