package com.nn.spring_todo_rest_api.user.repository;

import com.nn.spring_todo_rest_api.user.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUsername(String username);
}
