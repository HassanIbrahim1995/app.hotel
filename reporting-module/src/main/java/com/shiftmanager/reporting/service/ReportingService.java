package com.shiftmanager.reporting.service;

import com.shiftmanager.reporting.dto.ReportDTO;

import java.time.LocalDate;

/**
 * Service interface for generating reports
 */
public interface ReportingService {

    /**
     * Generate an employee shift report
     * @param employeeId The employee ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    ReportDTO generateEmployeeShiftReport(Long employeeId, LocalDate startDate, LocalDate endDate);

    /**
     * Generate a manager shift report
     * @param managerId The manager ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    ReportDTO generateManagerShiftReport(Long managerId, LocalDate startDate, LocalDate endDate);

    /**
     * Generate a department shift report
     * @param department The department
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    ReportDTO generateDepartmentShiftReport(String department, LocalDate startDate, LocalDate endDate);

    /**
     * Generate a location shift report
     * @param locationId The location ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    ReportDTO generateLocationShiftReport(Long locationId, LocalDate startDate, LocalDate endDate);

    /**
     * Generate an employee vacation report
     * @param employeeId The employee ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    ReportDTO generateEmployeeVacationReport(Long employeeId, LocalDate startDate, LocalDate endDate);

    /**
     * Generate a department vacation report
     * @param department The department
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    ReportDTO generateDepartmentVacationReport(String department, LocalDate startDate, LocalDate endDate);

    /**
     * Generate a comprehensive employee report including shifts and vacations
     * @param employeeId The employee ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    ReportDTO generateComprehensiveEmployeeReport(Long employeeId, LocalDate startDate, LocalDate endDate);

    /**
     * Generate a comprehensive manager report including managed employees' shifts and vacations
     * @param managerId The manager ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    ReportDTO generateComprehensiveManagerReport(Long managerId, LocalDate startDate, LocalDate endDate);

    /**
     * Generate a comprehensive department report
     * @param department The department
     * @param startDate The start date
     * @param endDate The end date
     * @return The generated report
     */
    ReportDTO generateComprehensiveDepartmentReport(String department, LocalDate startDate, LocalDate endDate);

    /**
     * Generate a PDF report for the specified employee's shifts
     * @param employeeId The employee ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The PDF report as a byte array
     */
    byte[] generateEmployeeShiftReportPdf(Long employeeId, LocalDate startDate, LocalDate endDate);

    /**
     * Generate a PDF report for the specified manager's team
     * @param managerId The manager ID
     * @param startDate The start date
     * @param endDate The end date
     * @return The PDF report as a byte array
     */
    byte[] generateManagerTeamReportPdf(Long managerId, LocalDate startDate, LocalDate endDate);
}
