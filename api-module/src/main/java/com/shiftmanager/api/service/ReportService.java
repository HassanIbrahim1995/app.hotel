package com.shiftmanager.api.service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for generating reports
 */
public interface ReportService {

    /**
     * Generate employee schedule report
     * @param employeeId Employee ID
     * @param startDate Start date
     * @param endDate End date
     * @return PDF report as byte array
     */
    byte[] generateEmployeeScheduleReport(Long employeeId, LocalDate startDate, LocalDate endDate);

    /**
     * Generate team schedule report
     * @param managerId Manager ID
     * @param startDate Start date
     * @param endDate End date
     * @return PDF report as byte array
     */
    byte[] generateTeamScheduleReport(Long managerId, LocalDate startDate, LocalDate endDate);

    /**
     * Generate location schedule report
     * @param locationId Location ID
     * @param startDate Start date
     * @param endDate End date
     * @return PDF report as byte array
     */
    byte[] generateLocationScheduleReport(Long locationId, LocalDate startDate, LocalDate endDate);

    /**
     * Generate vacation summary report
     * @param year Year
     * @param month Optional month (if not provided, full year report)
     * @param departmentId Optional department ID filter
     * @return PDF report as byte array
     */
    byte[] generateVacationSummaryReport(Integer year, Integer month, Long departmentId);

    /**
     * Generate hours worked report
     * @param employeeIds List of employee IDs (or null for all employees)
     * @param startDate Start date
     * @param endDate End date
     * @return PDF report as byte array
     */
    byte[] generateHoursWorkedReport(List<Long> employeeIds, LocalDate startDate, LocalDate endDate);
}