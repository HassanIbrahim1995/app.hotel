package com.shiftmanager.core.repository;

import com.shiftmanager.core.domain.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Employee entity operations
 */
@Repository
public interface EmployeeRepository extends PersonRepository<Employee> {
    
    /**
     * Find an employee by their employee number
     * @param employeeNumber The employee number to search for
     * @return Optional containing the employee if found
     */
    Optional<Employee> findByEmployeeNumber(String employeeNumber);
    
    /**
     * Find employees by department
     * @param department The department to search for
     * @return List of employees in the specified department
     */
    List<Employee> findByDepartment(String department);
    
    /**
     * Find employees by position
     * @param position The position to search for
     * @return List of employees with the specified position
     */
    List<Employee> findByPosition(String position);
    
    /**
     * Find employees by manager
     * @param managerId The manager ID
     * @return List of employees under the specified manager
     */
    List<Employee> findByManagerId(Long managerId);
    
    /**
     * Find employees hired after a specific date
     * @param date The date to compare against
     * @return List of employees hired after the specified date
     */
    List<Employee> findByHireDateAfter(LocalDate date);
    
    /**
     * Find employees without scheduled shifts in a date range
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return List of employees without scheduled shifts in the specified range
     */
    @Query("SELECT e FROM Employee e WHERE e.id NOT IN " +
           "(SELECT DISTINCT s.employee.id FROM Shift s WHERE s.startTime >= :startDate AND s.endTime <= :endDate)")
    List<Employee> findEmployeesWithoutShiftsInRange(
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find employees with overlapping vacation requests
     * @param startDate The start date to check for overlaps
     * @param endDate The end date to check for overlaps
     * @return List of employees with vacation requests that overlap with the specified date range
     */
    @Query("SELECT e FROM Employee e WHERE EXISTS " +
           "(SELECT v FROM VacationRequest v WHERE v.employee = e AND v.status = 'APPROVED' " +
           "AND ((v.startDate <= :endDate AND v.endDate >= :startDate)))")
    List<Employee> findEmployeesWithOverlappingVacations(
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
}
