package com.shiftmanager.reporting.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for various reports
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDTO {

    private String reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime generatedAt;
    private Long employeeId;
    private String employeeName;
    private Long managerId;
    private String managerName;
    private Long locationId;
    private String locationName;
    private String department;
    
    @Builder.Default
    private Double totalHours = 0.0;
    
    @Builder.Default
    private Long totalShifts = 0L;
    
    private List<ShiftReportEntryDTO> shifts;
    private List<VacationReportEntryDTO> vacations;
    private List<EmployeeReportSummaryDTO> employees;
    private List<DepartmentReportDTO> departments;
    
    /**
     * DTO for reporting shifts details
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShiftReportEntryDTO {
        private Long shiftId;
        private String employeeName;
        private String shiftType;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Double hours;
    }
    
    /**
     * DTO for reporting vacation details
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VacationReportEntryDTO {
        private Long vacationId;
        private String employeeName;
        private LocalDate startDate;
        private LocalDate endDate;
        private Long days;
        private String status;
    }
    
    /**
     * DTO for reporting employee summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeReportSummaryDTO {
        private Long employeeId;
        private String employeeName;
        private String department;
        private String position;
        private Double totalHours;
        private Long totalShifts;
        private Long vacationDays;
    }
    
    /**
     * DTO for reporting department summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentReportDTO {
        private String department;
        private Long employeeCount;
        private Double totalHours;
        private Long totalShifts;
        private Double averageHoursPerEmployee;
    }
}
