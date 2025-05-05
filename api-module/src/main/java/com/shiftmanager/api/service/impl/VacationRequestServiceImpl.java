package com.shiftmanager.api.service.impl;

import com.shiftmanager.api.dto.VacationRequestDTO;
import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.mapper.VacationRequestMapper;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.VacationRequest;
import com.shiftmanager.api.repository.EmployeeRepository;
import com.shiftmanager.api.repository.VacationRequestRepository;
import com.shiftmanager.api.service.NotificationService;
import com.shiftmanager.api.service.VacationRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the VacationRequestService interface
 */
@Service
@Transactional
public class VacationRequestServiceImpl implements VacationRequestService {
    
    private static final Logger logger = LoggerFactory.getLogger(VacationRequestServiceImpl.class);
    
    @Autowired
    private VacationRequestRepository vacationRequestRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private VacationRequestMapper vacationRequestMapper;
    
    @Autowired
    private NotificationService notificationService;
    
    @Override
    public List<VacationRequestDTO> getAllVacationRequests() {
        logger.debug("Getting all vacation requests");
        return vacationRequestRepository.findAll().stream()
                .map(vacationRequestMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<VacationRequestDTO> getVacationRequestById(Long id) {
        logger.debug("Getting vacation request with ID: {}", id);
        return vacationRequestRepository.findById(id)
                .map(vacationRequestMapper::toDto);
    }
    
    @Override
    public VacationRequestDTO createVacationRequest(VacationRequestDTO vacationRequestDTO) {
        logger.debug("Creating new vacation request: {}", vacationRequestDTO);
        
        // Get the employee
        Employee employee = employeeRepository.findById(vacationRequestDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + vacationRequestDTO.getEmployeeId()));
        
        // Validate dates
        if (vacationRequestDTO.getStartDate().isAfter(vacationRequestDTO.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        
        // Check for overlapping vacation requests
        checkForOverlappingVacations(employee, vacationRequestDTO.getStartDate(), vacationRequestDTO.getEndDate(), null);
        
        // Create new vacation request
        VacationRequest vacationRequest = new VacationRequest(
                employee,
                vacationRequestDTO.getStartDate(),
                vacationRequestDTO.getEndDate(),
                vacationRequestDTO.getRequestNotes());
        
        // Save and convert to DTO
        VacationRequest savedRequest = vacationRequestRepository.save(vacationRequest);
        VacationRequestDTO dto = vacationRequestMapper.toDto(savedRequest);
        
        // Notify the employee's manager
        if (employee.getManager() != null) {
            notificationService.createNotification(
                    employee.getManager().getId(),
                    "New vacation request from " + employee.getFirstName() + " " + employee.getLastName(),
                    "VACATION_REQUEST",
                    savedRequest.getId());
        }
        
        return dto;
    }
    
    @Override
    public VacationRequestDTO updateVacationRequest(Long id, VacationRequestDTO vacationRequestDTO) {
        logger.debug("Updating vacation request with ID: {}", id);
        
        return vacationRequestRepository.findById(id)
                .map(vacationRequest -> {
                    // Only update if the request is still pending
                    if (!vacationRequest.isPending()) {
                        throw new IllegalStateException("Cannot update vacation request that has already been processed.");
                    }
                    
                    vacationRequest.setStartDate(vacationRequestDTO.getStartDate());
                    vacationRequest.setEndDate(vacationRequestDTO.getEndDate());
                    vacationRequest.setRequestNotes(vacationRequestDTO.getRequestNotes());
                    
                    VacationRequest updatedRequest = vacationRequestRepository.save(vacationRequest);
                    return vacationRequestMapper.toDto(updatedRequest);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Vacation request not found with ID: " + id));
    }
    
    @Override
    public void deleteVacationRequest(Long id) {
        logger.debug("Deleting vacation request with ID: {}", id);
        
        VacationRequest vacationRequest = vacationRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation request not found with ID: " + id));
        
        // Only allow deletion of pending requests
        if (!vacationRequest.isPending()) {
            throw new IllegalStateException("Cannot delete vacation request that has already been processed.");
        }
        
        vacationRequestRepository.delete(vacationRequest);
    }
    
    @Override
    public List<VacationRequestDTO> getPendingVacationRequestsForManager(Long managerId) {
        logger.debug("Getting pending vacation requests for manager ID: {}", managerId);
        
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + managerId));
        
        return vacationRequestRepository.findByEmployeeManagerAndStatus(manager, "PENDING").stream()
                .map(vacationRequestMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public VacationRequestDTO approveVacationRequest(Long requestId, Long reviewerId, String notes) {
        logger.debug("Approving vacation request ID: {} by reviewer ID: {}", requestId, reviewerId);
        
        VacationRequest vacationRequest = vacationRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation request not found with ID: " + requestId));
        
        Employee reviewer = employeeRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found with ID: " + reviewerId));
        
        // Only allow approval of pending requests
        if (!vacationRequest.isPending()) {
            throw new IllegalStateException("Cannot approve vacation request that has already been processed.");
        }
        
        // Approve the request
        vacationRequest.approve(reviewer, notes);
        VacationRequest updatedRequest = vacationRequestRepository.save(vacationRequest);
        
        // Notify the employee
        notificationService.sendVacationRequestStatusNotification(
                vacationRequest.getEmployee().getId(),
                vacationRequest.getId(),
                true);
        
        return vacationRequestMapper.toDto(updatedRequest);
    }
    
    @Override
    public VacationRequestDTO rejectVacationRequest(Long requestId, Long reviewerId, String notes) {
        logger.debug("Rejecting vacation request ID: {} by reviewer ID: {}", requestId, reviewerId);
        
        VacationRequest vacationRequest = vacationRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation request not found with ID: " + requestId));
        
        Employee reviewer = employeeRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found with ID: " + reviewerId));
        
        // Only allow rejection of pending requests
        if (!vacationRequest.isPending()) {
            throw new IllegalStateException("Cannot reject vacation request that has already been processed.");
        }
        
        // Reject the request
        vacationRequest.reject(reviewer, notes);
        VacationRequest updatedRequest = vacationRequestRepository.save(vacationRequest);
        
        // Notify the employee
        notificationService.sendVacationRequestStatusNotification(
                vacationRequest.getEmployee().getId(),
                vacationRequest.getId(),
                false);
        
        return vacationRequestMapper.toDto(updatedRequest);
    }
    
    @Override
    public List<VacationRequestDTO> getVacationRequestsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting vacation requests from {} to {}", startDate, endDate);
        
        return vacationRequestRepository.findByDateRange(startDate, endDate).stream()
                .map(vacationRequestMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public byte[] generateVacationCalendar(Long locationId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Generating vacation calendar for location ID: {} from {} to {}", locationId, startDate, endDate);
        
        // This would typically integrate with a calendar generation library
        // For now, we'll return a placeholder
        return new byte[0]; // Placeholder implementation
    }
    
    /**
     * Check if there are any overlapping approved vacation requests for an employee
     * @param employee The employee
     * @param startDate Start date of the vacation
     * @param endDate End date of the vacation
     * @param currentRequestId ID of the current request (null for new requests)
     * @throws IllegalStateException if overlapping vacation is found
     */
    private void checkForOverlappingVacations(Employee employee, LocalDate startDate, LocalDate endDate, Long currentRequestId) {
        // Find approved vacation requests that overlap with the given date range
        List<VacationRequest> overlappingRequests = vacationRequestRepository.findOverlappingApprovedRequests(
                employee, startDate, endDate);
        
        // Filter out the current request if we're updating
        if (currentRequestId != null) {
            overlappingRequests = overlappingRequests.stream()
                    .filter(request -> !request.getId().equals(currentRequestId))
                    .collect(Collectors.toList());
        }
        
        if (!overlappingRequests.isEmpty()) {
            VacationRequest firstOverlap = overlappingRequests.get(0);
            throw new IllegalStateException("Vacation request overlaps with an existing approved vacation from " +
                    firstOverlap.getStartDate() + " to " + firstOverlap.getEndDate());
        }
    }
}