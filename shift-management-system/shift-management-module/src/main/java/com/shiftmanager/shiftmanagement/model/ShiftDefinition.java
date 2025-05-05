package com.shiftmanager.shiftmanagement.model;

import com.shiftmanager.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

/**
 * Entity representing a shift definition template
 */
@Entity
@Table(name = "shift_definitions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftDefinition extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type", nullable = false)
    private ShiftType shiftType;

    @Column(name = "break_duration_minutes")
    private Integer breakDurationMinutes;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "is_default")
    private boolean isDefault = false;

    /**
     * Calculate the duration of this shift in minutes
     * @return total minutes for the shift
     */
    public int getDurationMinutes() {
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int endMinutes = endTime.getHour() * 60 + endTime.getMinute();
        
        // If end time is earlier than start time, assume it's the next day
        if (endMinutes < startMinutes) {
            endMinutes += 24 * 60; // Add a full day of minutes
        }
        
        return endMinutes - startMinutes;
    }

    /**
     * Calculate the paid duration of this shift (total duration minus breaks)
     * @return paid minutes for the shift
     */
    public int getPaidDurationMinutes() {
        int totalMinutes = getDurationMinutes();
        return totalMinutes - (breakDurationMinutes != null ? breakDurationMinutes : 0);
    }
}
