package com.shiftmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for VacationRequest
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacationRequestDTO {
    
    private Long id;
    
    // Employee information
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    private String employeeName;
    
    // Request details
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    @Size(max = 255, message = "Request notes cannot exceed 255 characters")
    private String requestNotes;
    
    // Status information
    private String status; // PENDING, APPROVED, REJECTED
    
    // Review information
    private Long reviewerId;
    private String reviewerName;
    private LocalDateTime reviewedAt;
    
    @Size(max = 255, message = "Review notes cannot exceed 255 characters")
    private String reviewNotes;
    
    // Calculated field
    private Integer durationDays;
}
