package com.shiftmanager.schedule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for Schedule entity operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleDTO {

    private Long id;
    
    @NotBlank(message = "Schedule name is required")
    private String name;
    
    private Long employeeId;
    
    private String employeeName;
    
    private Long managerId;
    
    private String managerName;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private List<ShiftSummaryDTO> shifts;
    
    private String notes;
    
    private Double totalHours;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    /**
     * Validates that the schedule dates are valid (end date after start date)
     * @return true if valid, false otherwise
     */
    public boolean isValidScheduleDates() {
        return startDate != null && endDate != null && (endDate.isEqual(startDate) || endDate.isAfter(startDate));
    }
    
    /**
     * DTO for summarizing shift information in a schedule
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShiftSummaryDTO {
        private Long id;
        private String shiftType;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Double durationInHours;
    }
    
    /**
     * Adds a shift summary to this schedule
     * @param shift The shift summary to add
     */
    public void addShift(ShiftSummaryDTO shift) {
        if (shifts == null) {
            shifts = new ArrayList<>();
        }
        shifts.add(shift);
    }
}
