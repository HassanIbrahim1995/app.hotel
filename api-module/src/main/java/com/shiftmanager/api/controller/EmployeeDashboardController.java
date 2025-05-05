package com.shiftmanager.api.controller;

import com.shiftmanager.api.dto.*;
import com.shiftmanager.api.mapper.*;
import com.shiftmanager.api.model.*;
import com.shiftmanager.api.service.CalendarService;
import com.shiftmanager.api.service.EmployeeService;
import com.shiftmanager.api.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for employee dashboard operations
 */
@RestController
@RequestMapping("/api/employee/dashboard")
@PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMIN')")
public class EmployeeDashboardController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private ShiftMapper shiftMapper;

    @Autowired
    private VacationRequestMapper vacationRequestMapper;

    @Autowired
    private CalendarMapper calendarMapper;

    @Autowired
    private CalendarEntryMapper calendarEntryMapper;

    /**
     * Get employee dashboard overview data
     * @return Dashboard overview data
     */
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getDashboardOverview() {
        Long employeeId = getCurrentEmployeeId();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today.plusDays(30);
        
        // Get employee details
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(employeeId);
        
        // Get upcoming shifts
        List<Shift> upcomingShifts = shiftService.getEmployeeShifts(employeeId, today, endDate);
        List<ShiftDTO> upcomingShiftDTOs = upcomingShifts.stream()
                .map(shiftMapper::toDto)
                .collect(Collectors.toList());
        
        // Get vacation requests
        List<VacationRequest> vacationRequests = employeeService.getEmployeeVacationRequests(employeeId);
        List<VacationRequestDTO> vacationRequestDTOs = vacationRequests.stream()
                .map(vacationRequestMapper::toDto)
                .collect(Collectors.toList());
        
        // Get calendar
        Calendar calendar = calendarService.getOrCreateCalendarForEmployee(employeeId);
        
        // Get calendar entries
        List<CalendarEntry> calendarEntries = calendarService.getCalendarEntriesForDateRange(calendar.getId(), today, endDate);
        List<CalendarEntryDTO> calendarEntryDTOs = calendarEntries.stream()
                .map(calendarEntryMapper::toDto)
                .collect(Collectors.toList());
        
        // Get unread notifications count
        int unreadNotifications = employeeService.getUnreadNotificationsCount(employeeId);
        
        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("employee", employeeDTO);
        response.put("upcomingShifts", upcomingShiftDTOs);
        response.put("vacationRequests", vacationRequestDTOs);
        response.put("calendarEntries", calendarEntryDTOs);
        response.put("unreadNotifications", unreadNotifications);
        response.put("today", today);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get employee upcoming shifts
     * @param days Number of days to look ahead (default 7)
     * @return List of upcoming shifts
     */
    @GetMapping("/upcoming-shifts")
    public ResponseEntity<List<ShiftDTO>> getUpcomingShifts(@RequestParam(defaultValue = "7") Integer days) {
        Long employeeId = getCurrentEmployeeId();
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);
        
        List<Shift> upcomingShifts = shiftService.getEmployeeShifts(employeeId, today, endDate);
        List<ShiftDTO> upcomingShiftDTOs = upcomingShifts.stream()
                .map(shiftMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(upcomingShiftDTOs);
    }

    /**
     * Get employee monthly calendar
     * @param year Year (default current year)
     * @param month Month (default current month)
     * @return Calendar for specified month
     */
    @GetMapping("/calendar")
    public ResponseEntity<Map<String, Object>> getMonthlyCalendar(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        
        Long employeeId = getCurrentEmployeeId();
        
        // Use current year and month if not specified
        LocalDate now = LocalDate.now();
        if (year == null) {
            year = now.getYear();
        }
        if (month == null) {
            month = now.getMonthValue();
        }
        
        // Get the calendar entries
        List<CalendarEntry> entries = calendarService.getEmployeeCalendar(employeeId, year, month);
        
        // Get the calendar for metadata
        Calendar calendar = calendarService.getOrCreateCalendarForEmployee(employeeId);
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);
        
        // Map entries to DTOs
        List<CalendarEntryDTO> entryDTOs = entries.stream()
                .map(calendarEntryMapper::toDto)
                .collect(Collectors.toList());
        
        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("calendar", calendarDTO);
        response.put("entries", entryDTOs);
        response.put("year", year);
        response.put("month", month);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get employee calendar entries for a date range
     * @param startDate Start date (default today)
     * @param endDate End date (default today + 30 days)
     * @return List of calendar entries
     */
    @GetMapping("/calendar/entries")
    public ResponseEntity<List<CalendarEntryDTO>> getCalendarEntries(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Long employeeId = getCurrentEmployeeId();
        
        // Use default dates if not specified
        LocalDate now = LocalDate.now();
        if (startDate == null) {
            startDate = now;
        }
        if (endDate == null) {
            endDate = now.plusDays(30);
        }
        
        // Get the calendar for the employee
        Calendar calendar = calendarService.getOrCreateCalendarForEmployee(employeeId);
        
        // Get the entries for the date range
        List<CalendarEntry> calendarEntries = calendarService.getCalendarEntriesForDateRange(
                calendar.getId(), startDate, endDate);
                
        List<CalendarEntryDTO> calendarEntryDTOs = calendarEntries.stream()
                .map(calendarEntryMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(calendarEntryDTOs);
    }

    /**
     * Get employee vacation requests
     * @param status Optional status filter
     * @return List of vacation requests
     */
    @GetMapping("/vacation-requests")
    public ResponseEntity<List<VacationRequestDTO>> getVacationRequests(@RequestParam(required = false) String status) {
        Long employeeId = getCurrentEmployeeId();
        
        List<VacationRequest> vacationRequests = employeeService.getEmployeeVacationRequestsByStatus(employeeId, status);
        List<VacationRequestDTO> vacationRequestDTOs = vacationRequests.stream()
                .map(vacationRequestMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(vacationRequestDTOs);
    }

    /**
     * Create a new vacation request
     * @param vacationRequestDTO Vacation request data
     * @return Created vacation request
     */
    @PostMapping("/vacation-requests")
    public ResponseEntity<VacationRequestDTO> createVacationRequest(@RequestBody VacationRequestDTO vacationRequestDTO) {
        Long employeeId = getCurrentEmployeeId();
        
        VacationRequest vacationRequest = vacationRequestMapper.toEntity(vacationRequestDTO);
        VacationRequest createdVacationRequest = employeeService.createVacationRequest(employeeId, vacationRequest);
        VacationRequestDTO createdVacationRequestDTO = vacationRequestMapper.toDto(createdVacationRequest);
        
        return ResponseEntity.ok(createdVacationRequestDTO);
    }

    /**
     * Check for vacation request conflicts
     * @param startDate Start date
     * @param endDate End date
     * @param requestId Optional request ID to exclude from check
     * @return Conflict check result
     */
    @GetMapping("/vacation-requests/check-conflicts")
    public ResponseEntity<Map<String, Boolean>> checkVacationConflicts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long requestId) {
        
        Long employeeId = getCurrentEmployeeId();
        
        boolean hasConflicts = employeeService.hasVacationConflicts(employeeId, startDate, endDate, requestId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasConflicts", hasConflicts);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get employee notifications
     * @param unreadOnly Filter by unread only
     * @return List of notifications
     */
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@RequestParam(defaultValue = "false") boolean unreadOnly) {
        Long employeeId = getCurrentEmployeeId();
        
        List<Notification> notifications = employeeService.getEmployeeNotifications(employeeId, unreadOnly);
        List<NotificationDTO> notificationDTOs = notifications.stream()
                .map(notification -> {
                    NotificationDTO dto = new NotificationDTO();
                    dto.setId(notification.getId());
                    dto.setMessage(notification.getMessage());
                    dto.setType(notification.getType());
                    dto.setCreatedAt(notification.getCreatedAt());
                    dto.setRead(notification.isRead());
                    dto.setReferenceId(notification.getReferenceId());
                    return dto;
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(notificationDTOs);
    }

    /**
     * Mark notification as read
     * @param notificationId Notification ID
     * @return Success status
     */
    @PutMapping("/notifications/{notificationId}/read")
    public ResponseEntity<Map<String, Boolean>> markNotificationAsRead(@PathVariable Long notificationId) {
        Long employeeId = getCurrentEmployeeId();
        
        employeeService.markNotificationAsRead(employeeId, notificationId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", Boolean.TRUE);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Mark all notifications as read
     * @return Success status
     */
    @PutMapping("/notifications/read-all")
    public ResponseEntity<Map<String, Boolean>> markAllNotificationsAsRead() {
        Long employeeId = getCurrentEmployeeId();
        
        employeeService.markAllNotificationsAsRead(employeeId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", Boolean.TRUE);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get employee statistics
     * @param year Year (default current year)
     * @param month Month (optional - if not provided, returns full year stats)
     * @return Employee statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getEmployeeStatistics(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        
        Long employeeId = getCurrentEmployeeId();
        
        // Use current year if not specified
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        
        // Get statistics
        Map<String, Object> statistics = employeeService.getEmployeeStatistics(employeeId, year, month);
        
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get the calendar as a PDF export
     * @param year Year
     * @param month Month
     * @return PDF byte array
     */
    @GetMapping("/calendar/export/pdf")
    public ResponseEntity<byte[]> exportCalendarToPdf(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        
        Long employeeId = getCurrentEmployeeId();
        
        // Export to PDF directly using employee ID
        byte[] pdfBytes = calendarService.exportCalendarToPdf(employeeId, year, month);
        
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=calendar-" + year + "-" + month + ".pdf")
                .body(pdfBytes);
    }

    /**
     * Get the current employee ID from the authenticated user
     * @return Employee ID
     */
    private Long getCurrentEmployeeId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // This would typically come from a user service that would look up the employee ID
        // For now, we'll assume the username is the employee ID for simplicity
        // In a real application, you would have a UserService.getEmployeeIdByUsername method
        
        // This is a placeholder - in a real app, replace with actual lookup
        // Long employeeId = userService.getEmployeeIdByUsername(username);
        
        // For demonstration, we'll hardcode it to 1
        return 1L;
    }
}