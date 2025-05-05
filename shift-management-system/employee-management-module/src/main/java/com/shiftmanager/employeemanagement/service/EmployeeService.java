package com.shiftmanager.employeemanagement.service;

import com.shiftmanager.employeemanagement.model.Employee;
import com.shiftmanager.employeemanagement.model.Manager;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for employee operations
 */
public interface EmployeeService {

    /**
     * Create a new employee
     * @param employee The employee to create
     * @return The created employee with ID assigned
     */
    Employee createEmployee(Employee employee);

    /**
     * Get an employee by ID
     * @param id The employee ID
     * @return The employee if found
     * @throws javax.persistence.EntityNotFoundException if employee not found
     */
    Employee getEmployeeById(Long id);

    /**
     * Get an employee by employee ID
     * @param employeeId The unique employee ID
     * @return The employee if found
     * @throws javax.persistence.EntityNotFoundException if employee not found
     */
    Employee getEmployeeByEmployeeId(String employeeId);

    /**
     * Update an existing employee
     * @param id The employee ID
     * @param employeeDetails The updated employee details
     * @return The updated employee
     * @throws javax.persistence.EntityNotFoundException if employee not found
     */
    Employee updateEmployee(Long id, Employee employeeDetails);

    /**
     * Delete an employee
     * @param id The employee ID
     * @throws javax.persistence.EntityNotFoundException if employee not found
     */
    void deleteEmployee(Long id);

    /**
     * Get all employees
     * @return List of all employees
     */
    List<Employee> getAllEmployees();

    /**
     * Get employees by department
     * @param department The department name
     * @return List of employees in the department
     */
    List<Employee> getEmployeesByDepartment(String department);

    /**
     * Get employees by manager
     * @param managerId The manager's ID
     * @return List of employees under the manager
     */
    List<Employee> getEmployeesByManager(Long managerId);

    /**
     * Assign a manager to an employee
     * @param employeeId The employee ID
     * @param manager The manager to assign
     * @return The updated employee
     * @throws javax.persistence.EntityNotFoundException if employee not found
     */
    Employee assignManager(Long employeeId, Manager manager);

    /**
     * Update employee employment status
     * @param employeeId The employee ID
     * @param status The new employment status
     * @return The updated employee
     * @throws javax.persistence.EntityNotFoundException if employee not found
     */
    Employee updateEmploymentStatus(Long employeeId, Employee.EmploymentStatus status);

    /**
     * Search employees by criteria
     * @param department Optional department name
     * @param jobTitle Optional job title
     * @param employmentType Optional employment type
     * @param employmentStatus Optional employment status
     * @param hiredAfter Optional hire date filter
     * @return List of employees matching the criteria
     */
    List<Employee> searchEmployees(String department, String jobTitle, 
                                Employee.EmploymentType employmentType,
                                Employee.EmploymentStatus employmentStatus,
                                LocalDate hiredAfter);
}
