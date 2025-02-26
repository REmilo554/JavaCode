package com.example.spribgdatawithprojections.Service;

import com.example.spribgdatawithprojections.Entity.DepartmentEntity;
import com.example.spribgdatawithprojections.Exceptions.DepartmentNotFoundException;
import com.example.spribgdatawithprojections.Repository.DepartmentEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentEntityRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentEntityRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public DepartmentEntity getDepartmentById(Long id) {
        Optional<DepartmentEntity> department = departmentRepository.findById(id);
        if(department.isEmpty()) {
            throw new DepartmentNotFoundException("Department not found", HttpStatus.NOT_FOUND);
        }
        return department.get();
    }

    @Transactional(readOnly = true)
    public List<DepartmentEntity> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Transactional
    public DepartmentEntity createDepartment(DepartmentEntity department) {
        return departmentRepository.save(department);
    }

    @Transactional
    public DepartmentEntity updateDepartment(Long id, DepartmentEntity departmentDetails) {
        return departmentRepository.findById(id)
                .map(existingDepartment -> {
                    existingDepartment.setName(departmentDetails.getName());
                    return departmentRepository.save(existingDepartment);
                })
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
