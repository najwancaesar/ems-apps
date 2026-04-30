package com.localcentraldigital.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.localcentraldigital.ems.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByNameIgnoreCase(String name);
}
