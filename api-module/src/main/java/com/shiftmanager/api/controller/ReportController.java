package com.shiftmanager.api.controller;

import com.shiftmanager.api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for generating various reports
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private ReportService reportService;

    /**
     * Generate employee schedule report
     * @param employeeId Employee ID
     * @param startDate Start date
     * @param endDate End date
     * @return PDF report
     */
    @GetMapping("/employee-schedule")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isSelfOrManager(#employeeId)")
    public ResponseEntity<byte[]> generateEmployeeScheduleReport(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        byte[] reportBytes = reportService.generateEmployeeScheduleReport(employeeId, startDate, endDate);
        
        String filename = "employee-schedule-" + employeeId + "-" + startDate + "-to-" + endDate + ".pdf";
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(reportBytes);
    }

    /**
     * Generate team schedule report
     * @param managerId Manager ID
     * @param startDate Start date
     * @param endDate End date
     * @return PDF report
     */
    @GetMapping("/team-schedule")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<byte[]> generateTeamScheduleReport(
            @RequestParam Long managerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        byte[] reportBytes = reportService.generateTeamScheduleReport(managerId, startDate, endDate);
        
        String filename = "team-schedule-" + managerId + "-" + startDate + "-to-" + endDate + ".pdf";
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(reportBytes);
    }

    /**
     * Generate location schedule report
     * @param locationId Location ID
     * @param startDate Start date
     * @param endDate End date
     * @return PDF report
     */
    @GetMapping("/location-schedule")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<byte[]> generateLocationScheduleReport(
            @RequestParam Long locationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        byte[] reportBytes = reportService.generateLocationScheduleReport(locationId, startDate, endDate);
        
        String filename = "location-schedule-" + locationId + "-" + startDate + "-to-" + endDate + ".pdf";
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(reportBytes);
    }

    /**
     * Generate vacation summary report
     * @param year Year
     * @param month Optional month (if not provided, full year report)
     * @param departmentId Optional department ID filter
     * @return PDF report
     */
    @GetMapping("/vacation-summary")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<byte[]> generateVacationSummaryReport(
            @RequestParam Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Long departmentId) {
        
        byte[] reportBytes = reportService.generateVacationSummaryReport(year, month, departmentId);
        
        String filename = "vacation-summary-" + year;
        if (month != null) {
            filename += "-" + month;
        }
        if (departmentId != null) {
            filename += "-dept-" + departmentId;
        }
        filename += ".pdf";
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(reportBytes);
    }

    /**
     * Generate hours worked report
     * @param employeeIds List of employee IDs (or null for all employees)
     * @param startDate Start date
     * @param endDate End date
     * @return PDF report
     */
    @GetMapping("/hours-worked")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<byte[]> generateHoursWorkedReport(
            @RequestParam(required = false) List<Long> employeeIds,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        byte[] reportBytes = reportService.generateHoursWorkedReport(employeeIds, startDate, endDate);
        
        String filename = "hours-worked-" + startDate + "-to-" + endDate + ".pdf";
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(reportBytes);
    }
}