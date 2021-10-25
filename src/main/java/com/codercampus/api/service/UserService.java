package com.codercampus.api.service;

import com.codercampus.api.model.User;
import com.codercampus.api.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean existsByUsername(String userName){
        return this.userRepository.existsByUsername(userName);
    }

    public boolean existsByEmail(String email){
        return this.userRepository.existsByEmail(email);
    }

    public void save(User user){
        this.userRepository.save(user);
    }

}
