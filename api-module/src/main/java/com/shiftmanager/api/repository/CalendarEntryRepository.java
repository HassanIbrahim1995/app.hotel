package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.Calendar;
import com.shiftmanager.api.model.CalendarEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for CalendarEntry entity
 */
@Repository
public interface CalendarEntryRepository extends JpaRepository<CalendarEntry, Long> {
    
    /**
     * Find entries by calendar
     * @param calendar Calendar
     * @return List of calendar entries
     */
    List<CalendarEntry> findByCalendar(Calendar calendar);
    
    /**
     * Find entries by calendar ID
     * @param calendarId Calendar ID
     * @return List of calendar entries
     */
    List<CalendarEntry> findByCalendarId(Long calendarId);
    
    /**
     * Find entries by calendar and entry date
     * @param calendar Calendar
     * @param entryDate Entry date
     * @return List of calendar entries
     */
    List<CalendarEntry> findByCalendarAndEntryDate(Calendar calendar, LocalDate entryDate);
    
    /**
     * Find entries by calendar ID and entry date
     * @param calendarId Calendar ID
     * @param entryDate Entry date
     * @return List of calendar entries
     */
    List<CalendarEntry> findByCalendarIdAndEntryDate(Long calendarId, LocalDate entryDate);
    
    /**
     * Find entries by calendar and entry type
     * @param calendar Calendar
     * @param entryType Entry type
     * @return List of calendar entries
     */
    List<CalendarEntry> findByCalendarAndEntryType(Calendar calendar, String entryType);
    
    /**
     * Find entries by calendar ID and entry type
     * @param calendarId Calendar ID
     * @param entryType Entry type
     * @return List of calendar entries
     */
    List<CalendarEntry> findByCalendarIdAndEntryType(Long calendarId, String entryType);
    
    /**
     * Find entries for a date range
     * @param calendarId Calendar ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of calendar entries
     */
    @Query("SELECT e FROM CalendarEntry e WHERE e.calendar.id = :calendarId AND e.entryDate BETWEEN :startDate AND :endDate ORDER BY e.entryDate ASC")
    List<CalendarEntry> findByCalendarIdAndDateRange(
            @Param("calendarId") Long calendarId, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find entries by reference ID
     * @param referenceId Reference ID
     * @return List of calendar entries
     */
    List<CalendarEntry> findByReferenceId(Long referenceId);
    
    /**
     * Find entries by entry type and reference ID
     * @param entryType Entry type
     * @param referenceId Reference ID
     * @return List of calendar entries
     */
    List<CalendarEntry> findByEntryTypeAndReferenceId(String entryType, Long referenceId);
}