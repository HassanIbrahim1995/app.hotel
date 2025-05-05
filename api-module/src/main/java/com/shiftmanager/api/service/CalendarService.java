package com.shiftmanager.api.service;

import com.shiftmanager.api.model.Calendar;
import com.shiftmanager.api.model.CalendarEntry;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for calendar operations
 */
public interface CalendarService {
    
    /**
     * Get calendar by ID
     * @param calendarId Calendar ID
     * @return Calendar
     */
    Calendar getCalendarById(Long calendarId);
    
    /**
     * Get calendar by employee ID
     * @param employeeId Employee ID
     * @return Calendar
     */
    Calendar getCalendarByEmployeeId(Long employeeId);
    
    /**
     * Get or create calendar for employee
     * @param employeeId Employee ID
     * @return Calendar
     */
    Calendar getOrCreateCalendarForEmployee(Long employeeId);
    
    /**
     * Get entries for a calendar
     * @param calendarId Calendar ID
     * @return List of calendar entries
     */
    List<CalendarEntry> getCalendarEntries(Long calendarId);
    
    /**
     * Get entries for a calendar and date
     * @param calendarId Calendar ID
     * @param date Date
     * @return List of calendar entries
     */
    List<CalendarEntry> getCalendarEntriesForDate(Long calendarId, LocalDate date);
    
    /**
     * Get entries for a calendar and date range
     * @param calendarId Calendar ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of calendar entries
     */
    List<CalendarEntry> getCalendarEntriesForDateRange(Long calendarId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get entries by entry type
     * @param calendarId Calendar ID
     * @param entryType Entry type
     * @return List of calendar entries
     */
    List<CalendarEntry> getCalendarEntriesByType(Long calendarId, String entryType);
    
    /**
     * Add entry to calendar
     * @param calendarId Calendar ID
     * @param entry Calendar entry
     * @return Created calendar entry
     */
    CalendarEntry addCalendarEntry(Long calendarId, CalendarEntry entry);
    
    /**
     * Update calendar entry
     * @param entryId Entry ID
     * @param entry Calendar entry
     * @return Updated calendar entry
     */
    CalendarEntry updateCalendarEntry(Long entryId, CalendarEntry entry);
    
    /**
     * Delete calendar entry
     * @param entryId Entry ID
     * @return true if deleted
     */
    boolean deleteCalendarEntry(Long entryId);
    
    /**
     * Sync employee shifts to calendar
     * @param employeeId Employee ID
     * @return Number of entries created
     */
    int syncEmployeeShiftsToCalendar(Long employeeId);
    
    /**
     * Sync employee vacation requests to calendar
     * @param employeeId Employee ID
     * @return Number of entries created
     */
    int syncEmployeeVacationRequestsToCalendar(Long employeeId);
    
    /**
     * Get calendars for a manager's team
     * @param managerId Manager ID
     * @return List of calendars
     */
    List<Calendar> getTeamCalendars(Long managerId);
    
    /**
     * Get all calendars in the system
     * @return List of all calendars
     */
    List<Calendar> getAllCalendars();
    
    /**
     * Create a calendar entry for a specific calendar
     * @param calendarId Calendar ID
     * @param entry Calendar entry
     * @return Created calendar entry
     */
    CalendarEntry createCalendarEntry(Long calendarId, CalendarEntry entry);
    
    /**
     * Update a calendar entry
     * @param calendarId Calendar ID
     * @param entryId Entry ID
     * @param entry Updated calendar entry
     * @return Updated calendar entry
     */
    CalendarEntry updateCalendarEntry(Long calendarId, Long entryId, CalendarEntry entry);
    
    /**
     * Delete a calendar entry from a calendar
     * @param calendarId Calendar ID
     * @param entryId Entry ID
     * @return true if deleted
     */
    boolean deleteCalendarEntry(Long calendarId, Long entryId);
    
    /**
     * Get calendar entries for a specific period
     * @param calendarId Calendar ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of calendar entries
     */
    List<CalendarEntry> getCalendarEntries(Long calendarId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Export employee calendar to PDF
     * @param employeeId Employee ID
     * @param month Month (1-12)
     * @param year Year
     * @return PDF data as byte array
     */
    byte[] exportCalendarToPdf(Long employeeId, Integer month, Integer year);
    
    /**
     * Get employee calendar for a specific month
     * @param employeeId Employee ID
     * @param month Month (1-12)
     * @param year Year
     * @return List of calendar entries
     */
    List<CalendarEntry> getEmployeeCalendar(Long employeeId, Integer month, Integer year);
}