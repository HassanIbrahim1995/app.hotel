package com.shiftmanager.api.service;

import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.Shift;
import com.shiftmanager.api.model.VacationRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for manager operations
 * Only managers should have access to these functions
 */
public interface ManagerService {
    
    /**
     * Assign a shift to an employee
     * @param shiftId Shift ID
     * @param employeeId Employee ID
     * @param managerId Manager ID (for authorization)
     * @return True if assigned successfully
     */
    boolean assignShiftToEmployee(Long shiftId, Long employeeId, Long managerId);
    
    /**
     * Unassign a shift from an employee
     * @param shiftId Shift ID
     * @param employeeId Employee ID
     * @param managerId Manager ID (for authorization)
     * @return True if unassigned successfully
     */
    boolean unassignShiftFromEmployee(Long shiftId, Long employeeId, Long managerId);
    
    /**
     * Adjust employee shift
     * @param employeeShiftId Employee shift ID
     * @param newShiftId New shift ID
     * @param managerId Manager ID (for authorization)
     * @return Updated employee shift
     */
    Shift adjustEmployeeShift(Long employeeShiftId, Long newShiftId, Long managerId);
    
    /**
     * Approve a vacation request
     * @param vacationRequestId Vacation request ID
     * @param managerId Manager ID (for authorization)
     * @param reviewNotes Review notes
     * @return Approved vacation request
     */
    VacationRequest approveVacationRequest(Long vacationRequestId, Long managerId, String reviewNotes);
    
    /**
     * Reject a vacation request
     * @param vacationRequestId Vacation request ID
     * @param managerId Manager ID (for authorization)
     * @param reviewNotes Review notes
     * @return Rejected vacation request
     */
    VacationRequest rejectVacationRequest(Long vacationRequestId, Long managerId, String reviewNotes);
    
    /**
     * Get pending vacation requests for manager's team
     * @param managerId Manager ID
     * @return List of pending vacation requests
     */
    List<VacationRequest> getPendingVacationRequestsForTeam(Long managerId);
    
    /**
     * Get all employees reporting to manager
     * @param managerId Manager ID
     * @return List of employees
     */
    List<Employee> getTeamMembers(Long managerId);
    
    /**
     * Get employee schedules for a date range
     * @param managerId Manager ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of shifts for the team
     */
    List<Shift> getTeamSchedule(Long managerId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Create a new shift
     * @param shift Shift to create
     * @param managerId Manager ID (for authorization)
     * @return Created shift
     */
    Shift createShift(Shift shift, Long managerId);
    
    /**
     * Update an existing shift
     * @param shiftId Shift ID
     * @param updatedShift Updated shift details
     * @param managerId Manager ID (for authorization)
     * @return Updated shift
     */
    Shift updateShift(Long shiftId, Shift updatedShift, Long managerId);
    
    /**
     * Delete a shift
     * @param shiftId Shift ID
     * @param managerId Manager ID (for authorization)
     * @return True if deleted successfully
     */
    boolean deleteShift(Long shiftId, Long managerId);
    
    /**
     * Check if an employee is a manager
     * @param employeeId Employee ID
     * @return True if employee is a manager
     */
    boolean isManager(Long employeeId);
}