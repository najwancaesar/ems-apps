package com.localcentraldigital.ems.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.localcentraldigital.ems.model.Employee;
import com.localcentraldigital.ems.model.Role;
import com.localcentraldigital.ems.model.User;
import com.localcentraldigital.ems.repository.EmployeeRepository;
import com.localcentraldigital.ems.repository.UserRepository;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedDefaultUsers(
            UserRepository userRepository,
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!userRepository.existsByUsername("admin") && !userRepository.existsByEmail("admin@localcentraldigital.com")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@localcentraldigital.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setIsActive(true);
                userRepository.save(admin);
            }

            if (!userRepository.existsByUsername("emp001") && !userRepository.existsByEmail("emp001@localcentraldigital.com")) {
                User employeeUser = new User();
                employeeUser.setUsername("emp001");
                employeeUser.setEmail("emp001@localcentraldigital.com");
                employeeUser.setPassword(passwordEncoder.encode("user123"));
                employeeUser.setRole(Role.USER);
                employeeUser.setIsActive(true);

                employeeRepository.findAll()
                        .stream()
                        .filter(employee -> !userRepository.existsByEmployeeId(employee.getId()))
                        .findFirst()
                        .ifPresent(employeeUser::setEmployee);

                userRepository.save(employeeUser);
            }
        };
    }
}
