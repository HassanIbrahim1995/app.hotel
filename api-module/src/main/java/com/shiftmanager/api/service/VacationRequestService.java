package com.shiftmanager.api.service;

import com.shiftmanager.api.dto.VacationRequestDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing vacation requests
 */
public interface VacationRequestService {
    
    /**
     * Get all vacation requests
     * @return List of vacation request DTOs
     */
    List<VacationRequestDTO> getAllVacationRequests();
    
    /**
     * Get vacation request by ID
     * @param id Vacation request ID
     * @return Optional vacation request DTO
     */
    Optional<VacationRequestDTO> getVacationRequestById(Long id);
    
    /**
     * Create a new vacation request
     * @param vacationRequestDTO Vacation request data
     * @return Created vacation request DTO
     */
    VacationRequestDTO createVacationRequest(VacationRequestDTO vacationRequestDTO);
    
    /**
     * Update an existing vacation request
     * @param id Vacation request ID
     * @param vacationRequestDTO Updated vacation request data
     * @return Updated vacation request DTO
     */
    VacationRequestDTO updateVacationRequest(Long id, VacationRequestDTO vacationRequestDTO);
    
    /**
     * Delete a vacation request
     * @param id Vacation request ID
     */
    void deleteVacationRequest(Long id);
    
    /**
     * Get pending vacation requests for a manager to review
     * @param managerId Manager ID
     * @return List of pending vacation requests
     */
    List<VacationRequestDTO> getPendingVacationRequestsForManager(Long managerId);
    
    /**
     * Approve a vacation request
     * @param requestId Vacation request ID
     * @param reviewerId ID of the manager approving the request
     * @param notes Optional approval notes
     * @return Updated vacation request DTO
     */
    VacationRequestDTO approveVacationRequest(Long requestId, Long reviewerId, String notes);
    
    /**
     * Reject a vacation request
     * @param requestId Vacation request ID
     * @param reviewerId ID of the manager rejecting the request
     * @param notes Optional rejection notes
     * @return Updated vacation request DTO
     */
    VacationRequestDTO rejectVacationRequest(Long requestId, Long reviewerId, String notes);
    
    /**
     * Get vacation requests for a date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of vacation requests in the date range
     */
    List<VacationRequestDTO> getVacationRequestsByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Generate a vacation calendar for a location
     * @param locationId Location ID
     * @param startDate Start date
     * @param endDate End date
     * @return Calendar data with approved vacation days
     */
    byte[] generateVacationCalendar(Long locationId, LocalDate startDate, LocalDate endDate);
}