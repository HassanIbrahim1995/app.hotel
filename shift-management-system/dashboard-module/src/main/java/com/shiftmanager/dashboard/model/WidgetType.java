package com.shiftmanager.dashboard.model;

/**
 * Enum representing the possible dashboard widget types
 */
public enum WidgetType {
    EMPLOYEE_COUNT,             // Total employee count
    SCHEDULED_HOURS,            // Total scheduled hours
    UPCOMING_SHIFTS,            // List of upcoming shifts
    RECENT_TIME_OFF_REQUESTS,   // Recent time off requests
    SHIFT_DISTRIBUTION_CHART,   // Chart showing shift distribution
    LOCATION_COVERAGE_CHART,    // Chart showing location coverage
    ABSENCE_RATE_CHART,         // Chart showing absence rates
    EMPLOYEE_UTILIZATION_CHART, // Chart showing employee utilization
    CUSTOM_METRIC               // Custom user-defined metric
}
