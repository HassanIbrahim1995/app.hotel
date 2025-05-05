package com.shiftmanager.employee.service;

import com.shiftmanager.employee.dto.EmployeeDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Employee operations
 */
public interface EmployeeService {

    /**
     * Create a new employee
     * @param employeeDTO The employee data
     * @return The created employee
     */
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    /**
     * Get an employee by ID
     * @param id The employee ID
     * @return The employee data
     */
    EmployeeDTO getEmployeeById(Long id);

    /**
     * Get all employees
     * @return List of all employees
     */
    List<EmployeeDTO> getAllEmployees();

    /**
     * Update an employee
     * @param id The employee ID
     * @param employeeDTO The updated employee data
     * @return The updated employee
     */
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    /**
     * Delete an employee
     * @param id The employee ID
     */
    void deleteEmployee(Long id);

    /**
     * Get employees by department
     * @param department The department
     * @return List of employees in the specified department
     */
    List<EmployeeDTO> getEmployeesByDepartment(String department);

    /**
     * Get employees by manager
     * @param managerId The manager ID
     * @return List of employees under the specified manager
     */
    List<EmployeeDTO> getEmployeesByManager(Long managerId);

    /**
     * Get employees by location
     * @param locationId The location ID
     * @return List of employees at the specified location
     */
    List<EmployeeDTO> getEmployeesByLocation(Long locationId);

    /**
     * Get employees available during a date range (not on vacation)
     * @param startDate The start date
     * @param endDate The end date
     * @return List of available employees
     */
    List<EmployeeDTO> getAvailableEmployeesInDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get employee by employee number
     * @param employeeNumber The employee number
     * @return The employee data
     */
    EmployeeDTO getEmployeeByEmployeeNumber(String employeeNumber);

    /**
     * Get employees without shifts in a date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of employees without scheduled shifts
     */
    List<EmployeeDTO> getEmployeesWithoutShiftsInRange(LocalDate startDate, LocalDate endDate);
}
