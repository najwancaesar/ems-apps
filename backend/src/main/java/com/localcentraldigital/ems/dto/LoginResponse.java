package com.localcentraldigital.ems.dto;

import com.localcentraldigital.ems.model.Role;

public class LoginResponse {

    private String token;
    private String tokenType;
    private String username;
    private String email;
    private Role role;
    private Long employeeId;

    public LoginResponse(String token, String tokenType, String username, String email, Role role, Long employeeId) {
        this.token = token;
        this.tokenType = tokenType;
        this.username = username;
        this.email = email;
        this.role = role;
        this.employeeId = employeeId;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
}
