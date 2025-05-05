package com.shiftmanager.vacation.service.impl;

import com.shiftmanager.core.domain.Employee;
import com.shiftmanager.core.domain.Manager;
import com.shiftmanager.core.domain.VacationRequest;
import com.shiftmanager.core.domain.VacationStatus;
import com.shiftmanager.core.repository.EmployeeRepository;
import com.shiftmanager.core.repository.ManagerRepository;
import com.shiftmanager.core.repository.ShiftRepository;
import com.shiftmanager.core.repository.VacationRequestRepository;
import com.shiftmanager.notification.service.NotificationService;
import com.shiftmanager.vacation.dto.VacationRequestDTO;
import com.shiftmanager.vacation.service.VacationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of VacationService
 */
@Service
@RequiredArgsConstructor
public class VacationServiceImpl implements VacationService {

    private final VacationRequestRepository vacationRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final ShiftRepository shiftRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public VacationRequestDTO requestVacation(VacationRequestDTO vacationRequestDTO) {
        validateVacationDates(vacationRequestDTO);
        
        // Check for shift conflicts
        checkShiftConflicts(vacationRequestDTO);
        
        VacationRequest vacationRequest = mapDtoToEntity(vacationRequestDTO);
        
        // Set initial status to PENDING if not provided
        if (vacationRequest.getStatus() == null) {
            vacationRequest.setStatus(VacationStatus.PENDING);
        }
        
        VacationRequest savedVacationRequest = vacationRequestRepository.save(vacationRequest);
        
        // Notify the manager if the employee has one
        notifyManagerAboutVacationRequest(savedVacationRequest);
        
        return mapEntityToDto(savedVacationRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public VacationRequestDTO getVacationRequestById(Long id) {
        VacationRequest vacationRequest = vacationRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vacation request not found with id: " + id));
        return mapEntityToDto(vacationRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationRequestDTO> getAllVacationRequests() {
        return vacationRequestRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VacationRequestDTO updateVacationRequest(Long id, VacationRequestDTO vacationRequestDTO) {
        VacationRequest vacationRequest = vacationRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vacation request not found with id: " + id));
        
        // Only allow updating pending requests
        if (vacationRequest.getStatus() != VacationStatus.PENDING) {
            throw new IllegalStateException("Cannot update vacation request that is not in PENDING status");
        }
        
        validateVacationDates(vacationRequestDTO);
        
        // Check for shift conflicts
        checkShiftConflicts(vacationRequestDTO);
        
        // Update properties
        updateVacationRequestFromDto(vacationRequest, vacationRequestDTO);
        
        VacationRequest updatedVacationRequest = vacationRequestRepository.save(vacationRequest);
        
        // Notify the manager about the update if the employee has one
        notifyManagerAboutVacationUpdate(updatedVacationRequest);
        
        return mapEntityToDto(updatedVacationRequest);
    }

    @Override
    @Transactional
    public void deleteVacationRequest(Long id) {
        VacationRequest vacationRequest = vacationRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vacation request not found with id: " + id));
        
        // Only allow deleting pending requests
        if (vacationRequest.getStatus() != VacationStatus.PENDING) {
            throw new IllegalStateException("Cannot delete vacation request that is not in PENDING status");
        }
        
        vacationRequestRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationRequestDTO> getVacationRequestsByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
        
        return vacationRequestRepository.findByEmployeeId(employeeId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationRequestDTO> getVacationRequestsByManager(Long managerId) {
        if (!managerRepository.existsById(managerId)) {
            throw new EntityNotFoundException("Manager not found with id: " + managerId);
        }
        
        return vacationRequestRepository.findByManagerId(managerId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationRequestDTO> getVacationRequestsByStatus(VacationStatus status) {
        return vacationRequestRepository.findByStatus(status).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationRequestDTO> getVacationRequestsByEmployeeAndStatus(Long employeeId, VacationStatus status) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
        
        return vacationRequestRepository.findByEmployeeIdAndStatus(employeeId, status).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationRequestDTO> getVacationRequestsByDateRange(LocalDate startDate, LocalDate endDate) {
        return vacationRequestRepository.findByDateRange(startDate, endDate).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VacationRequestDTO approveVacationRequest(Long id, Long managerId, String comments) {
        VacationRequest vacationRequest = vacationRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vacation request not found with id: " + id));
        
        // Only allow approving pending requests
        if (vacationRequest.getStatus() != VacationStatus.PENDING) {
            throw new IllegalStateException("Cannot approve vacation request that is not in PENDING status");
        }
        
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + managerId));
        
        // Approve the request
        vacationRequest.approve(manager, comments);
        
        VacationRequest approvedRequest = vacationRequestRepository.save(vacationRequest);
        
        // Notify the employee about the approval
        notifyEmployeeAboutVacationStatus(approvedRequest, "Vacation Request Approved",
                "Your vacation request from " + approvedRequest.getStartDate() + 
                " to " + approvedRequest.getEndDate() + " has been approved.");
        
        return mapEntityToDto(approvedRequest);
    }

    @Override
    @Transactional
    public VacationRequestDTO rejectVacationRequest(Long id, Long managerId, String comments) {
        if (comments == null || comments.trim().isEmpty()) {
            throw new IllegalArgumentException("Comments are required when rejecting a vacation request");
        }
        
        VacationRequest vacationRequest = vacationRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vacation request not found with id: " + id));
        
        // Only allow rejecting pending requests
        if (vacationRequest.getStatus() != VacationStatus.PENDING) {
            throw new IllegalStateException("Cannot reject vacation request that is not in PENDING status");
        }
        
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + managerId));
        
        // Reject the request
        vacationRequest.reject(manager, comments);
        
        VacationRequest rejectedRequest = vacationRequestRepository.save(vacationRequest);
        
        // Notify the employee about the rejection
        notifyEmployeeAboutVacationStatus(rejectedRequest, "Vacation Request Rejected",
                "Your vacation request from " + rejectedRequest.getStartDate() + 
                " to " + rejectedRequest.getEndDate() + " has been rejected. Reason: " + comments);
        
        return mapEntityToDto(rejectedRequest);
    }

    @Override
    @Transactional
    public VacationRequestDTO cancelVacationRequest(Long id) {
        VacationRequest vacationRequest = vacationRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vacation request not found with id: " + id));
        
        // Only allow canceling pending or approved requests
        if (vacationRequest.getStatus() != VacationStatus.PENDING && vacationRequest.getStatus() != VacationStatus.APPROVED) {
            throw new IllegalStateException("Cannot cancel vacation request that is not in PENDING or APPROVED status");
        }
        
        // Cancel the request
        vacationRequest.setStatus(VacationStatus.CANCELLED);
        vacationRequest.setUpdatedAt(LocalDateTime.now());
        
        VacationRequest cancelledRequest = vacationRequestRepository.save(vacationRequest);
        
        // Notify the manager if the request had one
        if (cancelledRequest.getManager() != null) {
            notifyManagerAboutVacationCancellation(cancelledRequest);
        }
        
        return mapEntityToDto(cancelledRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacationRequestDTO> getPendingRequestsByManagerDepartment(Long managerId) {
        if (!managerRepository.existsById(managerId)) {
            throw new EntityNotFoundException("Manager not found with id: " + managerId);
        }
        
        return vacationRequestRepository.findPendingRequestsByManagerDepartment(managerId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Validates that vacation request dates are valid (end date not before start date)
     * @param vacationRequestDTO The vacation request to validate
     */
    private void validateVacationDates(VacationRequestDTO vacationRequestDTO) {
        if (vacationRequestDTO.getStartDate() == null || vacationRequestDTO.getEndDate() == null) {
            throw new IllegalArgumentException("Vacation start and end dates cannot be null");
        }
        
        if (vacationRequestDTO.getEndDate().isBefore(vacationRequestDTO.getStartDate())) {
            throw new IllegalArgumentException("Vacation end date must not be before start date");
        }
        
        if (vacationRequestDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Vacation start date cannot be in the past");
        }
    }

    /**
     * Check for conflicts with existing shifts
     * @param vacationRequestDTO The vacation request to check
     */
    private void checkShiftConflicts(VacationRequestDTO vacationRequestDTO) {
        LocalDateTime startDateTime = vacationRequestDTO.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = vacationRequestDTO.getEndDate().plusDays(1).atStartOfDay();
        
        // Get any shifts that overlap with the requested vacation period
        List<Long> overlappingShifts = shiftRepository.findAll().stream()
                .filter(shift -> shift.getEmployee().getId().equals(vacationRequestDTO.getEmployeeId()))
                .filter(shift -> shift.getStartTime().isBefore(endDateTime) && shift.getEndTime().isAfter(startDateTime))
                .map(shift -> shift.getId())
                .collect(Collectors.toList());
        
        if (!overlappingShifts.isEmpty()) {
            throw new IllegalStateException("Vacation request conflicts with existing shifts");
        }
    }

    /**
     * Maps VacationRequestDTO to VacationRequest entity
     * @param dto The DTO to map
     * @return The mapped entity
     */
    private VacationRequest mapDtoToEntity(VacationRequestDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
        
        VacationRequest vacationRequest = VacationRequest.builder()
                .id(dto.getId())
                .employee(employee)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus() != null ? dto.getStatus() : VacationStatus.PENDING)
                .reason(dto.getReason())
                .build();
        
        // Set manager if provided
        if (dto.getManagerId() != null) {
            Manager manager = managerRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + dto.getManagerId()));
            vacationRequest.setManager(manager);
            vacationRequest.setManagerComments(dto.getManagerComments());
            vacationRequest.setReviewedAt(dto.getReviewedAt());
        }
        
        return vacationRequest;
    }

    /**
     * Maps VacationRequest entity to VacationRequestDTO
     * @param vacationRequest The entity to map
     * @return The mapped DTO
     */
    private VacationRequestDTO mapEntityToDto(VacationRequest vacationRequest) {
        VacationRequestDTO dto = VacationRequestDTO.builder()
                .id(vacationRequest.getId())
                .employeeId(vacationRequest.getEmployee().getId())
                .employeeName(vacationRequest.getEmployee().getName().getFullName())
                .startDate(vacationRequest.getStartDate())
                .endDate(vacationRequest.getEndDate())
                .status(vacationRequest.getStatus())
                .reason(vacationRequest.getReason())
                .managerComments(vacationRequest.getManagerComments())
                .reviewedAt(vacationRequest.getReviewedAt())
                .daysCount(vacationRequest.getDaysCount())
                .createdAt(vacationRequest.getCreatedAt())
                .updatedAt(vacationRequest.getUpdatedAt())
                .build();
        
        // Set manager if available
        if (vacationRequest.getManager() != null) {
            dto.setManagerId(vacationRequest.getManager().getId());
            dto.setManagerName(vacationRequest.getManager().getName().getFullName());
        }
        
        return dto;
    }

    /**
     * Updates a VacationRequest entity from DTO
     * @param vacationRequest The entity to update
     * @param dto The DTO containing updated data
     */
    private void updateVacationRequestFromDto(VacationRequest vacationRequest, VacationRequestDTO dto) {
        if (dto.getStartDate() != null) {
            vacationRequest.setStartDate(dto.getStartDate());
        }
        
        if (dto.getEndDate() != null) {
            vacationRequest.setEndDate(dto.getEndDate());
        }
        
        if (dto.getReason() != null) {
            vacationRequest.setReason(dto.getReason());
        }
    }

    /**
     * Notifies the manager about a new vacation request
     * @param vacationRequest The vacation request
     */
    private void notifyManagerAboutVacationRequest(VacationRequest vacationRequest) {
        // Only notify if the employee has a manager
        if (vacationRequest.getEmployee().getManager() != null) {
            Long managerId = vacationRequest.getEmployee().getManager().getId();
            String employeeName = vacationRequest.getEmployee().getName().getFullName();
            
            String title = "New Vacation Request";
            String message = String.format(
                    "New vacation request from %s for %s to %s. Reason: %s",
                    employeeName,
                    vacationRequest.getStartDate(),
                    vacationRequest.getEndDate(),
                    vacationRequest.getReason() != null ? vacationRequest.getReason() : "Not specified"
            );
            
            notificationService.createNotification(managerId, title, message);
        }
    }

    /**
     * Notifies the manager about an updated vacation request
     * @param vacationRequest The vacation request
     */
    private void notifyManagerAboutVacationUpdate(VacationRequest vacationRequest) {
        // Only notify if the employee has a manager
        if (vacationRequest.getEmployee().getManager() != null) {
            Long managerId = vacationRequest.getEmployee().getManager().getId();
            String employeeName = vacationRequest.getEmployee().getName().getFullName();
            
            String title = "Updated Vacation Request";
            String message = String.format(
                    "Vacation request from %s has been updated. New dates: %s to %s. Reason: %s",
                    employeeName,
                    vacationRequest.getStartDate(),
                    vacationRequest.getEndDate(),
                    vacationRequest.getReason() != null ? vacationRequest.getReason() : "Not specified"
            );
            
            notificationService.createNotification(managerId, title, message);
        }
    }

    /**
     * Notifies the employee about the status of their vacation request
     * @param vacationRequest The vacation request
     * @param title The notification title
     * @param message The notification message
     */
    private void notifyEmployeeAboutVacationStatus(VacationRequest vacationRequest, String title, String message) {
        notificationService.createNotification(vacationRequest.getEmployee().getId(), title, message);
    }

    /**
     * Notifies the manager about a cancelled vacation request
     * @param vacationRequest The vacation request
     */
    private void notifyManagerAboutVacationCancellation(VacationRequest vacationRequest) {
        if (vacationRequest.getManager() != null) {
            Long managerId = vacationRequest.getManager().getId();
            String employeeName = vacationRequest.getEmployee().getName().getFullName();
            
            String title = "Vacation Request Cancelled";
            String message = String.format(
                    "Vacation request from %s for %s to %s has been cancelled.",
                    employeeName,
                    vacationRequest.getStartDate(),
                    vacationRequest.getEndDate()
            );
            
            notificationService.createNotification(managerId, title, message);
        }
    }
}
