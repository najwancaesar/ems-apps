package com.localcentraldigital.ems.dto;

import com.localcentraldigital.ems.model.Role;

public class CurrentUserResponse {

    private Long id;
    private String username;
    private String email;
    private Role role;
    private Long employeeId;
    private String employeeName;

    public CurrentUserResponse(Long id, String username, String email, Role role, Long employeeId, String employeeName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }

    public Long getId() {
        return id;
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

    public String getEmployeeName() {
        return employeeName;
    }
}
