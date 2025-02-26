package com.example.spribgdatawithprojections;

import com.example.spribgdatawithprojections.Entity.DepartmentEntity;
import com.example.spribgdatawithprojections.Exceptions.DepartmentNotFoundException;
import com.example.spribgdatawithprojections.Repository.DepartmentEntityRepository;
import com.example.spribgdatawithprojections.Service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentEntityRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private DepartmentEntity testDepartment1;
    private DepartmentEntity testDepartment2;

    @BeforeEach
    void setUp() {
        testDepartment1 = DepartmentEntity.builder().id(1L).name("Sales").build();
        testDepartment2 = DepartmentEntity.builder().id(2L).name("Marketing").build();
    }

    @Test
    void getDepartmentById_ExistingDepartment_ReturnsDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment1));

        DepartmentEntity department = departmentService.getDepartmentById(1L);

        assertEquals(testDepartment1, department);
    }

    @Test
    void getDepartmentById_NonExistingDepartment_ThrowsDepartmentNotFoundException() {
        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        DepartmentNotFoundException exception = assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.getDepartmentById(999L));

        assertEquals("Department not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void getAllDepartments_DepartmentsExist_ReturnsListOfDepartments() {
        List<DepartmentEntity> departments = Arrays.asList(testDepartment1, testDepartment2);
        when(departmentRepository.findAll()).thenReturn(departments);

        List<DepartmentEntity> allDepartments = departmentService.getAllDepartments();

        assertEquals(departments, allDepartments);
    }

    @Test
    void getAllDepartments_NoDepartmentsExist_ReturnsEmptyList() {
        when(departmentRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        List<DepartmentEntity> allDepartments = departmentService.getAllDepartments();

        assertTrue(allDepartments.isEmpty());
    }

    @Test
    void createDepartment_ValidDepartment_ReturnsCreatedDepartment() {
        when(departmentRepository.save(testDepartment1)).thenReturn(testDepartment1);

        DepartmentEntity createdDepartment = departmentService.createDepartment(testDepartment1);

        assertEquals(testDepartment1, createdDepartment);
    }

    @Test
    void updateDepartment_ExistingDepartment_ReturnsUpdatedDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(testDepartment1));
        when(departmentRepository.save(any(DepartmentEntity.class))).thenReturn(testDepartment1); // Важно: save() может вернуть измененный объект

        DepartmentEntity updatedDepartmentDetails = DepartmentEntity.builder().name("Updated Sales").build();
        DepartmentEntity updatedDepartment = departmentService.updateDepartment(1L, updatedDepartmentDetails);

        assertEquals("Updated Sales", updatedDepartment.getName());
    }

    @Test
    void updateDepartment_NonExistingDepartment_ThrowsDepartmentNotFoundException() {
        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        DepartmentNotFoundException exception = assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.updateDepartment(999L, DepartmentEntity.builder().name("New Name").build()));

        assertEquals("Department not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void deleteDepartment_ExistingDepartment_DeletesDepartment() {
        departmentService.deleteDepartment(1L);
        verify(departmentRepository, times(1)).deleteById(1L);
    }
}
