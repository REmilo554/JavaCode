package com.example.spribgdatawithprojections.Projections;

import org.springframework.beans.factory.annotation.Value;

public interface EmployeeProjection {
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();

    String getPosition();

    @Value("#{target.department.name}")
    String getDepartmentName();
}
