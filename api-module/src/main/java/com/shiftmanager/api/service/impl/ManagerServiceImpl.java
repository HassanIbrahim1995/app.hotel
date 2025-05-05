package com.shiftmanager.api.service.impl;

import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.exception.ValidationErrorResponse;
import com.shiftmanager.api.model.*;
import com.shiftmanager.api.repository.*;
import com.shiftmanager.api.service.ManagerService;
import com.shiftmanager.api.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ManagerService
 */
@Service
@Transactional
@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
public class ManagerServiceImpl implements ManagerService {

    private static final Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private EmployeeShiftRepository employeeShiftRepository;

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean assignShiftToEmployee(Long shiftId, Long employeeId, Long managerId) {
        logger.debug("Manager with ID: {} assigning shift ID: {} to employee ID: {}", managerId, shiftId, employeeId);
        
        // Verify manager exists and is authorized
        Employee manager = getAndVerifyManager(managerId);
        
        // Verify employee exists and reports to this manager
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        if (!employee.getManager().getId().equals(managerId)) {
            throw new ValidationErrorResponse("Not authorized to manage this employee");
        }
        
        // Verify shift exists
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + shiftId));
        
        // Check if employee already assigned to this shift
        if (employeeShiftRepository.findByEmployeeAndShift(employee, shift).isPresent()) {
            throw new ValidationErrorResponse("Employee already assigned to this shift");
        }
        
        // Check if employee already has a shift at the same time
        List<EmployeeShift> conflictingShifts = employeeShiftRepository.findConflictingShifts(
                employee.getId(), 
                shift.getShiftDate(), 
                shift.getStartTime(), 
                shift.getEndTime());
        
        if (!conflictingShifts.isEmpty()) {
            throw new ValidationErrorResponse("Employee already has a shift during this time");
        }
        
        // Create employee shift assignment
        EmployeeShift employeeShift = new EmployeeShift();
        employeeShift.setEmployee(employee);
        employeeShift.setShift(shift);
        employeeShift.setAssignedBy(manager);
        employeeShift.setAssignedAt(LocalDateTime.now());
        employeeShift.setStatus("ASSIGNED");
        
        employeeShiftRepository.save(employeeShift);
        
        // Send notification to employee
        notificationService.sendShiftAssignmentNotification(employeeId, shiftId);
        
        return true;
    }

    @Override
    public boolean unassignShiftFromEmployee(Long shiftId, Long employeeId, Long managerId) {
        logger.debug("Manager with ID: {} unassigning shift ID: {} from employee ID: {}", managerId, shiftId, employeeId);
        
        // Verify manager exists and is authorized
        getAndVerifyManager(managerId);
        
        // Verify employee exists
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        // Verify employee reports to this manager
        if (!employee.getManager().getId().equals(managerId)) {
            throw new ValidationErrorResponse("Not authorized to manage this employee");
        }
        
        // Verify shift exists
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + shiftId));
        
        // Find and delete employee shift assignment
        EmployeeShift employeeShift = employeeShiftRepository.findByEmployeeAndShift(employee, shift)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee with ID: " + employeeId + " is not assigned to shift with ID: " + shiftId));
        
        employeeShiftRepository.delete(employeeShift);
        
        // Create notification for employee
        notificationService.createNotification(
                employeeId,
                "Your assignment to shift on " + shift.getShiftDate() + " has been removed",
                "SHIFT_UNASSIGNED",
                shiftId);
        
        return true;
    }

    @Override
    public Shift adjustEmployeeShift(Long employeeShiftId, Long newShiftId, Long managerId) {
        logger.debug("Manager with ID: {} adjusting employee shift ID: {} to new shift ID: {}", managerId, employeeShiftId, newShiftId);
        
        // Verify manager exists and is authorized
        Employee manager = getAndVerifyManager(managerId);
        
        // Verify employee shift exists
        EmployeeShift employeeShift = employeeShiftRepository.findById(employeeShiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee shift not found with ID: " + employeeShiftId));
        
        // Verify employee reports to this manager
        if (!employeeShift.getEmployee().getManager().getId().equals(managerId)) {
            throw new ValidationErrorResponse("Not authorized to adjust shifts for this employee");
        }
        
        // Verify new shift exists
        Shift newShift = shiftRepository.findById(newShiftId)
                .orElseThrow(() -> new ResourceNotFoundException("New shift not found with ID: " + newShiftId));
        
        // Check for conflicts with the new shift
        List<EmployeeShift> conflictingShifts = employeeShiftRepository.findConflictingShifts(
                employeeShift.getEmployee().getId(), 
                newShift.getShiftDate(), 
                newShift.getStartTime(), 
                newShift.getEndTime());
        
        // Remove current shift from conflicts if it's in the list
        conflictingShifts = conflictingShifts.stream()
                .filter(shift -> !shift.getId().equals(employeeShiftId))
                .collect(Collectors.toList());
        
        if (!conflictingShifts.isEmpty()) {
            throw new ValidationErrorResponse("Employee already has a shift during this time");
        }
        
        // Store old shift details for notification
        Shift oldShift = employeeShift.getShift();
        
        // Update employee shift
        employeeShift.setShift(newShift);
        employeeShift.setAssignedBy(manager);
        employeeShift.setAssignedAt(LocalDateTime.now());
        employeeShift.setStatus("REASSIGNED");
        
        employeeShiftRepository.save(employeeShift);
        
        // Create notification for employee
        notificationService.createNotification(
                employeeShift.getEmployee().getId(),
                "Your shift on " + oldShift.getShiftDate() + " has been changed to " + 
                        newShift.getShiftDate() + " at " + newShift.getStartTime() + 
                        " " + newShift.getLocation().getName() + " (" + newShift.getShiftType().getName() + " shift)",
                "SHIFT_ADJUSTED",
                newShift.getId());
        
        return newShift;
    }

    @Override
    public VacationRequest approveVacationRequest(Long vacationRequestId, Long managerId, String reviewNotes) {
        logger.debug("Manager with ID: {} approving vacation request ID: {}", managerId, vacationRequestId);
        
        // Verify manager exists and is authorized
        getAndVerifyManager(managerId);
        
        // Verify vacation request exists
        VacationRequest vacationRequest = vacationRequestRepository.findById(vacationRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation request not found with ID: " + vacationRequestId));
        
        // Verify employee reports to this manager
        if (!vacationRequest.getEmployee().getManager().getId().equals(managerId)) {
            throw new ValidationErrorResponse("Not authorized to approve vacation requests for this employee");
        }
        
        // Verify request is pending
        if (!"PENDING".equals(vacationRequest.getStatus())) {
            throw new ValidationErrorResponse("Can only approve pending vacation requests");
        }
        
        // Check for overlapping approved requests
        List<VacationRequest> overlappingRequests = vacationRequestRepository.findOverlappingApprovedRequests(
                vacationRequest.getEmployee().getId(), 
                vacationRequest.getStartDate(), 
                vacationRequest.getEndDate());
        
        if (!overlappingRequests.isEmpty()) {
            throw new ValidationErrorResponse("Employee already has approved vacation during this period");
        }
        
        // Update vacation request
        vacationRequest.setStatus("APPROVED");
        vacationRequest.setReviewedBy(managerId);
        vacationRequest.setReviewDate(LocalDateTime.now());
        vacationRequest.setReviewNotes(reviewNotes);
        
        VacationRequest approvedRequest = vacationRequestRepository.save(vacationRequest);
        
        // Send notification to employee
        notificationService.sendVacationRequestStatusNotification(
                vacationRequest.getEmployee().getId(), 
                vacationRequestId, 
                true);
        
        return approvedRequest;
    }

    @Override
    public VacationRequest rejectVacationRequest(Long vacationRequestId, Long managerId, String reviewNotes) {
        logger.debug("Manager with ID: {} rejecting vacation request ID: {}", managerId, vacationRequestId);
        
        // Verify manager exists and is authorized
        getAndVerifyManager(managerId);
        
        // Verify vacation request exists
        VacationRequest vacationRequest = vacationRequestRepository.findById(vacationRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation request not found with ID: " + vacationRequestId));
        
        // Verify employee reports to this manager
        if (!vacationRequest.getEmployee().getManager().getId().equals(managerId)) {
            throw new ValidationErrorResponse("Not authorized to reject vacation requests for this employee");
        }
        
        // Verify request is pending
        if (!"PENDING".equals(vacationRequest.getStatus())) {
            throw new ValidationErrorResponse("Can only reject pending vacation requests");
        }
        
        // Update vacation request
        vacationRequest.setStatus("REJECTED");
        vacationRequest.setReviewedBy(managerId);
        vacationRequest.setReviewDate(LocalDateTime.now());
        vacationRequest.setReviewNotes(reviewNotes);
        
        VacationRequest rejectedRequest = vacationRequestRepository.save(vacationRequest);
        
        // Send notification to employee
        notificationService.sendVacationRequestStatusNotification(
                vacationRequest.getEmployee().getId(), 
                vacationRequestId, 
                false);
        
        return rejectedRequest;
    }

    @Override
    public List<VacationRequest> getPendingVacationRequestsForTeam(Long managerId) {
        logger.debug("Getting pending vacation requests for manager ID: {}", managerId);
        
        // Verify manager exists and is authorized
        getAndVerifyManager(managerId);
        
        // Get all subordinates
        List<Employee> subordinates = employeeRepository.findByManagerId(managerId);
        
        if (subordinates.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Get pending vacation requests for all subordinates
        return vacationRequestRepository.findByEmployeeInAndStatus(
                subordinates, "PENDING");
    }

    @Override
    public List<Employee> getTeamMembers(Long managerId) {
        logger.debug("Getting team members for manager ID: {}", managerId);
        
        // Verify manager exists and is authorized
        getAndVerifyManager(managerId);
        
        // Get all subordinates
        return employeeRepository.findByManagerId(managerId);
    }

    @Override
    public List<Shift> getTeamSchedule(Long managerId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting team schedule for manager ID: {} from {} to {}", managerId, startDate, endDate);
        
        // Verify manager exists and is authorized
        getAndVerifyManager(managerId);
        
        // Get all subordinates
        List<Employee> subordinates = employeeRepository.findByManagerId(managerId);
        
        if (subordinates.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Get all shifts for subordinates in date range
        return shiftRepository.findByEmployeesInManagerTeamAndDateRange(
                managerId, startDate, endDate);
    }

    @Override
    public Shift createShift(Shift shift, Long managerId) {
        logger.debug("Manager with ID: {} creating new shift", managerId);
        
        // Verify manager exists and is authorized
        getAndVerifyManager(managerId);
        
        // Set created by
        shift.setCreatedById(managerId);
        
        return shiftRepository.save(shift);
    }

    @Override
    public Shift updateShift(Long shiftId, Shift updatedShift, Long managerId) {
        logger.debug("Manager with ID: {} updating shift ID: {}", managerId, shiftId);
        
        // Verify manager exists and is authorized
        getAndVerifyManager(managerId);
        
        // Verify shift exists
        Shift existingShift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + shiftId));
        
        // Update shift fields
        existingShift.setShiftDate(updatedShift.getShiftDate());
        existingShift.setStartTime(updatedShift.getStartTime());
        existingShift.setEndTime(updatedShift.getEndTime());
        existingShift.setLocation(updatedShift.getLocation());
        existingShift.setShiftType(updatedShift.getShiftType());
        existingShift.setNotes(updatedShift.getNotes());
        
        // Mark as updated
        existingShift.setUpdatedById(managerId);
        
        Shift savedShift = shiftRepository.save(existingShift);
        
        // Notify all employees assigned to this shift
        List<EmployeeShift> assignedEmployees = employeeShiftRepository.findByShift(existingShift);
        for (EmployeeShift assignment : assignedEmployees) {
            notificationService.createNotification(
                    assignment.getEmployee().getId(),
                    "Your shift on " + existingShift.getShiftDate() + " has been updated",
                    "SHIFT_UPDATED",
                    shiftId);
        }
        
        return savedShift;
    }

    @Override
    public boolean deleteShift(Long shiftId, Long managerId) {
        logger.debug("Manager with ID: {} deleting shift ID: {}", managerId, shiftId);
        
        // Verify manager exists and is authorized
        getAndVerifyManager(managerId);
        
        // Verify shift exists
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + shiftId));
        
        // Get all employees assigned to this shift for notifications
        List<EmployeeShift> assignedEmployees = employeeShiftRepository.findByShift(shift);
        
        // Delete all assignments (will cascade due to foreign key constraints)
        employeeShiftRepository.deleteByShift(shift);
        
        // Delete the shift
        shiftRepository.delete(shift);
        
        // Notify all employees who were assigned to this shift
        for (EmployeeShift assignment : assignedEmployees) {
            notificationService.createNotification(
                    assignment.getEmployee().getId(),
                    "Your shift on " + shift.getShiftDate() + " at " + 
                            shift.getLocation().getName() + " has been canceled",
                    "SHIFT_CANCELED",
                    null);
        }
        
        return true;
    }

    @Override
    @PreAuthorize("permitAll")
    public boolean isManager(Long employeeId) {
        // Check if employee exists and is a manager
        return employeeRepository.findById(employeeId)
                .map(employee -> employee instanceof Manager)
                .orElse(false);
    }
    
    /**
     * Helper method to verify manager exists and is authorized
     * @param managerId Manager ID
     * @return Manager employee
     */
    private Employee getAndVerifyManager(Long managerId) {
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + managerId));
        
        // Verify employee is a manager
        if (!(manager instanceof Manager)) {
            throw new ValidationErrorResponse("Employee with ID: " + managerId + " is not a manager");
        }
        
        return manager;
    }
}