package com.shiftmanager.vacation.controller;

import com.shiftmanager.core.domain.VacationStatus;
import com.shiftmanager.vacation.dto.VacationRequestDTO;
import com.shiftmanager.vacation.service.VacationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for vacation operations
 */
@RestController
@RequestMapping("/api/vacations")
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;

    /**
     * Request a vacation
     * @param vacationRequestDTO The vacation request data
     * @return The created vacation request
     */
    @PostMapping
    public ResponseEntity<VacationRequestDTO> requestVacation(@Valid @RequestBody VacationRequestDTO vacationRequestDTO) {
        return new ResponseEntity<>(vacationService.requestVacation(vacationRequestDTO), HttpStatus.CREATED);
    }

    /**
     * Get a vacation request by ID
     * @param id The vacation request ID
     * @return The vacation request data
     */
    @GetMapping("/{id}")
    public ResponseEntity<VacationRequestDTO> getVacationRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(vacationService.getVacationRequestById(id));
    }

    /**
     * Get all vacation requests
     * @return List of all vacation requests
     */
    @GetMapping
    public ResponseEntity<List<VacationRequestDTO>> getAllVacationRequests() {
        return ResponseEntity.ok(vacationService.getAllVacationRequests());
    }

    /**
     * Update a vacation request
     * @param id The vacation request ID
     * @param vacationRequestDTO The updated vacation request data
     * @return The updated vacation request
     */
    @PutMapping("/{id}")
    public ResponseEntity<VacationRequestDTO> updateVacationRequest(
            @PathVariable Long id, 
            @Valid @RequestBody VacationRequestDTO vacationRequestDTO) {
        return ResponseEntity.ok(vacationService.updateVacationRequest(id, vacationRequestDTO));
    }

    /**
     * Delete a vacation request
     * @param id The vacation request ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacationRequest(@PathVariable Long id) {
        vacationService.deleteVacationRequest(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get vacation requests by employee
     * @param employeeId The employee ID
     * @return List of vacation requests for the specified employee
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<VacationRequestDTO>> getVacationRequestsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(vacationService.getVacationRequestsByEmployee(employeeId));
    }

    /**
     * Get vacation requests by manager
     * @param managerId The manager ID
     * @return List of vacation requests handled by the specified manager
     */
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<VacationRequestDTO>> getVacationRequestsByManager(@PathVariable Long managerId) {
        return ResponseEntity.ok(vacationService.getVacationRequestsByManager(managerId));
    }

    /**
     * Get vacation requests by status
     * @param status The vacation request status
     * @return List of vacation requests with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<VacationRequestDTO>> getVacationRequestsByStatus(@PathVariable VacationStatus status) {
        return ResponseEntity.ok(vacationService.getVacationRequestsByStatus(status));
    }

    /**
     * Get vacation requests by employee and status
     * @param employeeId The employee ID
     * @param status The vacation request status
     * @return List of vacation requests for the specified employee with the specified status
     */
    @GetMapping("/employee/{employeeId}/status/{status}")
    public ResponseEntity<List<VacationRequestDTO>> getVacationRequestsByEmployeeAndStatus(
            @PathVariable Long employeeId, 
            @PathVariable VacationStatus status) {
        return ResponseEntity.ok(vacationService.getVacationRequestsByEmployeeAndStatus(employeeId, status));
    }

    /**
     * Get vacation requests by date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of vacation requests within the specified date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<VacationRequestDTO>> getVacationRequestsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(vacationService.getVacationRequestsByDateRange(startDate, endDate));
    }

    /**
     * Approve a vacation request
     * @param id The vacation request ID
     * @param managerId The manager ID
     * @param comments Optional comments from the manager
     * @return The updated vacation request
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<VacationRequestDTO> approveVacationRequest(
            @PathVariable Long id,
            @RequestParam Long managerId,
            @RequestParam(required = false) String comments) {
        return ResponseEntity.ok(vacationService.approveVacationRequest(id, managerId, comments));
    }

    /**
     * Reject a vacation request
     * @param id The vacation request ID
     * @param managerId The manager ID
     * @param comments Required comments explaining the rejection reason
     * @return The updated vacation request
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<VacationRequestDTO> rejectVacationRequest(
            @PathVariable Long id,
            @RequestParam Long managerId,
            @RequestParam String comments) {
        return ResponseEntity.ok(vacationService.rejectVacationRequest(id, managerId, comments));
    }

    /**
     * Cancel a vacation request
     * @param id The vacation request ID
     * @return The updated vacation request
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<VacationRequestDTO> cancelVacationRequest(@PathVariable Long id) {
        return ResponseEntity.ok(vacationService.cancelVacationRequest(id));
    }

    /**
     * Get pending vacation requests for a manager's department
     * @param managerId The manager ID
     * @return List of pending vacation requests from employees in the manager's department
     */
    @GetMapping("/manager/{managerId}/pending")
    public ResponseEntity<List<VacationRequestDTO>> getPendingRequestsByManagerDepartment(@PathVariable Long managerId) {
        return ResponseEntity.ok(vacationService.getPendingRequestsByManagerDepartment(managerId));
    }
}
