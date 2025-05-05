package com.shiftmanager.employeemanagement.controller;

import com.shiftmanager.employeemanagement.model.Employee;
import com.shiftmanager.employeemanagement.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST controller for employee operations
 */
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Create a new employee
     * @param employee Employee data to create
     * @return The created employee
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        Employee newEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    /**
     * Get an employee by ID
     * @param id The employee ID
     * @return The employee if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get an employee by employee ID
     * @param employeeId The employee ID
     * @return The employee if found
     */
    @GetMapping("/employee-id/{employeeId}")
    public ResponseEntity<Employee> getEmployeeByEmployeeId(@PathVariable String employeeId) {
        try {
            Employee employee = employeeService.getEmployeeByEmployeeId(employeeId);
            return ResponseEntity.ok(employee);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update an employee
     * @param id The employee ID
     * @param employeeDetails Updated employee details
     * @return The updated employee
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, 
                                                @Valid @RequestBody Employee employeeDetails) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
            return ResponseEntity.ok(updatedEmployee);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete an employee
     * @param id The employee ID
     * @return Empty response with appropriate status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all employees
     * @return List of all employees
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * Get employees by department
     * @param department The department name
     * @return List of employees in the department
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable String department) {
        List<Employee> employees = employeeService.getEmployeesByDepartment(department);
        return ResponseEntity.ok(employees);
    }

    /**
     * Get employees by manager
     * @param managerId The manager ID
     * @return List of employees under the manager
     */
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Employee>> getEmployeesByManager(@PathVariable Long managerId) {
        List<Employee> employees = employeeService.getEmployeesByManager(managerId);
        return ResponseEntity.ok(employees);
    }

    /**
     * Update employee's employment status
     * @param id The employee ID
     * @param statusRequest Object containing the status update
     * @return The updated employee
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Employee> updateEmploymentStatus(@PathVariable Long id, 
                                                       @RequestBody Map<String, String> statusRequest) {
        String statusValue = statusRequest.get("status");
        if (statusValue == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Employee.EmploymentStatus status = Employee.EmploymentStatus.valueOf(statusValue);
            Employee updatedEmployee = employeeService.updateEmploymentStatus(id, status);
            return ResponseEntity.ok(updatedEmployee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Search employees by criteria
     * @param department Optional department
     * @param jobTitle Optional job title
     * @param employmentType Optional employment type
     * @param employmentStatus Optional employment status
     * @param hiredAfter Optional hire date filter
     * @return List of matching employees
     */
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) Employee.EmploymentType employmentType,
            @RequestParam(required = false) Employee.EmploymentStatus employmentStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hiredAfter) {
        
        List<Employee> employees = employeeService.searchEmployees(
                department, jobTitle, employmentType, employmentStatus, hiredAfter);
        return ResponseEntity.ok(employees);
    }
}
