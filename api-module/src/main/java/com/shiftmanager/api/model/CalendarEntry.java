package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * CalendarEntry entity - represents an event in an employee's calendar
 */
@Entity
@Table(name = "calendar_entry")
@Getter
@Setter
@NoArgsConstructor
public class CalendarEntry extends BaseEntity {

    @NotNull(message = "Calendar is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;
    
    @NotNull(message = "Entry date is required")
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;
    
    @Column(name = "start_time")
    private LocalTime startTime;
    
    @Column(name = "end_time")
    private LocalTime endTime;
    
    @NotBlank(message = "Entry type is required")
    @Size(max = 50, message = "Entry type cannot exceed 50 characters")
    @Column(name = "entry_type", nullable = false)
    private String entryType;  // SHIFT, VACATION, HOLIDAY, NOTE, etc.
    
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    @Column(name = "title", nullable = false)
    private String title;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(name = "description")
    private String description;
    
    @Column(name = "all_day")
    private boolean allDay = false;
    
    @Column(name = "reference_id")
    private Long referenceId;  // ID of related entity (Shift, VacationRequest, etc.)
    
    @Size(max = 50, message = "Color cannot exceed 50 characters")
    @Column(name = "color")
    private String color;  // CSS color for UI display
    
    /**
     * Constructor for a shift-based calendar entry
     * @param calendar Calendar to add entry to
     * @param shift Shift to create entry for
     */
    public CalendarEntry(Calendar calendar, Shift shift) {
        this.calendar = calendar;
        this.entryDate = shift.getShiftDate();
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        this.entryType = "SHIFT";
        this.title = shift.getShiftType().getName() + " Shift - " + shift.getLocation().getName();
        this.description = shift.getNotes();
        this.referenceId = shift.getId();
        // Default color based on shift type
        if (shift.getShiftType().getName().contains("DAY")) {
            this.color = "#4CAF50"; // Green
        } else if (shift.getShiftType().getName().contains("EVENING")) {
            this.color = "#2196F3"; // Blue
        } else if (shift.getShiftType().getName().contains("NIGHT")) {
            this.color = "#9C27B0"; // Purple
        } else {
            this.color = "#FF9800"; // Orange
        }
    }
    
    /**
     * Constructor for a vacation request-based calendar entry
     * @param calendar Calendar to add entry to
     * @param vacationRequest Vacation request to create entry for
     */
    public CalendarEntry(Calendar calendar, VacationRequest vacationRequest) {
        this.calendar = calendar;
        this.entryDate = vacationRequest.getStartDate(); // First day of vacation
        this.entryType = "VACATION";
        this.title = "Vacation";
        this.description = vacationRequest.getReason();
        this.allDay = true;
        this.referenceId = vacationRequest.getId();
        this.color = "#F44336"; // Red
    }
    
    /**
     * Basic constructor
     * @param calendar Calendar to add entry to
     * @param entryDate Date of the entry
     * @param entryType Type of entry
     * @param title Entry title
     */
    public CalendarEntry(Calendar calendar, LocalDate entryDate, String entryType, String title) {
        this.calendar = calendar;
        this.entryDate = entryDate;
        this.entryType = entryType;
        this.title = title;
    }
    
    /**
     * Full constructor
     * @param calendar Calendar to add entry to
     * @param entryDate Date of the entry
     * @param startTime Start time of the entry
     * @param endTime End time of the entry
     * @param entryType Type of entry
     * @param title Entry title
     * @param description Entry description
     * @param allDay Whether the entry is all day
     * @param referenceId ID of related entity
     * @param color CSS color for UI display
     */
    public CalendarEntry(Calendar calendar, LocalDate entryDate, LocalTime startTime, LocalTime endTime,
                        String entryType, String title, String description, boolean allDay,
                        Long referenceId, String color) {
        this.calendar = calendar;
        this.entryDate = entryDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.entryType = entryType;
        this.title = title;
        this.description = description;
        this.allDay = allDay;
        this.referenceId = referenceId;
        this.color = color;
    }
}