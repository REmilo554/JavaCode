package com.example.spribgdatawithprojections.Repository;


import com.example.spribgdatawithprojections.Entity.EmployeeEntity;
import com.example.spribgdatawithprojections.Projections.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeEntityRepository extends JpaRepository<EmployeeEntity, Long> {
    EmployeeProjection findByEmployeeId(Long employeeId);
}
