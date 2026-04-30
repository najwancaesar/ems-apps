package com.localcentraldigital.ems.service;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.localcentraldigital.ems.dto.CurrentUserResponse;
import com.localcentraldigital.ems.dto.LoginRequest;
import com.localcentraldigital.ems.dto.LoginResponse;
import com.localcentraldigital.ems.exception.ResourceNotFoundException;
import com.localcentraldigital.ems.model.Employee;
import com.localcentraldigital.ems.model.User;
import com.localcentraldigital.ems.repository.UserRepository;
import com.localcentraldigital.ems.security.JwtService;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername().trim();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, request.getPassword())
        );

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user);
        Employee employee = user.getEmployee();

        return new LoginResponse(
                token,
                "Bearer",
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                employee == null ? null : employee.getId()
        );
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AuthenticationCredentialsNotFoundException("Authentication is required");
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Employee employee = user.getEmployee();

        return new CurrentUserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                employee == null ? null : employee.getId(),
                employee == null ? null : employee.getFullName()
        );
    }
}
