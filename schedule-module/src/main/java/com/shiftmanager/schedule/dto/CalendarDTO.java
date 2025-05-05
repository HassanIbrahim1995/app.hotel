package com.shiftmanager.schedule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for calendar data with scheduled shifts and events
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarDTO {

    private String personName;
    private Long personId;
    private String personType; // "EMPLOYEE" or "MANAGER"
    private LocalDate startDate;
    private LocalDate endDate;
    private List<CalendarEntryDTO> entries;
    private Double totalScheduledHours;
    
    /**
     * Adds an entry to this calendar
     * @param entry The calendar entry to add
     */
    public void addEntry(CalendarEntryDTO entry) {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        entries.add(entry);
    }
    
    /**
     * DTO for a calendar entry (shift or vacation)
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalendarEntryDTO {
        private Long id;
        private String title;
        private LocalDate date;
        private String startTime;
        private String endTime;
        private String type; // "SHIFT", "VACATION", etc.
        private String status; // For vacation: "PENDING", "APPROVED", etc.
        private Double hours;
        private String color; // For UI display
        private String notes;
        
        /**
         * Creates a calendar entry for a shift
         */
        public static CalendarEntryDTO fromShift(Long id, String shiftType, LocalDate date, 
                                               String startTime, String endTime, Double hours, String notes) {
            return CalendarEntryDTO.builder()
                    .id(id)
                    .title(shiftType)
                    .date(date)
                    .startTime(startTime)
                    .endTime(endTime)
                    .type("SHIFT")
                    .hours(hours)
                    .color(getColorForShiftType(shiftType))
                    .notes(notes)
                    .build();
        }
        
        /**
         * Creates a calendar entry for a vacation
         */
        public static CalendarEntryDTO fromVacation(Long id, LocalDate date, String status, String notes) {
            return CalendarEntryDTO.builder()
                    .id(id)
                    .title("Vacation")
                    .date(date)
                    .type("VACATION")
                    .status(status)
                    .color(getColorForVacationStatus(status))
                    .notes(notes)
                    .build();
        }
        
        /**
         * Returns a color based on shift type for UI display
         */
        private static String getColorForShiftType(String shiftType) {
            if (shiftType == null) return "#808080"; // Gray default
            
            switch (shiftType) {
                case "Day Shift": return "#4CAF50"; // Green
                case "Evening Shift": return "#2196F3"; // Blue
                case "Night Shift": return "#9C27B0"; // Purple
                default: return "#FF9800"; // Orange for custom
            }
        }
        
        /**
         * Returns a color based on vacation status for UI display
         */
        private static String getColorForVacationStatus(String status) {
            if (status == null) return "#808080"; // Gray default
            
            switch (status) {
                case "APPROVED": return "#4CAF50"; // Green
                case "PENDING": return "#FFC107"; // Yellow
                case "REJECTED": return "#F44336"; // Red
                case "CANCELLED": return "#607D8B"; // Blue gray
                default: return "#808080"; // Gray
            }
        }
    }
}
