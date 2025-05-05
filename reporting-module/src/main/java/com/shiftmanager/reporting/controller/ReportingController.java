package com.shiftmanager.reporting.controller;

import com.shiftmanager.reporting.dto.ReportDTO;
import com.shiftmanager.reporting.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST controller for reporting operations
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    /**
     * Generate an employee shift report
     * @param employeeId The employee ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    @GetMapping("/employee/{employeeId}/shifts")
    public ResponseEntity<ReportDTO> getEmployeeShiftReport(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportingService.generateEmployeeShiftReport(employeeId, startDate, endDate));
    }

    /**
     * Generate a manager shift report
     * @param managerId The manager ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    @GetMapping("/manager/{managerId}/shifts")
    public ResponseEntity<ReportDTO> getManagerShiftReport(
            @PathVariable Long managerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportingService.generateManagerShiftReport(managerId, startDate, endDate));
    }

    /**
     * Generate a department shift report
     * @param department The department
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    @GetMapping("/department/{department}/shifts")
    public ResponseEntity<ReportDTO> getDepartmentShiftReport(
            @PathVariable String department,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportingService.generateDepartmentShiftReport(department, startDate, endDate));
    }

    /**
     * Generate a location shift report
     * @param locationId The location ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    @GetMapping("/location/{locationId}/shifts")
    public ResponseEntity<ReportDTO> getLocationShiftReport(
            @PathVariable Long locationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportingService.generateLocationShiftReport(locationId, startDate, endDate));
    }

    /**
     * Generate an employee vacation report
     * @param employeeId The employee ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    @GetMapping("/employee/{employeeId}/vacations")
    public ResponseEntity<ReportDTO> getEmployeeVacationReport(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportingService.generateEmployeeVacationReport(employeeId, startDate, endDate));
    }

    /**
     * Generate a department vacation report
     * @param department The department
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    @GetMapping("/department/{department}/vacations")
    public ResponseEntity<ReportDTO> getDepartmentVacationReport(
            @PathVariable String department,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportingService.generateDepartmentVacationReport(department, startDate, endDate));
    }

    /**
     * Generate a comprehensive employee report
     * @param employeeId The employee ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    @GetMapping("/employee/{employeeId}/comprehensive")
    public ResponseEntity<ReportDTO> getComprehensiveEmployeeReport(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportingService.generateComprehensiveEmployeeReport(employeeId, startDate, endDate));
    }

    /**
     * Generate a comprehensive manager report
     * @param managerId The manager ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    @GetMapping("/manager/{managerId}/comprehensive")
    public ResponseEntity<ReportDTO> getComprehensiveManagerReport(
            @PathVariable Long managerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportingService.generateComprehensiveManagerReport(managerId, startDate, endDate));
    }

    /**
     * Generate a comprehensive department report
     * @param department The department
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    @GetMapping("/department/{department}/comprehensive")
    public ResponseEntity<ReportDTO> getComprehensiveDepartmentReport(
            @PathVariable String department,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportingService.generateComprehensiveDepartmentReport(department, startDate, endDate));
    }

    /**
     * Generate a PDF report for the specified employee's shifts
     * @param employeeId The employee ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The PDF report
     */
    @GetMapping("/employee/{employeeId}/shifts/pdf")
    public ResponseEntity<byte[]> getEmployeeShiftReportPdf(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        byte[] pdfBytes = reportingService.generateEmployeeShiftReportPdf(employeeId, startDate, endDate);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "employee_shift_report_" + employeeId + ".pdf";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    /**
     * Generate a PDF report for the specified manager's team
     * @param managerId The manager ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The PDF report
     */
    @GetMapping("/manager/{managerId}/team/pdf")
    public ResponseEntity<byte[]> getManagerTeamReportPdf(
            @PathVariable Long managerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        byte[] pdfBytes = reportingService.generateManagerTeamReportPdf(managerId, startDate, endDate);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "manager_team_report_" + managerId + ".pdf";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
