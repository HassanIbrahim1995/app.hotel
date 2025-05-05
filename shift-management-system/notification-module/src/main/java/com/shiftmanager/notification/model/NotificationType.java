package com.shiftmanager.notification.model;

/**
 * Enum representing the possible types of notifications
 */
public enum NotificationType {
    SHIFT_ASSIGNED,      // New shift assigned to employee
    SHIFT_MODIFIED,      // Existing shift was modified
    SHIFT_CANCELED,      // Shift was canceled
    SHIFT_REMINDER,      // Reminder for upcoming shift
    TIME_OFF_APPROVED,   // Time off request was approved
    TIME_OFF_DENIED,     // Time off request was denied
    TIME_OFF_REQUESTED,  // New time off request from employee (for managers)
    SCHEDULE_PUBLISHED,  // New schedule published
    GENERAL_ANNOUNCEMENT // General company announcement
}
