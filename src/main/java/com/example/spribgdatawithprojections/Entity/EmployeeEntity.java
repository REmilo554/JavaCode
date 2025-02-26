package com.example.spribgdatawithprojections.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "employees")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    Long employeeId;

    @Column(name = "first_name")
    @NotBlank
    String firstName;

    @Column(name = "last_name")
    @NotBlank
    String lastName;

    @Column(name = "position")
    @NotBlank
    String position;

    @Column(name = "salary")
    @NotNull
    BigDecimal salary;

    @ManyToOne
    @JoinColumn(name = "department_department_id")
    DepartmentEntity department;
}
