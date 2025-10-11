package com.nn.spring_todo_rest_api.task.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "tasks")
public class Task {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String title;
    private String description;
    private boolean isCompleted;

    protected Task() {}

    public Task(
            String username,
            String title,
            String description
    ) {
        this.username = username;
        this.title = title;
        this.description = description;
        isCompleted = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean completed) {
        isCompleted = completed;
    }
}
