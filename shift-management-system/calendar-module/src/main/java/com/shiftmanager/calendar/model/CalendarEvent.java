package com.shiftmanager.calendar.model;

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
import jakarta.validation.constraints.NotBlank;
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
 * Entity representing a calendar event
 */
@Entity
@Table(name = "calendar_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarEvent extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private CalendarEventType eventType;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "all_day")
    private boolean allDay = false;

    @Column(name = "recurring")
    private boolean recurring = false;

    @Size(max = 255)
    @Column(name = "recurrence_pattern")
    private String recurrencePattern;

    @Column(name = "recurrence_end_date")
    private LocalDateTime recurrenceEndDate;

    @Size(max = 50)
    @Column(name = "color")
    private String color;

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @Size(max = 50)
    @Column(name = "related_entity_type")
    private String relatedEntityType;

    /**
     * Calculate the duration of the event in minutes
     * @return duration in minutes
     */
    public long getDurationMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    /**
     * Format the event time range as a string
     * @return formatted time range
     */
    public String getFormattedTimeRange() {
        if (allDay) {
            return "All day";
        }
        return String.format("%s - %s", 
                startTime.toLocalTime().toString(), 
                endTime.toLocalTime().toString());
    }
}
