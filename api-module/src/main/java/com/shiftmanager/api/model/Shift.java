package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Shift entity for storing shift details
 */
@Entity
@Table(name = "shift")
@Getter
@Setter
@NoArgsConstructor
public class Shift extends BaseEntity {

    @NotNull(message = "Shift date is required")
    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_type_id", nullable = false)
    private ShiftType shiftType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeShift> employeeShifts = new HashSet<>();

    /**
     * Constructor with all fields
     * @param shiftDate Date of the shift
     * @param shiftType Type of shift
     * @param location Location of the shift
     */
    public Shift(LocalDate shiftDate, ShiftType shiftType, Location location) {
        this.shiftDate = shiftDate;
        this.shiftType = shiftType;
        this.location = location;
    }

    /**
     * Add an employee shift assignment
     * @param employeeShift The employee shift to add
     */
    public void addEmployeeShift(EmployeeShift employeeShift) {
        employeeShifts.add(employeeShift);
        employeeShift.setShift(this);
    }

    /**
     * Remove an employee shift assignment
     * @param employeeShift The employee shift to remove
     */
    public void removeEmployeeShift(EmployeeShift employeeShift) {
        employeeShifts.remove(employeeShift);
        employeeShift.setShift(null);
    }
}
