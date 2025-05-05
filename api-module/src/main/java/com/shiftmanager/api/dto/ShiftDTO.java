package com.shiftmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

/**
 * Data Transfer Object for Shift
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {
    
    private Long id;
    
    @NotNull(message = "Shift date is required")
    private LocalDate shiftDate;
    
    // ShiftType information (start/end times come from the shift type)
    private Long shiftTypeId;
    private String shiftTypeName;
    private LocalTime startTime;
    private LocalTime endTime;
    
    // Location information
    private Long locationId;
    private String locationName;
    
    // Calculated fields
    private Double durationHours;
    private Boolean isOvertime;
    
    // Employee assignments
    private Set<EmployeeShiftDTO> employeeShifts;
    
    // For simple creation/updates
    private String status; // Used when assigning an employee directly
}
