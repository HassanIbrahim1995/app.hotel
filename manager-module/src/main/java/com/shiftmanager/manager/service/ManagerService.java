package com.shiftmanager.manager.service;

import com.shiftmanager.manager.dto.ManagerDTO;

import java.util.List;

/**
 * Service interface for Manager operations
 */
public interface ManagerService {

    /**
     * Create a new manager
     * @param managerDTO The manager data
     * @return The created manager
     */
    ManagerDTO createManager(ManagerDTO managerDTO);

    /**
     * Get a manager by ID
     * @param id The manager ID
     * @return The manager data
     */
    ManagerDTO getManagerById(Long id);

    /**
     * Get all managers
     * @return List of all managers
     */
    List<ManagerDTO> getAllManagers();

    /**
     * Update a manager
     * @param id The manager ID
     * @param managerDTO The updated manager data
     * @return The updated manager
     */
    ManagerDTO updateManager(Long id, ManagerDTO managerDTO);

    /**
     * Delete a manager
     * @param id The manager ID
     */
    void deleteManager(Long id);

    /**
     * Get managers by department
     * @param department The department
     * @return List of managers in the specified department
     */
    List<ManagerDTO> getManagersByDepartment(String department);

    /**
     * Get managers by level
     * @param level The manager level
     * @return List of managers with the specified level
     */
    List<ManagerDTO> getManagersByLevel(String level);

    /**
     * Get managers by location
     * @param locationId The location ID
     * @return List of managers at the specified location
     */
    List<ManagerDTO> getManagersByLocation(Long locationId);

    /**
     * Get manager by manager number
     * @param managerNumber The manager number
     * @return The manager data
     */
    ManagerDTO getManagerByManagerNumber(String managerNumber);
    
    /**
     * Assign an employee to a manager
     * @param managerId The manager ID
     * @param employeeId The employee ID
     * @return The updated manager data
     */
    ManagerDTO assignEmployeeToManager(Long managerId, Long employeeId);
    
    /**
     * Remove an employee from a manager
     * @param managerId The manager ID
     * @param employeeId The employee ID
     * @return The updated manager data
     */
    ManagerDTO removeEmployeeFromManager(Long managerId, Long employeeId);
}
