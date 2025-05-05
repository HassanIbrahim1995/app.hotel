package com.shiftmanager.vacation.service;

import com.shiftmanager.core.domain.VacationStatus;
import com.shiftmanager.vacation.dto.VacationRequestDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Vacation operations
 */
public interface VacationService {

    /**
     * Request a vacation
     * @param vacationRequestDTO The vacation request data
     * @return The created vacation request
     */
    VacationRequestDTO requestVacation(VacationRequestDTO vacationRequestDTO);

    /**
     * Get a vacation request by ID
     * @param id The vacation request ID
     * @return The vacation request data
     */
    VacationRequestDTO getVacationRequestById(Long id);

    /**
     * Get all vacation requests
     * @return List of all vacation requests
     */
    List<VacationRequestDTO> getAllVacationRequests();

    /**
     * Update a vacation request
     * @param id The vacation request ID
     * @param vacationRequestDTO The updated vacation request data
     * @return The updated vacation request
     */
    VacationRequestDTO updateVacationRequest(Long id, VacationRequestDTO vacationRequestDTO);

    /**
     * Delete a vacation request
     * @param id The vacation request ID
     */
    void deleteVacationRequest(Long id);

    /**
     * Get vacation requests by employee
     * @param employeeId The employee ID
     * @return List of vacation requests for the specified employee
     */
    List<VacationRequestDTO> getVacationRequestsByEmployee(Long employeeId);

    /**
     * Get vacation requests by manager
     * @param managerId The manager ID
     * @return List of vacation requests handled by the specified manager
     */
    List<VacationRequestDTO> getVacationRequestsByManager(Long managerId);

    /**
     * Get vacation requests by status
     * @param status The vacation request status
     * @return List of vacation requests with the specified status
     */
    List<VacationRequestDTO> getVacationRequestsByStatus(VacationStatus status);

    /**
     * Get vacation requests by employee and status
     * @param employeeId The employee ID
     * @param status The vacation request status
     * @return List of vacation requests for the specified employee with the specified status
     */
    List<VacationRequestDTO> getVacationRequestsByEmployeeAndStatus(Long employeeId, VacationStatus status);

    /**
     * Get vacation requests by date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of vacation requests within the specified date range
     */
    List<VacationRequestDTO> getVacationRequestsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Approve a vacation request
     * @param id The vacation request ID
     * @param managerId The manager ID
     * @param comments Optional comments from the manager
     * @return The updated vacation request
     */
    VacationRequestDTO approveVacationRequest(Long id, Long managerId, String comments);

    /**
     * Reject a vacation request
     * @param id The vacation request ID
     * @param managerId The manager ID
     * @param comments Required comments explaining the rejection reason
     * @return The updated vacation request
     */
    VacationRequestDTO rejectVacationRequest(Long id, Long managerId, String comments);

    /**
     * Cancel a vacation request
     * @param id The vacation request ID
     * @return The updated vacation request
     */
    VacationRequestDTO cancelVacationRequest(Long id);

    /**
     * Get pending vacation requests for a manager's department
     * @param managerId The manager ID
     * @return List of pending vacation requests from employees in the manager's department
     */
    List<VacationRequestDTO> getPendingRequestsByManagerDepartment(Long managerId);
}
