package com.localcentraldigital.ems.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.localcentraldigital.ems.model.EmploymentStatus;

public class EmployeeResponse {

    private Long id;
    private String employeeCode;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private EmploymentStatus employmentStatus;
    private Long departmentId;
    private String departmentName;
    private Long positionId;
    private String positionName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public EmployeeResponse(
            Long id,
            String employeeCode,
            String fullName,
            String email,
            String phone,
            String address,
            String gender,
            LocalDate birthDate,
            LocalDate hireDate,
            EmploymentStatus employmentStatus,
            Long departmentId,
            String departmentName,
            Long positionId,
            String positionName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.employmentStatus = employmentStatus;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.positionId = positionId;
        this.positionName = positionName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Long getPositionId() {
        return positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
