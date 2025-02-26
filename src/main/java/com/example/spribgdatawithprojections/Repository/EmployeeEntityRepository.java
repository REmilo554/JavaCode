package com.example.spribgdatawithprojections.Repository;


import com.example.spribgdatawithprojections.Entity.EmployeeEntity;
import com.example.spribgdatawithprojections.Projections.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeEntityRepository extends JpaRepository<EmployeeEntity, Long> {
    Optional<EmployeeProjection> findByEmployeeId(Long employeeId);
}
