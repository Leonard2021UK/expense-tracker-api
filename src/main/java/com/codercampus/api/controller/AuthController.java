package com.codercampus.api.controller;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.codercampus.api.exception.RefreshTokenException;
import com.codercampus.api.model.RefreshToken;
import com.codercampus.api.payload.request.RefreshTokenRequest;
import com.codercampus.api.payload.response.RefreshTokenResponse;
import com.codercampus.api.service.AuthenticationService;
import com.codercampus.api.service.RefreshTokenService;
import com.codercampus.api.service.RoleService;
import com.codercampus.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codercampus.api.model.ERole;
import com.codercampus.api.model.Role;
import com.codercampus.api.model.User;
import com.codercampus.api.payload.request.LoginRequest;
import com.codercampus.api.payload.request.SignupRequest;
import com.codercampus.api.payload.response.JwtResponse;
import com.codercampus.api.payload.response.MessageResponse;
import com.codercampus.api.repository.RoleRepository;
import com.codercampus.api.repository.UserRepository;
import com.codercampus.api.security.jwt.JwtUtils;
import com.codercampus.api.security.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationService authenticationService;

    RefreshTokenService refreshTokenService;

    UserService userService;

    RoleService roleService;

    PasswordEncoder encoder;

    JwtUtils jwtUtils;

    public AuthController(
        AuthenticationService authenticationService,
        UserService userService,
        RoleService roleService,
        PasswordEncoder encoder,
        JwtUtils jwtUtils,
        RefreshTokenService refreshTokenService
    ){
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @CrossOrigin("{http://localhost:3000}")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = this.authenticationService.authenticateUser(loginRequest);

        this.authenticationService.setAuthenticationInSecurityContext(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        // Get authenticated user
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Get roles associated with the authenticated user
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());


        //        HttpHeaders headers = new HttpHeaders();
//        headers.add("Location","localhost:3000/admin");
//        return ResponseEntity.status(200).body(new JwtResponse(jwt,
//                userDetails.getId(),
//                userDetails.getUsername(),
//                userDetails.getEmail(),
//                roles));
        return ResponseEntity.ok(
                new JwtResponse(jwt,
                refreshToken,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        // Check if username already exists
        if (this.userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check if email already exists
        if (this.userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                this.encoder.encode(signUpRequest.getPassword())
        );

        // Roles present in the request as String
        Set<String> strRoles = signUpRequest.getRole();

        // Roles converted from String to Role
        Set<Role> roles = new HashSet<>();

        // If role was not specified in the request set ROLE_USER as the role of the new user.
        if (strRoles == null) {

            Role userRole = this.roleService.findByName(ERole.ROLE_USER)
                    // TODO Handle runtime exception
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            roles.add(userRole);

        } else {
            // converts String role names to Role entity
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = this.roleService.findByName(ERole.ROLE_ADMIN)
                                // TODO Handle runtime exception
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = this.roleService.findByName(ERole.ROLE_PAID_USER)
                                // TODO Handle runtime exception
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = this.roleService.findByName(ERole.ROLE_USER)
                                // TODO Handle runtime exception
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        // Creates the relationship between the new user and its role, then saves the user
        user.setRoles(roles);
        this.userService.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> getRefreshToke(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return this.refreshTokenService.findByToken(requestRefreshToken)
                .map(this.refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new RefreshTokenResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RefreshTokenException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
