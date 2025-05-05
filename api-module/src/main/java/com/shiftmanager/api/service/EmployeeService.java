package com.shiftmanager.api.service;

import com.shiftmanager.api.dto.EmployeeDTO;
import com.shiftmanager.api.dto.ShiftDTO;
import com.shiftmanager.api.dto.VacationRequestDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing employees
 */
public interface EmployeeService {
    
    /**
     * Get all employees
     * @return List of employee DTOs
     */
    List<EmployeeDTO> getAllEmployees();
    
    /**
     * Get employee by ID
     * @param id Employee ID
     * @return Optional employee DTO
     */
    Optional<EmployeeDTO> getEmployeeById(Long id);
    
    /**
     * Create a new employee
     * @param employeeDTO Employee data
     * @return Created employee DTO
     */
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    
    /**
     * Update an existing employee
     * @param id Employee ID
     * @param employeeDTO Updated employee data
     * @return Updated employee DTO
     */
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    
    /**
     * Delete an employee
     * @param id Employee ID
     */
    void deleteEmployee(Long id);
    
    /**
     * Get schedule for an employee for a date range
     * @param employeeId Employee ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of shifts scheduled for the employee in the date range
     */
    List<ShiftDTO> getEmployeeSchedule(Long employeeId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Get vacation requests for an employee
     * @param employeeId Employee ID
     * @return List of vacation requests for the employee
     */
    List<VacationRequestDTO> getEmployeeVacationRequests(Long employeeId);
    
    /**
     * Generate a personal calendar for an employee with their shifts and vacation days
     * @param employeeId Employee ID
     * @param startDate Start date
     * @param endDate End date
     * @return Calendar data with shifts and vacation days
     */
    byte[] generateEmployeeCalendar(Long employeeId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Export employee schedule to PDF
     * @param employeeId Employee ID
     * @param startDate Start date
     * @param endDate End date
     * @return PDF document as byte array
     */
    byte[] exportScheduleToPdf(Long employeeId, LocalDate startDate, LocalDate endDate);
}