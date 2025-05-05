package com.shiftmanager.core.domain;

import lombok.Getter;

/**
 * Enum representing different types of shifts
 */
@Getter
public enum ShiftType {
    DAY("Day Shift", "08:00", "16:00"),
    EVENING("Evening Shift", "16:00", "00:00"),
    NIGHT("Night Shift", "00:00", "08:00"),
    CUSTOM("Custom Shift", null, null);

    private final String displayName;
    private final String defaultStartTime;
    private final String defaultEndTime;

    ShiftType(String displayName, String defaultStartTime, String defaultEndTime) {
        this.displayName = displayName;
        this.defaultStartTime = defaultStartTime;
        this.defaultEndTime = defaultEndTime;
    }
}
