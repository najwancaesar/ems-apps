package com.localcentraldigital.ems.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.localcentraldigital.ems.dto.EmployeeRequest;
import com.localcentraldigital.ems.dto.EmployeeResponse;
import com.localcentraldigital.ems.exception.DuplicateResourceException;
import com.localcentraldigital.ems.exception.ResourceNotFoundException;
import com.localcentraldigital.ems.model.Department;
import com.localcentraldigital.ems.model.Employee;
import com.localcentraldigital.ems.model.EmploymentStatus;
import com.localcentraldigital.ems.model.Position;
import com.localcentraldigital.ems.repository.DepartmentRepository;
import com.localcentraldigital.ems.repository.EmployeeRepository;
import com.localcentraldigital.ems.repository.PositionRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            PositionRepository positionRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = findEmployeeById(id);
        return toResponse(employee);
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        String employeeCode = request.getEmployeeCode().trim();
        String email = request.getEmail().trim();

        validateUniqueEmployeeCode(employeeCode);
        validateUniqueEmail(email);

        Department department = findDepartmentById(request.getDepartmentId());
        Position position = findPositionById(request.getPositionId());

        Employee employee = new Employee();
        applyRequest(employee, request, department, position);
        employee.setEmployeeCode(employeeCode);
        employee.setEmail(email);
        employee.setEmploymentStatus(resolveEmploymentStatus(request.getEmploymentStatus()));

        Employee savedEmployee = employeeRepository.save(employee);
        return toResponse(savedEmployee);
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = findEmployeeById(id);

        String employeeCode = request.getEmployeeCode().trim();
        String email = request.getEmail().trim();

        validateUniqueEmployeeCodeForUpdate(employeeCode, id);
        validateUniqueEmailForUpdate(email, id);

        Department department = findDepartmentById(request.getDepartmentId());
        Position position = findPositionById(request.getPositionId());

        applyRequest(employee, request, department, position);
        employee.setEmployeeCode(employeeCode);
        employee.setEmail(email);
        if (request.getEmploymentStatus() != null) {
            employee.setEmploymentStatus(request.getEmploymentStatus());
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return toResponse(savedEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = findEmployeeById(id);
        employee.setEmploymentStatus(EmploymentStatus.INACTIVE);
        employeeRepository.save(employee);
    }

    private void applyRequest(Employee employee, EmployeeRequest request, Department department, Position position) {
        employee.setFullName(request.getFullName().trim());
        employee.setPhone(trim(request.getPhone()));
        employee.setAddress(trim(request.getAddress()));
        employee.setGender(trim(request.getGender()));
        employee.setBirthDate(request.getBirthDate());
        employee.setHireDate(request.getHireDate());
        employee.setDepartment(department);
        employee.setPosition(position);
    }

    private EmploymentStatus resolveEmploymentStatus(EmploymentStatus employmentStatus) {
        if (employmentStatus == null) {
            return EmploymentStatus.ACTIVE;
        }
        return employmentStatus;
    }

    private String trim(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    private Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    private Department findDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
    }

    private Position findPositionById(Long id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Position not found with id: " + id));
    }

    private void validateUniqueEmployeeCode(String employeeCode) {
        if (employeeRepository.existsByEmployeeCodeIgnoreCase(employeeCode)) {
            throw new DuplicateResourceException("Employee code already exists");
        }
    }

    private void validateUniqueEmail(String email) {
        if (employeeRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Employee email already exists");
        }
    }

    private void validateUniqueEmployeeCodeForUpdate(String employeeCode, Long id) {
        if (employeeRepository.existsByEmployeeCodeIgnoreCaseAndIdNot(employeeCode, id)) {
            throw new DuplicateResourceException("Employee code already exists");
        }
    }

    private void validateUniqueEmailForUpdate(String email, Long id) {
        if (employeeRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new DuplicateResourceException("Employee email already exists");
        }
    }

    private EmployeeResponse toResponse(Employee employee) {
        Department department = employee.getDepartment();
        Position position = employee.getPosition();

        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getFullName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getAddress(),
                employee.getGender(),
                employee.getBirthDate(),
                employee.getHireDate(),
                employee.getEmploymentStatus(),
                department.getId(),
                department.getName(),
                position.getId(),
                position.getName(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }
}
