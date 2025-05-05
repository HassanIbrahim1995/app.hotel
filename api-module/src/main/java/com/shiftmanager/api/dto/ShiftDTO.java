package com.shiftmanager.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftDTO {
    private Long id;
    
    @NotNull(message = "Shift date is required")
    @FutureOrPresent(message = "Shift date cannot be in the past")
    private LocalDate shiftDate;
    
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    @NotNull(message = "End time is required")
    private LocalTime endTime;
    
    @NotNull(message = "Location is required")
    private LocationDTO location;
    
    @NotNull(message = "Shift type is required")
    private ShiftTypeDTO shiftType;
    
    private String note;
    
    private Long createdById;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
