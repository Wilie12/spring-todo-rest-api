package com.nn.spring_todo_rest_api.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
public class UserAccount {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String password;
    private List<String> authorities = new ArrayList<>();

    protected UserAccount() {}

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        authorities.add("USER");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}
