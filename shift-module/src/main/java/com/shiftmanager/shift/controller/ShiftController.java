package com.shiftmanager.shift.controller;

import com.shiftmanager.core.domain.ShiftType;
import com.shiftmanager.shift.dto.ShiftDTO;
import com.shiftmanager.shift.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for shift operations
 */
@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    /**
     * Create a new shift
     * @param shiftDTO The shift data
     * @return The created shift
     */
    @PostMapping
    public ResponseEntity<ShiftDTO> createShift(@Valid @RequestBody ShiftDTO shiftDTO) {
        return new ResponseEntity<>(shiftService.createShift(shiftDTO), HttpStatus.CREATED);
    }

    /**
     * Create multiple shifts (batch operation)
     * @param shiftDTOs List of shift data
     * @return List of created shifts
     */
    @PostMapping("/batch")
    public ResponseEntity<List<ShiftDTO>> createShifts(@Valid @RequestBody List<ShiftDTO> shiftDTOs) {
        return new ResponseEntity<>(shiftService.createShifts(shiftDTOs), HttpStatus.CREATED);
    }

    /**
     * Get a shift by ID
     * @param id The shift ID
     * @return The shift data
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShiftDTO> getShiftById(@PathVariable Long id) {
        return ResponseEntity.ok(shiftService.getShiftById(id));
    }

    /**
     * Get all shifts
     * @return List of all shifts
     */
    @GetMapping
    public ResponseEntity<List<ShiftDTO>> getAllShifts() {
        return ResponseEntity.ok(shiftService.getAllShifts());
    }

    /**
     * Update a shift
     * @param id The shift ID
     * @param shiftDTO The updated shift data
     * @return The updated shift
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShiftDTO> updateShift(
            @PathVariable Long id, 
            @Valid @RequestBody ShiftDTO shiftDTO) {
        return ResponseEntity.ok(shiftService.updateShift(id, shiftDTO));
    }

    /**
     * Delete a shift
     * @param id The shift ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get shifts by employee
     * @param employeeId The employee ID
     * @return List of shifts for the specified employee
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ShiftDTO>> getShiftsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(shiftService.getShiftsByEmployee(employeeId));
    }

    /**
     * Get shifts by schedule
     * @param scheduleId The schedule ID
     * @return List of shifts for the specified schedule
     */
    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<ShiftDTO>> getShiftsBySchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(shiftService.getShiftsBySchedule(scheduleId));
    }

    /**
     * Get shifts by date range
     * @param startTime The start of the date range
     * @param endTime The end of the date range
     * @return List of shifts within the specified date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<ShiftDTO>> getShiftsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(shiftService.getShiftsByDateRange(startTime, endTime));
    }

    /**
     * Get shifts by employee and date range
     * @param employeeId The employee ID
     * @param startTime The start of the date range
     * @param endTime The end of the date range
     * @return List of shifts for the specified employee within the date range
     */
    @GetMapping("/employee/{employeeId}/date-range")
    public ResponseEntity<List<ShiftDTO>> getShiftsByEmployeeAndDateRange(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(shiftService.getShiftsByEmployeeAndDateRange(employeeId, startTime, endTime));
    }

    /**
     * Get shifts by shift type
     * @param shiftType The shift type
     * @return List of shifts of the specified type
     */
    @GetMapping("/type/{shiftType}")
    public ResponseEntity<List<ShiftDTO>> getShiftsByType(@PathVariable ShiftType shiftType) {
        return ResponseEntity.ok(shiftService.getShiftsByType(shiftType));
    }

    /**
     * Check for shift overlaps
     * @param shiftDTO The shift data to check
     * @return True if there are overlaps, false otherwise
     */
    @PostMapping("/check-overlaps")
    public ResponseEntity<Boolean> checkOverlappingShifts(@RequestBody ShiftDTO shiftDTO) {
        return ResponseEntity.ok(shiftService.hasOverlappingShifts(shiftDTO));
    }

    /**
     * Calculate total hours for an employee in a date range
     * @param employeeId The employee ID
     * @param startTime The start of the date range
     * @param endTime The end of the date range
     * @return The total hours as a double
     */
    @GetMapping("/employee/{employeeId}/total-hours")
    public ResponseEntity<Double> calculateTotalHoursForEmployee(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(shiftService.calculateTotalHoursForEmployee(employeeId, startTime, endTime));
    }
}
