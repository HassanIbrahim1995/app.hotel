package com.shiftmanager.api.service;

import com.shiftmanager.api.dto.EmployeeShiftDTO;
import com.shiftmanager.api.dto.ShiftDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing shifts
 */
public interface ShiftService {
    
    /**
     * Get all shifts
     * @return List of shift DTOs
     */
    List<ShiftDTO> getAllShifts();
    
    /**
     * Get shift by ID
     * @param id Shift ID
     * @return Optional shift DTO
     */
    Optional<ShiftDTO> getShiftById(Long id);
    
    /**
     * Create a new shift
     * @param shiftDTO Shift data
     * @return Created shift DTO
     */
    ShiftDTO createShift(ShiftDTO shiftDTO);
    
    /**
     * Update an existing shift
     * @param id Shift ID
     * @param shiftDTO Updated shift data
     * @return Updated shift DTO
     */
    ShiftDTO updateShift(Long id, ShiftDTO shiftDTO);
    
    /**
     * Delete a shift
     * @param id Shift ID
     */
    void deleteShift(Long id);
    
    /**
     * Get shifts for a date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of shifts in the date range
     */
    List<ShiftDTO> getShiftsByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get shifts for a specific location
     * @param locationId Location ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of shifts at the location in the date range
     */
    List<ShiftDTO> getShiftsByLocation(Long locationId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Assign an employee to a shift
     * @param shiftId Shift ID
     * @param employeeId Employee ID
     * @return The created employee shift assignment
     */
    EmployeeShiftDTO assignEmployeeToShift(Long shiftId, Long employeeId);
    
    /**
     * Remove an employee from a shift
     * @param shiftId Shift ID
     * @param employeeId Employee ID
     */
    void removeEmployeeFromShift(Long shiftId, Long employeeId);
    
    /**
     * Update an employee's shift status (e.g., ASSIGNED, CONFIRMED, COMPLETED)
     * @param employeeShiftId Employee shift assignment ID
     * @param status New status
     * @return Updated employee shift assignment
     */
    EmployeeShiftDTO updateShiftStatus(Long employeeShiftId, String status);
    
    /**
     * Generate a schedule report for a location and date range
     * @param locationId Location ID
     * @param startDate Start date
     * @param endDate End date
     * @return PDF document as byte array
     */
    byte[] generateScheduleReport(Long locationId, LocalDate startDate, LocalDate endDate);
}