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
 * Employee entity extending Person base class
 */
@Entity
@DiscriminatorValue("EMPLOYEE")
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Employee extends Person {

    @NotNull
    private LocalDate hireDate;

    private LocalDate terminationDate;

    @NotNull
    private String employeeNumber;

    private String position;

    @NotNull
    private String department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Shift> shifts = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VacationRequest> vacationRequests = new HashSet<>();
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();
    
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Schedule schedule;
    
    /**
     * Helper method to add a shift to the employee
     * @param shift The shift to add
     */
    public void addShift(Shift shift) {
        shifts.add(shift);
        shift.setEmployee(this);
    }
    
    /**
     * Helper method to remove a shift from the employee
     * @param shift The shift to remove
     */
    public void removeShift(Shift shift) {
        shifts.remove(shift);
        shift.setEmployee(null);
    }

    /**
     * Helper method to add a vacation request
     * @param vacationRequest The vacation request to add
     */
    public void addVacationRequest(VacationRequest vacationRequest) {
        vacationRequests.add(vacationRequest);
        vacationRequest.setEmployee(this);
    }
    
    /**
     * Helper method to remove a vacation request
     * @param vacationRequest The vacation request to remove
     */
    public void removeVacationRequest(VacationRequest vacationRequest) {
        vacationRequests.remove(vacationRequest);
        vacationRequest.setEmployee(null);
    }
}
