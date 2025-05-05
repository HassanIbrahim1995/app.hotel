package com.shiftmanager.vacation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shiftmanager.core.domain.VacationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for VacationRequest entity operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VacationRequestDTO {

    private Long id;
    
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    
    private String employeeName;
    
    private Long managerId;
    
    private String managerName;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private VacationStatus status;
    
    private String reason;
    
    private String managerComments;
    
    private LocalDateTime reviewedAt;
    
    private Long daysCount;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    /**
     * Validates that the vacation request dates are valid (end date not before start date)
     * @return true if valid, false otherwise
     */
    public boolean isValidVacationDates() {
        return startDate != null && endDate != null && (endDate.isEqual(startDate) || endDate.isAfter(startDate));
    }
}
