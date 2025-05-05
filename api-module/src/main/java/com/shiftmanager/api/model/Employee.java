package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Employee entity - extends Person
 */
@Entity
@DiscriminatorValue("EMPLOYEE")
@Getter
@Setter
@NoArgsConstructor
public class Employee extends Person {

    @NotBlank(message = "Employee ID is required")
    @Size(max = 20, message = "Employee ID cannot exceed 20 characters")
    @Column(name = "employee_id", nullable = false, unique = true)
    private String employeeId;

    @NotNull(message = "Hire date is required")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @NotBlank(message = "Job title is required")
    @Size(max = 100, message = "Job title cannot exceed 100 characters")
    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Size(max = 100, message = "Department cannot exceed 100 characters")
    @Column(name = "department")
    private String department;

    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private Set<Employee> subordinates = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    private Set<EmployeeShift> shifts = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    private Set<VacationRequest> vacationRequests = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    private Set<Notification> notifications = new HashSet<>();

    /**
     * Add a subordinate employee
     * @param employee The subordinate to add
     */
    public void addSubordinate(Employee employee) {
        subordinates.add(employee);
        employee.setManager(this);
    }

    /**
     * Remove a subordinate employee
     * @param employee The subordinate to remove
     */
    public void removeSubordinate(Employee employee) {
        subordinates.remove(employee);
        employee.setManager(null);
    }

    /**
     * Check if employee is active
     * @return true if employee is active
     */
    @Transient
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }
}
