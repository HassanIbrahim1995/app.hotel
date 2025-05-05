package com.shiftmanager.employeemanagement.model;

import com.shiftmanager.common.model.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing an employee in the system
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee extends Person {

    @Column(name = "employee_id", unique = true, nullable = false)
    private String employeeId;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type")
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status")
    private EmploymentStatus employmentStatus;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "department")
    private String department;

    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;

    @Column(name = "weekly_hours")
    private Double weeklyHours;

    @Column(name = "vacation_days")
    private Integer vacationDays;

    @Column(name = "sick_days")
    private Integer sickDays;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Column(name = "notes")
    private String notes;

    @Column(name = "special_requirements")
    private String specialRequirements;

    @Column(name = "terminated_date")
    private LocalDate terminatedDate;

    /**
     * Enumeration for different types of employment
     */
    public enum EmploymentType {
        FULL_TIME, PART_TIME, CONTRACT, TEMPORARY, INTERN
    }

    /**
     * Enumeration for employment status
     */
    public enum EmploymentStatus {
        ACTIVE, ON_LEAVE, TERMINATED, SUSPENDED
    }
}
