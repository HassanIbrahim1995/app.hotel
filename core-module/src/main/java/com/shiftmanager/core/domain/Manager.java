package com.shiftmanager.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Manager entity extending Person base class
 */
@Entity
@DiscriminatorValue("MANAGER")
@Table(name = "managers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Manager extends Person {

    @NotNull
    private LocalDate hireDate;

    private LocalDate terminationDate;

    @NotNull
    private String managerNumber;

    @NotNull
    private String managerLevel;
    
    private String department;
    
    @OneToMany(mappedBy = "manager", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Employee> managedEmployees = new HashSet<>();

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private Set<VacationRequest> handledVacationRequests = new HashSet<>();

    @OneToOne(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    private Schedule schedule;

    /**
     * Helper method to add an employee to be managed by this manager
     * @param employee The employee to add
     */
    public void addEmployee(Employee employee) {
        managedEmployees.add(employee);
        employee.setManager(this);
    }
    
    /**
     * Helper method to remove an employee from being managed by this manager
     * @param employee The employee to remove
     */
    public void removeEmployee(Employee employee) {
        managedEmployees.remove(employee);
        employee.setManager(null);
    }

    /**
     * Helper method to handle a vacation request
     * @param vacationRequest The vacation request to handle
     */
    public void handleVacationRequest(VacationRequest vacationRequest) {
        handledVacationRequests.add(vacationRequest);
        vacationRequest.setManager(this);
    }
}
