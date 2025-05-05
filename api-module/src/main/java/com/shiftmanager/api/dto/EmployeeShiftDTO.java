package com.shiftmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for EmployeeShift
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShiftDTO {
    
    private Long id;
    
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    private String employeeName;
    
    @NotNull(message = "Shift ID is required")
    private Long shiftId;
    
    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    private String status; // ASSIGNED, CONFIRMED, COMPLETED, etc.
}