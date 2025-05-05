package com.shiftmanager.employee.controller;

import com.shiftmanager.employee.dto.EmployeeDTO;
import com.shiftmanager.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for employee operations
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Create a new employee
     * @param employeeDTO The employee data
     * @return The created employee
     */
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    /**
     * Get an employee by ID
     * @param id The employee ID
     * @return The employee data
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    /**
     * Get all employees
     * @return List of all employees
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    /**
     * Update an employee
     * @param id The employee ID
     * @param employeeDTO The updated employee data
     * @return The updated employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Long id, 
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    /**
     * Delete an employee
     * @param id The employee ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get employees by department
     * @param department The department
     * @return List of employees in the specified department
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(department));
    }

    /**
     * Get employees by manager
     * @param managerId The manager ID
     * @return List of employees under the specified manager
     */
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByManager(@PathVariable Long managerId) {
        return ResponseEntity.ok(employeeService.getEmployeesByManager(managerId));
    }

    /**
     * Get employees by location
     * @param locationId The location ID
     * @return List of employees at the specified location
     */
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(employeeService.getEmployeesByLocation(locationId));
    }

    /**
     * Get employees available during a date range (not on vacation)
     * @param startDate The start date
     * @param endDate The end date
     * @return List of available employees
     */
    @GetMapping("/available")
    public ResponseEntity<List<EmployeeDTO>> getAvailableEmployees(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(employeeService.getAvailableEmployeesInDateRange(startDate, endDate));
    }

    /**
     * Get employee by employee number
     * @param employeeNumber The employee number
     * @return The employee data
     */
    @GetMapping("/number/{employeeNumber}")
    public ResponseEntity<EmployeeDTO> getEmployeeByEmployeeNumber(@PathVariable String employeeNumber) {
        return ResponseEntity.ok(employeeService.getEmployeeByEmployeeNumber(employeeNumber));
    }

    /**
     * Get employees without shifts in a date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of employees without scheduled shifts
     */
    @GetMapping("/unscheduled")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesWithoutShifts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(employeeService.getEmployeesWithoutShiftsInRange(startDate, endDate));
    }
}
