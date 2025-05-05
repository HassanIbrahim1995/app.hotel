package com.shiftmanager.core.repository;

import com.shiftmanager.core.domain.Manager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Manager entity operations
 */
@Repository
public interface ManagerRepository extends PersonRepository<Manager> {
    
    /**
     * Find a manager by their manager number
     * @param managerNumber The manager number to search for
     * @return Optional containing the manager if found
     */
    Optional<Manager> findByManagerNumber(String managerNumber);
    
    /**
     * Find managers by manager level
     * @param managerLevel The manager level to search for
     * @return List of managers with the specified level
     */
    List<Manager> findByManagerLevel(String managerLevel);
    
    /**
     * Find managers by department
     * @param department The department to search for
     * @return List of managers in the specified department
     */
    List<Manager> findByDepartment(String department);
    
    /**
     * Find managers by location
     * @param locationId The location ID
     * @return List of managers at the specified location
     */
    List<Manager> findByLocationId(Long locationId);
    
    /**
     * Count the number of employees managed by a specific manager
     * @param managerId The manager ID
     * @return The count of employees managed
     */
    Integer countByManagedEmployeesManagerId(Long managerId);
}
