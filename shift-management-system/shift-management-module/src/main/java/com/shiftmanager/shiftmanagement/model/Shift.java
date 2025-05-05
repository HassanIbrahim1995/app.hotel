package com.shiftmanager.shiftmanagement.model;

import com.shiftmanager.common.model.BaseEntity;
import com.shiftmanager.common.model.Location;
import com.shiftmanager.employeemanagement.model.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Entity representing a scheduled shift for an employee
 */
@Entity
@Table(name = "shifts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift extends BaseEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "shift_definition_id", nullable = false)
    private ShiftDefinition shiftDefinition;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ShiftStatus status = ShiftStatus.SCHEDULED;

    @Size(max = 255)
    @Column(name = "notes")
    private String notes;

    @Column(name = "break_duration_minutes")
    private Integer breakDurationMinutes;

    /**
     * Calculate the scheduled duration of this shift in minutes
     * @return scheduled duration in minutes
     */
    public long getScheduledDurationMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    /**
     * Calculate the actual duration of this shift in minutes
     * @return actual duration in minutes, or 0 if shift hasn't been completed
     */
    public long getActualDurationMinutes() {
        if (actualStartTime != null && actualEndTime != null) {
            return Duration.between(actualStartTime, actualEndTime).toMinutes();
        }
        return 0;
    }

    /**
     * Check if the shift has been completed
     * @return true if shift is completed
     */
    public boolean isCompleted() {
        return status == ShiftStatus.COMPLETED;
    }

    /**
     * Check if the shift has been canceled
     * @return true if shift is canceled
     */
    public boolean isCanceled() {
        return status == ShiftStatus.CANCELED;
    }
}
