package com.shiftmanager.shiftmanagement.model;

/**
 * Enum representing the possible shift statuses
 */
public enum ShiftStatus {
    SCHEDULED,  // Shift is scheduled but hasn't started yet
    IN_PROGRESS, // Shift has started but not ended
    COMPLETED,  // Shift has completed normally
    CANCELED,   // Shift was canceled
    NO_SHOW     // Employee didn't show up for the shift
}
