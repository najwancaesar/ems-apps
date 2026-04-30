package com.localcentraldigital.ems.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.localcentraldigital.ems.dto.DepartmentRequest;
import com.localcentraldigital.ems.dto.DepartmentResponse;
import com.localcentraldigital.ems.exception.DuplicateResourceException;
import com.localcentraldigital.ems.model.Department;
import com.localcentraldigital.ems.repository.DepartmentRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DepartmentResponse createDepartment(DepartmentRequest request) {
        String name = request.getName().trim();
        if (departmentRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Department name already exists");
        }

        Department department = new Department();
        department.setName(name);
        department.setDescription(request.getDescription());

        Department savedDepartment = departmentRepository.save(department);
        return toResponse(savedDepartment);
    }

    private DepartmentResponse toResponse(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getCreatedAt(),
                department.getUpdatedAt()
        );
    }
}
