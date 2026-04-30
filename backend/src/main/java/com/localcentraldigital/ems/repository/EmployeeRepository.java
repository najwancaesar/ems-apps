package com.localcentraldigital.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.localcentraldigital.ems.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmployeeCodeIgnoreCase(String employeeCode);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmployeeCodeIgnoreCaseAndIdNot(String employeeCode, Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
