package com.shiftmanager.api.controller;

import com.shiftmanager.api.dto.VacationRequestDTO;
import com.shiftmanager.api.mapper.VacationRequestMapper;
import com.shiftmanager.api.model.VacationRequest;
import com.shiftmanager.api.service.VacationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for vacation request operations
 */
@RestController
@RequestMapping("/api/vacation-requests")
public class VacationRequestController {

    @Autowired
    private VacationRequestService vacationRequestService;

    @Autowired
    private VacationRequestMapper vacationRequestMapper;

    /**
     * Get all vacation requests (admin only)
     * @param startDate Optional start date filter
     * @param endDate Optional end date filter
     * @param status Optional status filter
     * @return List of vacation requests
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<VacationRequestDTO>> getAllVacationRequests(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status) {

        List<VacationRequest> vacationRequests = vacationRequestService.getAllVacationRequests(startDate, endDate, status);
        List<VacationRequestDTO> vacationRequestDTOs = vacationRequests.stream()
                .map(vacationRequestMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(vacationRequestDTOs);
    }

    /**
     * Get vacation request by ID
     * @param id Vacation request ID
     * @return Vacation request
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isVacationRequestOwnerOrManager(#id)")
    public ResponseEntity<VacationRequestDTO> getVacationRequestById(@PathVariable Long id) {
        VacationRequest vacationRequest = vacationRequestService.getVacationRequestById(id);
        VacationRequestDTO vacationRequestDTO = vacationRequestMapper.toDto(vacationRequest);

        return ResponseEntity.ok(vacationRequestDTO);
    }

    /**
     * Update vacation request (only for pending requests)
     * @param id Vacation request ID
     * @param vacationRequestDTO Vacation request data
     * @return Updated vacation request
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isVacationRequestOwner(#id)")
    public ResponseEntity<VacationRequestDTO> updateVacationRequest(
            @PathVariable Long id,
            @Valid @RequestBody VacationRequestDTO vacationRequestDTO) {

        VacationRequest vacationRequest = vacationRequestMapper.toEntity(vacationRequestDTO);
        VacationRequest updatedVacationRequest = vacationRequestService.updateVacationRequest(id, vacationRequest);
        VacationRequestDTO updatedVacationRequestDTO = vacationRequestMapper.toDto(updatedVacationRequest);

        return ResponseEntity.ok(updatedVacationRequestDTO);
    }

    /**
     * Cancel vacation request (only for pending requests)
     * @param id Vacation request ID
     * @return Cancel status
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.isVacationRequestOwner(#id)")
    public ResponseEntity<Map<String, Object>> cancelVacationRequest(@PathVariable Long id) {
        vacationRequestService.cancelVacationRequest(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Vacation request canceled successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Check if employee has vacation conflicts
     * @param employeeId Employee ID
     * @param startDate Start date
     * @param endDate End date
     * @param excludeRequestId Optional request ID to exclude from check
     * @return Conflict check result
     */
    @GetMapping("/check-conflicts")
    public ResponseEntity<Map<String, Object>> checkVacationConflicts(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long excludeRequestId) {

        boolean hasConflicts = vacationRequestService.hasVacationConflicts(employeeId, startDate, endDate, excludeRequestId);

        Map<String, Object> response = new HashMap<>();
        response.put("hasConflicts", hasConflicts);

        return ResponseEntity.ok(response);
    }
}