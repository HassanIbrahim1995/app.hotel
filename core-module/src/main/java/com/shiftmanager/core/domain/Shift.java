package com.shiftmanager.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Entity representing a work shift
 */
@Entity
@Table(name = "shifts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private String notes;

    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Calculates the duration of the shift in hours
     * @return Duration in hours
     */
    @Transient
    public double getDurationInHours() {
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes() / 60.0;
    }

    /**
     * Checks if this shift overlaps with another shift
     * @param other The other shift to check against
     * @return true if shifts overlap, false otherwise
     */
    public boolean overlaps(Shift other) {
        return (startTime.isBefore(other.endTime) && endTime.isAfter(other.startTime));
    }
}
