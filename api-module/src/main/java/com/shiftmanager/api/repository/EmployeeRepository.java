package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Employee entity
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    /**
     * Find employees by status
     * @param status Status
     * @return List of employees
     */
    List<Employee> findByStatus(String status);
    
    /**
     * Find active employees
     * @return List of active employees
     */
    List<Employee> findByStatusIgnoreCase(String status);
    
    /**
     * Find employees by location
     * @param location Location
     * @return List of employees
     */
    List<Employee> findByLocation(Location location);
    
    /**
     * Find employees by manager
     * @param manager Manager
     * @return List of employees
     */
    List<Employee> findByManager(Employee manager);
    
    /**
     * Find employees by employee ID
     * @param employeeId Employee ID
     * @return Employee
     */
    Employee findByEmployeeId(String employeeId);
    
    /**
     * Count employees by location
     * @param location Location
     * @return Count of employees
     */
    int countByLocation(Location location);
    
    /**
     * Find employees by department
     * @param department Department
     * @return List of employees
     */
    List<Employee> findByDepartment(String department);
    
    /**
     * Find employees by job title
     * @param jobTitle Job title
     * @return List of employees
     */
    List<Employee> findByJobTitle(String jobTitle);
}