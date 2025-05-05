package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * ShiftType entity for defining different types of shifts
 */
@Entity
@Table(name = "shift_type")
@Getter
@Setter
@NoArgsConstructor
public class ShiftType extends BaseEntity {

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @OneToMany(mappedBy = "shiftType")
    private Set<Shift> shifts = new HashSet<>();

    /**
     * Constructor with all fields
     * @param name Shift type name
     * @param description Shift type description
     * @param startTime Shift start time
     * @param endTime Shift end time
     */
    public ShiftType(String name, String description, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Calculate shift duration in hours
     * @return Duration in hours
     */
    @Transient
    public double getDurationHours() {
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int endMinutes = endTime.getHour() * 60 + endTime.getMinute();
        
        // Handle overnight shifts
        if (endMinutes < startMinutes) {
            endMinutes += 24 * 60; // Add 24 hours
        }
        
        return (endMinutes - startMinutes) / 60.0;
    }
}
