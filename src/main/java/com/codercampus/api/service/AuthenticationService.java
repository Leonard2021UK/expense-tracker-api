package com.codercampus.api.service;

import com.codercampus.api.payload.request.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    /**
     *
     * @param loginRequest - login request body
     * @return an {Authentication} populated by the authenticated user details
     */
    public Authentication authenticateUser(LoginRequest loginRequest){
        // UsernamePasswordAuthenticationToken is a type of Authentication object which is passed to the
        // authentication manager to authenticate th user. UsernamePasswordAuthenticationToken uses userDetail, userdetailService and passwordEncoder
        // to retrieve and authenticate the user.
       return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
    }

    /**
     * Sets authenticated user into the security context
     * @param authentication - authenticated user
     */
    public void setAuthenticationInSecurityContext(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
