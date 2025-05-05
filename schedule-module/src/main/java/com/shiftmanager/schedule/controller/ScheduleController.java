package com.shiftmanager.schedule.controller;

import com.shiftmanager.schedule.dto.CalendarDTO;
import com.shiftmanager.schedule.dto.ScheduleDTO;
import com.shiftmanager.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for schedule operations
 */
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * Create a new schedule
     * @param scheduleDTO The schedule data
     * @return The created schedule
     */
    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO) {
        return new ResponseEntity<>(scheduleService.createSchedule(scheduleDTO), HttpStatus.CREATED);
    }

    /**
     * Get a schedule by ID
     * @param id The schedule ID
     * @return The schedule data
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    /**
     * Get all schedules
     * @return List of all schedules
     */
    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    /**
     * Update a schedule
     * @param id The schedule ID
     * @param scheduleDTO The updated schedule data
     * @return The updated schedule
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(
            @PathVariable Long id, 
            @Valid @RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, scheduleDTO));
    }

    /**
     * Delete a schedule
     * @param id The schedule ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get schedule by employee
     * @param employeeId The employee ID
     * @return The employee's schedule
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ScheduleDTO> getScheduleByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(scheduleService.getScheduleByEmployee(employeeId));
    }

    /**
     * Get schedule by manager
     * @param managerId The manager ID
     * @return The manager's schedule
     */
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<ScheduleDTO> getScheduleByManager(@PathVariable Long managerId) {
        return ResponseEntity.ok(scheduleService.getScheduleByManager(managerId));
    }

    /**
     * Get schedules by date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of schedules within the specified date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.getSchedulesByDateRange(startDate, endDate));
    }

    /**
     * Get a calendar for an employee with all scheduled shifts and vacations
     * @param employeeId The employee ID
     * @param startDate The start date
     * @param endDate The end date
     * @return Calendar data with all entries
     */
    @GetMapping("/employee/{employeeId}/calendar")
    public ResponseEntity<CalendarDTO> getEmployeeCalendar(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.getEmployeeCalendar(employeeId, startDate, endDate));
    }

    /**
     * Get a calendar for a manager with all scheduled shifts and vacations
     * @param managerId The manager ID
     * @param startDate The start date
     * @param endDate The end date
     * @return Calendar data with all entries
     */
    @GetMapping("/manager/{managerId}/calendar")
    public ResponseEntity<CalendarDTO> getManagerCalendar(
            @PathVariable Long managerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.getManagerCalendar(managerId, startDate, endDate));
    }

    /**
     * Create or update an employee's schedule
     * @param employeeId The employee ID
     * @param scheduleDTO The schedule data
     * @return The created or updated schedule
     */
    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<ScheduleDTO> createOrUpdateEmployeeSchedule(
            @PathVariable Long employeeId,
            @Valid @RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.createOrUpdateEmployeeSchedule(employeeId, scheduleDTO));
    }

    /**
     * Create or update a manager's schedule
     * @param managerId The manager ID
     * @param scheduleDTO The schedule data
     * @return The created or updated schedule
     */
    @PostMapping("/manager/{managerId}")
    public ResponseEntity<ScheduleDTO> createOrUpdateManagerSchedule(
            @PathVariable Long managerId,
            @Valid @RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.createOrUpdateManagerSchedule(managerId, scheduleDTO));
    }
}
