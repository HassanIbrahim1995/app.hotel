package com.shiftmanager.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employee")
@DiscriminatorValue("EMPLOYEE")
@Getter
@Setter
@NoArgsConstructor
public class Employee extends Person {

    @Column(name = "employee_id", nullable = false, unique = true)
    private String employeeId;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "department")
    private String department;

    @Column(name = "position")
    private String position;

    @Column(name = "hourly_rate", nullable = false)
    private BigDecimal hourlyRate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    public Employee(String firstName, String lastName, String email, String employeeId, LocalDate hireDate, BigDecimal hourlyRate) {
        super(firstName, lastName, email);
        this.employeeId = employeeId;
        this.hireDate = hireDate;
        this.hourlyRate = hourlyRate;
    }

    public enum EmployeeStatus {
        ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
    }
}
