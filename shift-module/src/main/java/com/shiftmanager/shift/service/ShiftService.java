package com.shiftmanager.shift.service;

import com.shiftmanager.core.domain.ShiftType;
import com.shiftmanager.shift.dto.ShiftDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Shift operations
 */
public interface ShiftService {

    /**
     * Create a new shift
     * @param shiftDTO The shift data
     * @return The created shift
     */
    ShiftDTO createShift(ShiftDTO shiftDTO);

    /**
     * Create multiple shifts (batch operation)
     * @param shiftDTOs List of shift data
     * @return List of created shifts
     */
    List<ShiftDTO> createShifts(List<ShiftDTO> shiftDTOs);

    /**
     * Get a shift by ID
     * @param id The shift ID
     * @return The shift data
     */
    ShiftDTO getShiftById(Long id);

    /**
     * Get all shifts
     * @return List of all shifts
     */
    List<ShiftDTO> getAllShifts();

    /**
     * Update a shift
     * @param id The shift ID
     * @param shiftDTO The updated shift data
     * @return The updated shift
     */
    ShiftDTO updateShift(Long id, ShiftDTO shiftDTO);

    /**
     * Delete a shift
     * @param id The shift ID
     */
    void deleteShift(Long id);

    /**
     * Get shifts by employee
     * @param employeeId The employee ID
     * @return List of shifts for the specified employee
     */
    List<ShiftDTO> getShiftsByEmployee(Long employeeId);

    /**
     * Get shifts by schedule
     * @param scheduleId The schedule ID
     * @return List of shifts for the specified schedule
     */
    List<ShiftDTO> getShiftsBySchedule(Long scheduleId);

    /**
     * Get shifts by date range
     * @param startTime The start of the date range
     * @param endTime The end of the date range
     * @return List of shifts within the specified date range
     */
    List<ShiftDTO> getShiftsByDateRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Get shifts by employee and date range
     * @param employeeId The employee ID
     * @param startTime The start of the date range
     * @param endTime The end of the date range
     * @return List of shifts for the specified employee within the date range
     */
    List<ShiftDTO> getShiftsByEmployeeAndDateRange(Long employeeId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Get shifts by shift type
     * @param shiftType The shift type
     * @return List of shifts of the specified type
     */
    List<ShiftDTO> getShiftsByType(ShiftType shiftType);

    /**
     * Check for shift overlaps
     * @param shiftDTO The shift data to check
     * @return true if there are overlaps, false otherwise
     */
    boolean hasOverlappingShifts(ShiftDTO shiftDTO);

    /**
     * Calculate total hours for an employee in a date range
     * @param employeeId The employee ID
     * @param startTime The start of the date range
     * @param endTime The end of the date range
     * @return The total hours as a double
     */
    double calculateTotalHoursForEmployee(Long employeeId, LocalDateTime startTime, LocalDateTime endTime);
}
