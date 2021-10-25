package com.codercampus.api.service;

import com.codercampus.api.model.ERole;
import com.codercampus.api.model.Role;
import com.codercampus.api.model.User;
import com.codercampus.api.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public Optional<Role> findByName(ERole roleName){
        return this.roleRepository.findByName(roleName);
    }

}
