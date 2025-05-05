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

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for employee operations
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

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
     * Get all employees (admin only)
     * @return List of employees
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeDTO> employeeDTOs = employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(employeeDTOs);
    }

    /**
     * Get employee by ID
     * @param id Employee ID
     * @return Employee
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isSelfOrManager(#id)")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        return ResponseEntity.ok(employeeDTO);
    }

    /**
     * Create a new employee (admin only)
     * @param employeeDTO Employee data
     * @return Created employee
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee createdEmployee = employeeService.createEmployee(employee);
        EmployeeDTO createdEmployeeDTO = employeeMapper.toDto(createdEmployee);

        return ResponseEntity.ok(createdEmployeeDTO);
    }

    /**
     * Update employee
     * @param id Employee ID
     * @param employeeDTO Employee data
     * @return Updated employee
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isManager(#id)")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        EmployeeDTO updatedEmployeeDTO = employeeMapper.toDto(updatedEmployee);

        return ResponseEntity.ok(updatedEmployeeDTO);
    }

    /**
     * Delete employee (admin only)
     * @param id Employee ID
     * @return Delete status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return ResponseEntity.ok(response);
    }

    /**
     * Get employee shifts
     * @param id Employee ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of shifts
     */
    @GetMapping("/{id}/shifts")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isSelfOrManager(#id)")
    public ResponseEntity<List<ShiftDTO>> getEmployeeShifts(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Shift> shifts = shiftService.getEmployeeShifts(id, startDate, endDate);
        List<ShiftDTO> shiftDTOs = shifts.stream()
                .map(shiftMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(shiftDTOs);
    }

    /**
     * Get employee vacation requests
     * @param id Employee ID
     * @return List of vacation requests
     */
    @GetMapping("/{id}/vacation-requests")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isSelfOrManager(#id)")
    public ResponseEntity<List<VacationRequestDTO>> getEmployeeVacationRequests(@PathVariable Long id) {
        List<VacationRequest> vacationRequests = employeeService.getEmployeeVacationRequests(id);
        List<VacationRequestDTO> vacationRequestDTOs = vacationRequests.stream()
                .map(vacationRequestMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(vacationRequestDTOs);
    }

    /**
     * Create vacation request
     * @param id Employee ID
     * @param vacationRequestDTO Vacation request data
     * @return Created vacation request
     */
    @PostMapping("/{id}/vacation-requests")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isSelf(#id)")
    public ResponseEntity<VacationRequestDTO> createVacationRequest(
            @PathVariable Long id,
            @Valid @RequestBody VacationRequestDTO vacationRequestDTO) {

        VacationRequest vacationRequest = vacationRequestMapper.toEntity(vacationRequestDTO);
        VacationRequest createdVacationRequest = employeeService.createVacationRequest(id, vacationRequest);
        VacationRequestDTO createdVacationRequestDTO = vacationRequestMapper.toDto(createdVacationRequest);

        return ResponseEntity.ok(createdVacationRequestDTO);
    }

    /**
     * Get employee calendar
     * @param id Employee ID
     * @param year Year
     * @param month Month
     * @return Employee calendar
     */
    @GetMapping("/{id}/calendar")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isSelfOrManager(#id)")
    public ResponseEntity<CalendarDTO> getEmployeeCalendar(
            @PathVariable Long id,
            @RequestParam Integer year,
            @RequestParam Integer month) {

        Calendar calendar = calendarService.getEmployeeCalendar(id, year, month);
        CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

        return ResponseEntity.ok(calendarDTO);
    }

    /**
     * Get employee calendar entries
     * @param id Employee ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of calendar entries
     */
    @GetMapping("/{id}/calendar/entries")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isSelfOrManager(#id)")
    public ResponseEntity<List<CalendarEntryDTO>> getEmployeeCalendarEntries(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<CalendarEntry> calendarEntries = calendarService.getEmployeeCalendarEntries(id, startDate, endDate);
        List<CalendarEntryDTO> calendarEntryDTOs = calendarEntries.stream()
                .map(calendarEntryMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(calendarEntryDTOs);
    }

    /**
     * Get employee notifications
     * @param id Employee ID
     * @return List of notifications
     */
    @GetMapping("/{id}/notifications")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isSelf(#id)")
    public ResponseEntity<List<NotificationDTO>> getEmployeeNotifications(@PathVariable Long id) {
        List<Notification> notifications = employeeService.getEmployeeNotifications(id);
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
     * @param id Employee ID
     * @param notificationId Notification ID
     * @return Success status
     */
    @PutMapping("/{id}/notifications/{notificationId}/read")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isSelf(#id)")
    public ResponseEntity<Map<String, Boolean>> markNotificationAsRead(
            @PathVariable Long id,
            @PathVariable Long notificationId) {

        employeeService.markNotificationAsRead(id, notificationId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("success", Boolean.TRUE);

        return ResponseEntity.ok(response);
    }

    /**
     * Get current authenticated employee
     * @return Current employee
     */
    @GetMapping("/me")
    public ResponseEntity<EmployeeDTO> getCurrentEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Employee employee = employeeService.getEmployeeByUsername(username);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        return ResponseEntity.ok(employeeDTO);
    }
}