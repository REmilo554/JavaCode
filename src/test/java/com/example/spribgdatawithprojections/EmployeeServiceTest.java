package com.example.spribgdatawithprojections;

import com.example.spribgdatawithprojections.Entity.DepartmentEntity;
import com.example.spribgdatawithprojections.Entity.EmployeeEntity;
import com.example.spribgdatawithprojections.Exceptions.EmployeeNotFoundException;
import com.example.spribgdatawithprojections.Projections.EmployeeProjection;
import com.example.spribgdatawithprojections.Repository.EmployeeEntityRepository;
import com.example.spribgdatawithprojections.Service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeEntityRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeEntity testEmployee1;
    private EmployeeEntity testEmployee2;
    private DepartmentEntity testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = DepartmentEntity.builder().id(1L).name("Sales").build();
        testEmployee1 = EmployeeEntity.builder()
                .employeeId(1L)
                .firstName("John")
                .lastName("Doe")
                .position("Manager")
                .salary(BigDecimal.valueOf(60000))
                .department(testDepartment)
                .build();
        testEmployee2 = EmployeeEntity.builder()
                .employeeId(2L)
                .firstName("Jane")
                .lastName("Smith")
                .position("Developer")
                .salary(BigDecimal.valueOf(70000))
                .department(testDepartment)
                .build();
    }

    @Test
    void getEmployeeById_ExistingEmployee_ReturnsEmployeeProjection() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(testEmployee1));

        EmployeeProjection actualProjection = employeeService.getEmployeeById(employeeId);

        assertEquals(testEmployee1.getFirstName() + " " + testEmployee1.getLastName(), actualProjection.getFullName());
        assertEquals(testEmployee1.getPosition(), actualProjection.getPosition());
        assertEquals(testEmployee1.getDepartment().getName(), actualProjection.getDepartmentName());
    }

    @Test
    void getEmployeeById_NonExistingEmployee_ThrowsEmployeeNotFoundException() {
        Long employeeId = 999L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeById(employeeId);
        });

        assertEquals("Employee not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void getAllEmployees_EmployeesExist_ReturnsListOfEmployeeProjections() {
        List<EmployeeEntity> employeeList = Arrays.asList(testEmployee1, testEmployee2);
        when(employeeRepository.findAll()).thenReturn(employeeList);

        List<EmployeeProjection> actualProjections = employeeService.getAllEmployees();

        assertEquals(employeeList.size(), actualProjections.size());
        assertEquals(testEmployee1.getFirstName() + " " + testEmployee1.getLastName(), actualProjections.get(0).getFullName());
        assertEquals(testEmployee2.getPosition(), actualProjections.get(1).getPosition());
    }

    @Test
    void getAllEmployees_NoEmployeesExist_ReturnsEmptyList() {
        when(employeeRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        List<EmployeeProjection> actualProjections = employeeService.getAllEmployees();

        assertTrue(actualProjections.isEmpty());
    }

    @Test
    void createEmployee_ValidEmployee_ReturnsCreatedEmployeeProjection() {
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(testEmployee1);

        EmployeeProjection createdEmployee = employeeService.createEmployee(testEmployee1);

        assertEquals(testEmployee1.getFirstName() + " " + testEmployee1.getLastName(), createdEmployee.getFullName());
        assertEquals(testEmployee1.getPosition(), createdEmployee.getPosition());
        assertEquals(testEmployee1.getDepartment().getName(), createdEmployee.getDepartmentName());
    }

    @Test
    void updateEmployee_ExistingEmployee_ReturnsUpdatedEmployeeProjection() {
        Long employeeId = 1L;
        EmployeeEntity updatedEmployeeDetails = EmployeeEntity.builder()
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .position("UpdatedPosition")
                .salary(BigDecimal.valueOf(70000))
                .department(testDepartment)
                .build();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(testEmployee1));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(testEmployee1);

        EmployeeProjection updatedProjection = employeeService.updateEmployee(employeeId, updatedEmployeeDetails);

        assertEquals(updatedEmployeeDetails.getFirstName() + " " + updatedEmployeeDetails.getLastName(), updatedProjection.getFullName());
        assertEquals(updatedEmployeeDetails.getPosition(), updatedProjection.getPosition());
        assertEquals(updatedEmployeeDetails.getDepartment().getName(), updatedProjection.getDepartmentName());
    }

    @Test
    void updateEmployee_NonExistingEmployee_ThrowsEmployeeNotFoundException() {
        Long employeeId = 999L;
        EmployeeEntity updatedEmployeeDetails = EmployeeEntity.builder()
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .position("UpdatedPosition")
                .salary(BigDecimal.valueOf(70000))
                .department(testDepartment)
                .build();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.updateEmployee(employeeId, updatedEmployeeDetails);
        });

        assertEquals("Employee not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void deleteEmployee_ExistingEmployee_DeletesEmployee() {
        Long employeeId = 1L;

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
