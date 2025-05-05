package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * EmployeeShift entity for mapping employees to shifts
 */
@Entity
@Table(name = "employee_shift")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeShift extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    @Column(name = "status", nullable = false)
    private String status;

    /**
     * Constructor with all fields
     * @param employee Associated employee
     * @param shift Associated shift
     * @param status Assignment status (e.g., ASSIGNED, CONFIRMED, COMPLETED)
     */
    public EmployeeShift(Employee employee, Shift shift, String status) {
        this.employee = employee;
        this.shift = shift;
        this.status = status;
    }

    /**
     * Check if shift is confirmed
     * @return true if confirmed
     */
    @Transient
    public boolean isConfirmed() {
        return "CONFIRMED".equalsIgnoreCase(status);
    }

    /**
     * Check if shift is completed
     * @return true if completed
     */
    @Transient
    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(status);
    }
}
