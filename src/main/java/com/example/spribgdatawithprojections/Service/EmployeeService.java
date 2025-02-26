package com.example.spribgdatawithprojections.Service;


import com.example.spribgdatawithprojections.Entity.EmployeeEntity;
import com.example.spribgdatawithprojections.Exceptions.EmployeeNotFoundException;
import com.example.spribgdatawithprojections.Projections.EmployeeProjection;
import com.example.spribgdatawithprojections.Repository.EmployeeEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeEntityRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeEntityRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public EmployeeProjection getEmployeeById(Long id) {
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
        if (employeeEntity.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found", HttpStatus.NOT_FOUND);
        }
        return convertToProjection(employeeEntity.get());
    }

    @Transactional(readOnly = true)
    public List<EmployeeProjection> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToProjection)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeProjection createEmployee(EmployeeEntity employee) {
        EmployeeEntity savedEmployee = employeeRepository.save(employee);
        return convertToProjection(savedEmployee);
    }

    @Transactional
    public EmployeeProjection updateEmployee(Long id, EmployeeEntity employee) {
        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    existingEmployee.setFirstName(employee.getFirstName());
                    existingEmployee.setLastName(employee.getLastName());
                    existingEmployee.setPosition(employee.getPosition());
                    existingEmployee.setSalary(employee.getSalary());
                    existingEmployee.setDepartment(employee.getDepartment());

                    EmployeeEntity updatedEmployee = employeeRepository.save(existingEmployee);

                    return convertToProjection(updatedEmployee);
                })
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    private EmployeeProjection convertToProjection(EmployeeEntity employee) {
        return new EmployeeProjection() {
            @Override
            public String getFullName() {
                return employee.getFirstName() + " " + employee.getLastName();
            }

            @Override
            public String getPosition() {
                return employee.getPosition();
            }

            @Override
            public String getDepartmentName() {
                return employee.getDepartment().getName();
            }
        };
    }
}
