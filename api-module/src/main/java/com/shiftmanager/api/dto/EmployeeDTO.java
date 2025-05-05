package com.shiftmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

/**
 * Data Transfer Object for Employee
 * Extends PersonDTO since Employee is a subclass of Person
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmployeeDTO extends PersonDTO {
    
    @NotBlank(message = "Employee ID is required")
    @Pattern(regexp = "^[A-Z0-9]{5,15}$", message = "Employee ID must be 5-15 alphanumeric characters")
    private String employeeId;
    
    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;
    
    private LocalDate terminationDate;
    
    private Boolean isActive;
    
    // Employee's manager ID if applicable
    private Long managerId;
    
    // Location where employee is primarily assigned
    private Long locationId;
    
    private String locationName;
    
    // Role information
    private Long roleId;
    
    private String roleName;
    
    // Upcoming shifts for calendar view
    private Set<ShiftDTO> upcomingShifts;
}
