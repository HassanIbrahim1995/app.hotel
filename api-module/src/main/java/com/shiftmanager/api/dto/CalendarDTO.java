package com.shiftmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Calendar
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDTO {
    
    private Long id;
    private Long employeeId;
    private String employeeName;
    private int year;
    private int month;
    
    /**
     * Static builder method
     * @return Builder
     */
    public static CalendarDTOBuilder builder() {
        return new CalendarDTOBuilder();
    }
}