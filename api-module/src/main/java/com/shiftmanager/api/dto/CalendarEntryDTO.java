package com.shiftmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for CalendarEntry
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEntryDTO {
    
    private Long id;
    private Long calendarId;
    private LocalDate entryDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String entryType;
    private String title;
    private String description;
    private boolean allDay;
    private Long referenceId;
    private String color;
    
    /**
     * Static builder method
     * @return Builder
     */
    public static CalendarEntryDTOBuilder builder() {
        return new CalendarEntryDTOBuilder();
    }
}