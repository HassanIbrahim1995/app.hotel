package com.shiftmanager.api.controller;

import com.shiftmanager.api.dto.CalendarDTO;
import com.shiftmanager.api.dto.CalendarEntryDTO;
import com.shiftmanager.api.mapper.CalendarEntryMapper;
import com.shiftmanager.api.mapper.CalendarMapper;
import com.shiftmanager.api.model.Calendar;
import com.shiftmanager.api.model.CalendarEntry;
import com.shiftmanager.api.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for calendar operations
 */
@RestController
@RequestMapping("/api/calendars")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private CalendarMapper calendarMapper;

    @Autowired
    private CalendarEntryMapper calendarEntryMapper;

    /**
     * Get all calendars (admin only)
     * @return List of calendars
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CalendarDTO>> getAllCalendars() {
        List<Calendar> calendars = calendarService.getAllCalendars();
        List<CalendarDTO> calendarDTOs = calendars.stream()
                .map(calendarMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(calendarDTOs);
    }

    /**
     * Get calendar by ID
     * @param id Calendar ID
     * @return Calendar
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isCalendarOwnerOrManager(#id)")
    public ResponseEntity<CalendarDTO> getCalendarById(@PathVariable Long id) {
        Calendar calendar = calendarService.getCalendarById(id);
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        return ResponseEntity.ok(calendarDTO);
    }

    /**
     * Get calendar entries
     * @param id Calendar ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of calendar entries
     */
    @GetMapping("/{id}/entries")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isCalendarOwnerOrManager(#id)")
    public ResponseEntity<List<CalendarEntryDTO>> getCalendarEntries(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<CalendarEntry> calendarEntries = calendarService.getCalendarEntries(id, startDate, endDate);
        List<CalendarEntryDTO> calendarEntryDTOs = calendarEntries.stream()
                .map(calendarEntryMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(calendarEntryDTOs);
    }

    /**
     * Create calendar entry
     * @param id Calendar ID
     * @param calendarEntryDTO Calendar entry data
     * @return Created calendar entry
     */
    @PostMapping("/{id}/entries")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isCalendarOwnerOrManager(#id)")
    public ResponseEntity<CalendarEntryDTO> createCalendarEntry(
            @PathVariable Long id,
            @Valid @RequestBody CalendarEntryDTO calendarEntryDTO) {

        CalendarEntry calendarEntry = calendarEntryMapper.toEntity(calendarEntryDTO);
        CalendarEntry createdCalendarEntry = calendarService.createCalendarEntry(id, calendarEntry);
        CalendarEntryDTO createdCalendarEntryDTO = calendarEntryMapper.toDto(createdCalendarEntry);

        return ResponseEntity.ok(createdCalendarEntryDTO);
    }

    /**
     * Update calendar entry
     * @param id Calendar ID
     * @param entryId Calendar entry ID
     * @param calendarEntryDTO Calendar entry data
     * @return Updated calendar entry
     */
    @PutMapping("/{id}/entries/{entryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isCalendarOwnerOrManager(#id)")
    public ResponseEntity<CalendarEntryDTO> updateCalendarEntry(
            @PathVariable Long id,
            @PathVariable Long entryId,
            @Valid @RequestBody CalendarEntryDTO calendarEntryDTO) {

        CalendarEntry calendarEntry = calendarEntryMapper.toEntity(calendarEntryDTO);
        CalendarEntry updatedCalendarEntry = calendarService.updateCalendarEntry(id, entryId, calendarEntry);
        CalendarEntryDTO updatedCalendarEntryDTO = calendarEntryMapper.toDto(updatedCalendarEntry);

        return ResponseEntity.ok(updatedCalendarEntryDTO);
    }

    /**
     * Delete calendar entry
     * @param id Calendar ID
     * @param entryId Calendar entry ID
     * @return Delete status
     */
    @DeleteMapping("/{id}/entries/{entryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isCalendarOwnerOrManager(#id)")
    public ResponseEntity<Map<String, Boolean>> deleteCalendarEntry(
            @PathVariable Long id,
            @PathVariable Long entryId) {

        calendarService.deleteCalendarEntry(id, entryId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return ResponseEntity.ok(response);
    }

    /**
     * Export calendar to PDF
     * @param id Calendar ID
     * @param year Year
     * @param month Month
     * @return PDF file
     */
    @GetMapping("/{id}/export/pdf")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isCalendarOwnerOrManager(#id)")
    public ResponseEntity<byte[]> exportCalendarToPdf(
            @PathVariable Long id,
            @RequestParam Integer year,
            @RequestParam Integer month) {

        byte[] pdfBytes = calendarService.exportCalendarToPdf(id, year, month);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=calendar-" + year + "-" + month + ".pdf")
                .body(pdfBytes);
    }
}