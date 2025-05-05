package com.shiftmanager.api.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.shiftmanager.api.dto.EmployeeShiftDTO;
import com.shiftmanager.api.dto.ShiftDTO;
import com.shiftmanager.api.model.Shift;

public interface ShiftService {
    
    List<ShiftDTO> getAllShifts(LocalDate startDate, LocalDate endDate);
    
    ShiftDTO getShiftById(Long id);
    
    ShiftDTO createShift(ShiftDTO shiftDTO);
    
    ShiftDTO updateShift(Long id, ShiftDTO shiftDTO);
    
    void deleteShift(Long id);
    
    List<ShiftDTO> getShiftsByLocation(Long locationId, LocalDate startDate, LocalDate endDate);
    
    List<ShiftDTO> getShiftsByShiftType(Long shiftTypeId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get shifts for an employee
     * @param employeeId Employee ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of shifts
     */
    List<Shift> getEmployeeShifts(Long employeeId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get all assigned shifts for an employee
     * @param employeeId Employee ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of employee shift assignments
     */
    List<EmployeeShiftDTO> getEmployeeShiftAssignments(Long employeeId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get shifts for the current authenticated employee
     * @param startDate Start date
     * @param endDate End date
     * @return List of employee shift assignments
     */
    List<EmployeeShiftDTO> getMyShifts(LocalDate startDate, LocalDate endDate);
    
    /**
     * Assign an employee to a shift
     * @param shiftId Shift ID
     * @param employeeId Employee ID
     * @return true if successful
     */
    boolean assignShift(Long shiftId, Long employeeId);
    
    /**
     * Employee confirms a shift assignment
     * @param employeeShiftId Employee shift assignment ID
     * @return Updated employee shift assignment
     */
    EmployeeShiftDTO confirmShift(Long employeeShiftId);
    
    /**
     * Employee declines a shift assignment
     * @param employeeShiftId Employee shift assignment ID
     * @param reason Optional reason for declining
     * @return Updated employee shift assignment
     */
    EmployeeShiftDTO declineShift(Long employeeShiftId, String reason);
    
    /**
     * Clock in for a shift
     * @param employeeShiftId Employee shift assignment ID
     * @return Updated employee shift assignment
     */
    EmployeeShiftDTO clockIn(Long employeeShiftId);
    
    /**
     * Clock out from a shift
     * @param employeeShiftId Employee shift assignment ID
     * @return Updated employee shift assignment
     */
    EmployeeShiftDTO clockOut(Long employeeShiftId);
    
    /**
     * Update clock in time for a shift (manager function)
     * @param employeeShiftId Employee shift assignment ID
     * @param clockInTime New clock in time
     * @return Updated employee shift assignment
     */
    EmployeeShiftDTO updateClockInTime(Long employeeShiftId, LocalTime clockInTime);
    
    /**
     * Update clock out time for a shift (manager function)
     * @param employeeShiftId Employee shift assignment ID
     * @param clockOutTime New clock out time
     * @return Updated employee shift assignment
     */
    EmployeeShiftDTO updateClockOutTime(Long employeeShiftId, LocalTime clockOutTime);
}
