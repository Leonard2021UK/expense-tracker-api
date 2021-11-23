package com.codercampus.api.service;

import com.codercampus.api.model.User;
import com.codercampus.api.repository.UserRepository;
import com.codercampus.api.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    /**
     * Returns logged-in user
     * @return UserDetailsImpl
     */
    public UserDetailsImpl getUserDetails (){
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Optional<User> findById(Long id){
        return this.userRepository.findById(id);
    }



}
