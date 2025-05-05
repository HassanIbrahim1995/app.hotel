package com.shiftmanager.manager.controller;

import com.shiftmanager.manager.dto.ManagerDTO;
import com.shiftmanager.manager.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for manager operations
 */
@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    /**
     * Create a new manager
     * @param managerDTO The manager data
     * @return The created manager
     */
    @PostMapping
    public ResponseEntity<ManagerDTO> createManager(@Valid @RequestBody ManagerDTO managerDTO) {
        ManagerDTO createdManager = managerService.createManager(managerDTO);
        return new ResponseEntity<>(createdManager, HttpStatus.CREATED);
    }

    /**
     * Get a manager by ID
     * @param id The manager ID
     * @return The manager data
     */
    @GetMapping("/{id}")
    public ResponseEntity<ManagerDTO> getManagerById(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.getManagerById(id));
    }

    /**
     * Get all managers
     * @return List of all managers
     */
    @GetMapping
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        return ResponseEntity.ok(managerService.getAllManagers());
    }

    /**
     * Update a manager
     * @param id The manager ID
     * @param managerDTO The updated manager data
     * @return The updated manager
     */
    @PutMapping("/{id}")
    public ResponseEntity<ManagerDTO> updateManager(
            @PathVariable Long id, 
            @Valid @RequestBody ManagerDTO managerDTO) {
        return ResponseEntity.ok(managerService.updateManager(id, managerDTO));
    }

    /**
     * Delete a manager
     * @param id The manager ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long id) {
        managerService.deleteManager(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get managers by department
     * @param department The department
     * @return List of managers in the specified department
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<ManagerDTO>> getManagersByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(managerService.getManagersByDepartment(department));
    }

    /**
     * Get managers by level
     * @param level The manager level
     * @return List of managers with the specified level
     */
    @GetMapping("/level/{level}")
    public ResponseEntity<List<ManagerDTO>> getManagersByLevel(@PathVariable String level) {
        return ResponseEntity.ok(managerService.getManagersByLevel(level));
    }

    /**
     * Get managers by location
     * @param locationId The location ID
     * @return List of managers at the specified location
     */
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<ManagerDTO>> getManagersByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(managerService.getManagersByLocation(locationId));
    }

    /**
     * Get manager by manager number
     * @param managerNumber The manager number
     * @return The manager data
     */
    @GetMapping("/number/{managerNumber}")
    public ResponseEntity<ManagerDTO> getManagerByManagerNumber(@PathVariable String managerNumber) {
        return ResponseEntity.ok(managerService.getManagerByManagerNumber(managerNumber));
    }
    
    /**
     * Assign an employee to a manager
     * @param managerId The manager ID
     * @param employeeId The employee ID
     * @return The updated manager data
     */
    @PostMapping("/{managerId}/employees/{employeeId}")
    public ResponseEntity<ManagerDTO> assignEmployeeToManager(
            @PathVariable Long managerId, 
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(managerService.assignEmployeeToManager(managerId, employeeId));
    }
    
    /**
     * Remove an employee from a manager
     * @param managerId The manager ID
     * @param employeeId The employee ID
     * @return The updated manager data
     */
    @DeleteMapping("/{managerId}/employees/{employeeId}")
    public ResponseEntity<ManagerDTO> removeEmployeeFromManager(
            @PathVariable Long managerId, 
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(managerService.removeEmployeeFromManager(managerId, employeeId));
    }
}
