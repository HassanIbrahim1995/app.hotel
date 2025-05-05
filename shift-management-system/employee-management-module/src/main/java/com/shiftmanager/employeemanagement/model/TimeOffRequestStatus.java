package com.shiftmanager.employeemanagement.model;

/**
 * Enum representing the possible statuses for time off requests
 */
public enum TimeOffRequestStatus {
    PENDING,   // Request is pending approval
    APPROVED,  // Request has been approved
    DENIED,    // Request has been denied
    CANCELED   // Request was canceled by the employee
}
