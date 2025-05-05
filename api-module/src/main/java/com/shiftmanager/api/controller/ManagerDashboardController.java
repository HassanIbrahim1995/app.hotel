package com.shiftmanager.api.controller;

import com.shiftmanager.api.dto.*;
import com.shiftmanager.api.mapper.CalendarMapper;
import com.shiftmanager.api.mapper.EmployeeMapper;
import com.shiftmanager.api.mapper.ShiftMapper;
import com.shiftmanager.api.mapper.VacationRequestMapper;
import com.shiftmanager.api.model.Calendar;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.Shift;
import com.shiftmanager.api.model.VacationRequest;
import com.shiftmanager.api.service.CalendarService;
import com.shiftmanager.api.service.ManagerService;
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
 * REST Controller for manager dashboard operations
 */
@RestController
@RequestMapping("/api/manager/dashboard")
@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
public class ManagerDashboardController {

    @Autowired
    private ManagerService managerService;

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

    /**
     * Get manager dashboard overview data
     * @param startDate Start date for shift schedule
     * @param endDate End date for shift schedule
     * @return Dashboard overview data
     */
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getDashboardOverview(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long managerId = getCurrentManagerId();
        
        // Get team members
        List<Employee> teamMembers = managerService.getTeamMembers(managerId);
        List<EmployeeDTO> teamMemberDTOs = teamMembers.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
        
        // Get team schedules
        List<Shift> teamSchedules = managerService.getTeamSchedule(managerId, startDate, endDate);
        List<ShiftDTO> teamScheduleDTOs = teamSchedules.stream()
                .map(shiftMapper::toDto)
                .collect(Collectors.toList());
        
        // Get pending vacation requests
        List<VacationRequest> pendingRequests = managerService.getPendingVacationRequestsForTeam(managerId);
        List<VacationRequestDTO> pendingRequestDTOs = pendingRequests.stream()
                .map(vacationRequestMapper::toDto)
                .collect(Collectors.toList());
        
        // Get team calendars
        List<Calendar> teamCalendars = calendarService.getTeamCalendars(managerId);
        List<CalendarDTO> teamCalendarDTOs = teamCalendars.stream()
                .map(calendarMapper::toDto)
                .collect(Collectors.toList());
        
        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("teamMembers", teamMemberDTOs);
        response.put("teamSchedule", teamScheduleDTOs);
        response.put("pendingVacationRequests", pendingRequestDTOs);
        response.put("teamCalendars", teamCalendarDTOs);
        response.put("dateRange", Map.of("startDate", startDate, "endDate", endDate));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get team members
     * @return List of team members
     */
    @GetMapping("/team")
    public ResponseEntity<List<EmployeeDTO>> getTeamMembers() {
        Long managerId = getCurrentManagerId();
        
        List<Employee> teamMembers = managerService.getTeamMembers(managerId);
        List<EmployeeDTO> teamMemberDTOs = teamMembers.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(teamMemberDTOs);
    }

    /**
     * Get team schedule
     * @param startDate Start date
     * @param endDate End date
     * @return Team schedule
     */
    @GetMapping("/schedule")
    public ResponseEntity<List<ShiftDTO>> getTeamSchedule(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        Long managerId = getCurrentManagerId();
        
        List<Shift> teamSchedules = managerService.getTeamSchedule(managerId, startDate, endDate);
        List<ShiftDTO> teamScheduleDTOs = teamSchedules.stream()
                .map(shiftMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(teamScheduleDTOs);
    }

    /**
     * Get pending vacation requests
     * @return List of pending vacation requests
     */
    @GetMapping("/vacation-requests")
    public ResponseEntity<List<VacationRequestDTO>> getPendingVacationRequests() {
        Long managerId = getCurrentManagerId();
        
        List<VacationRequest> pendingRequests = managerService.getPendingVacationRequestsForTeam(managerId);
        List<VacationRequestDTO> pendingRequestDTOs = pendingRequests.stream()
                .map(vacationRequestMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(pendingRequestDTOs);
    }

    /**
     * Approve a vacation request
     * @param requestId Vacation request ID
     * @param reviewNotes Review notes
     * @return Approved vacation request
     */
    @PutMapping("/vacation-requests/{requestId}/approve")
    public ResponseEntity<VacationRequestDTO> approveVacationRequest(
            @PathVariable Long requestId,
            @RequestParam(required = false) String reviewNotes) {
        
        Long managerId = getCurrentManagerId();
        
        VacationRequest approvedRequest = managerService.approveVacationRequest(requestId, managerId, reviewNotes);
        VacationRequestDTO approvedRequestDTO = vacationRequestMapper.toDto(approvedRequest);
        
        return ResponseEntity.ok(approvedRequestDTO);
    }

    /**
     * Reject a vacation request
     * @param requestId Vacation request ID
     * @param reviewNotes Review notes
     * @return Rejected vacation request
     */
    @PutMapping("/vacation-requests/{requestId}/reject")
    public ResponseEntity<VacationRequestDTO> rejectVacationRequest(
            @PathVariable Long requestId,
            @RequestParam(required = false) String reviewNotes) {
        
        Long managerId = getCurrentManagerId();
        
        VacationRequest rejectedRequest = managerService.rejectVacationRequest(requestId, managerId, reviewNotes);
        VacationRequestDTO rejectedRequestDTO = vacationRequestMapper.toDto(rejectedRequest);
        
        return ResponseEntity.ok(rejectedRequestDTO);
    }

    /**
     * Assign a shift to an employee
     * @param shiftId Shift ID
     * @param employeeId Employee ID
     * @return Result of assignment
     */
    @PostMapping("/shifts/{shiftId}/assign/{employeeId}")
    public ResponseEntity<Map<String, Object>> assignShiftToEmployee(
            @PathVariable Long shiftId,
            @PathVariable Long employeeId) {
        
        Long managerId = getCurrentManagerId();
        
        boolean result = managerService.assignShiftToEmployee(shiftId, employeeId, managerId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        response.put("message", "Shift assigned successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Unassign a shift from an employee
     * @param shiftId Shift ID
     * @param employeeId Employee ID
     * @return Result of unassignment
     */
    @PostMapping("/shifts/{shiftId}/unassign/{employeeId}")
    public ResponseEntity<Map<String, Object>> unassignShiftFromEmployee(
            @PathVariable Long shiftId,
            @PathVariable Long employeeId) {
        
        Long managerId = getCurrentManagerId();
        
        boolean result = managerService.unassignShiftFromEmployee(shiftId, employeeId, managerId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        response.put("message", "Shift unassigned successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new shift
     * @param shiftDTO Shift DTO
     * @return Created shift
     */
    @PostMapping("/shifts")
    public ResponseEntity<ShiftDTO> createShift(@RequestBody ShiftDTO shiftDTO) {
        Long managerId = getCurrentManagerId();
        
        Shift shift = shiftMapper.toEntity(shiftDTO);
        Shift createdShift = managerService.createShift(shift, managerId);
        ShiftDTO createdShiftDTO = shiftMapper.toDto(createdShift);
        
        return ResponseEntity.ok(createdShiftDTO);
    }

    /**
     * Update a shift
     * @param shiftId Shift ID
     * @param shiftDTO Shift DTO
     * @return Updated shift
     */
    @PutMapping("/shifts/{shiftId}")
    public ResponseEntity<ShiftDTO> updateShift(
            @PathVariable Long shiftId,
            @RequestBody ShiftDTO shiftDTO) {
        
        Long managerId = getCurrentManagerId();
        
        Shift shift = shiftMapper.toEntity(shiftDTO);
        Shift updatedShift = managerService.updateShift(shiftId, shift, managerId);
        ShiftDTO updatedShiftDTO = shiftMapper.toDto(updatedShift);
        
        return ResponseEntity.ok(updatedShiftDTO);
    }

    /**
     * Delete a shift
     * @param shiftId Shift ID
     * @return Result of deletion
     */
    @DeleteMapping("/shifts/{shiftId}")
    public ResponseEntity<Map<String, Object>> deleteShift(@PathVariable Long shiftId) {
        Long managerId = getCurrentManagerId();
        
        boolean result = managerService.deleteShift(shiftId, managerId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", result);
        response.put("message", "Shift deleted successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get team calendars
     * @return Team calendars
     */
    @GetMapping("/calendars")
    public ResponseEntity<List<CalendarDTO>> getTeamCalendars() {
        Long managerId = getCurrentManagerId();
        
        List<Calendar> teamCalendars = calendarService.getTeamCalendars(managerId);
        List<CalendarDTO> teamCalendarDTOs = teamCalendars.stream()
                .map(calendarMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(teamCalendarDTOs);
    }

    /**
     * Get the current manager ID from the authenticated user
     * @return Manager ID
     * @throws IllegalStateException if no manager found for the authenticated user
     */
    private Long getCurrentManagerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // This would typically come from a user service that would look up the employee ID
        // For now, we'll assume the username is the employee ID for simplicity
        // In a real application, you would have a UserService.getEmployeeIdByUsername method
        
        // This is a placeholder - in a real app, replace with actual lookup
        // Long employeeId = userService.getEmployeeIdByUsername(username);
        
        // For demonstration, we'll hardcode it to 1 (the manager from our sample data)
        return 1L;
    }
}