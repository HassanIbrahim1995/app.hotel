package com.shiftmanager.shift.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shiftmanager.core.domain.ShiftType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Shift entity operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShiftDTO {

    private Long id;
    
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    
    private String employeeName;
    
    @NotNull(message = "Schedule ID is required")
    private Long scheduleId;
    
    @NotNull(message = "Shift type is required")
    private ShiftType shiftType;
    
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;
    
    @NotNull(message = "End time is required")
    private LocalDateTime endTime;
    
    private String notes;
    
    private Double durationInHours;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    /**
     * Validates if the shift times are valid (end time after start time)
     * @return true if valid, false otherwise
     */
    public boolean isValidShiftTimes() {
        return startTime != null && endTime != null && endTime.isAfter(startTime);
    }
}
