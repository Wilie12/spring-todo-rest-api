package com.nn.spring_todo_rest_api.user.service;

import com.nn.spring_todo_rest_api.user.domain.UserAccount;
import com.nn.spring_todo_rest_api.user.domain.UserAccountDetails;
import com.nn.spring_todo_rest_api.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAccountDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserAccountDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userRepository.findByUsername(username);

        return new UserAccountDetails(userAccount);
    }
}
