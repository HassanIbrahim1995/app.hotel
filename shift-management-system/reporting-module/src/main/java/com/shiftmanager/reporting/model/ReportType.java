package com.shiftmanager.reporting.model;

/**
 * Enum representing the possible types of reports
 */
public enum ReportType {
    EMPLOYEE_HOURS,      // Hours worked by employees
    LOCATION_COVERAGE,   // Coverage analysis by location
    SHIFT_DISTRIBUTION,  // Distribution of shifts among employees
    ABSENCE_REPORT,      // Employee absences and time-off
    OVERTIME_REPORT,     // Overtime analysis
    COST_ANALYSIS,       // Cost analysis of staff hours
    SCHEDULE_EFFICIENCY, // Schedule efficiency analysis
    CUSTOM_REPORT        // Custom user-defined report
}
