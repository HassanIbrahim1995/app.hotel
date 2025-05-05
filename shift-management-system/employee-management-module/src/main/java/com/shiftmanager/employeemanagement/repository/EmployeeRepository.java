package com.shiftmanager.employeemanagement.repository;

import com.shiftmanager.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Employee entity
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Find an employee by their unique employee ID
     * @param employeeId The employee ID to search for
     * @return Optional containing the employee if found
     */
    Optional<Employee> findByEmployeeId(String employeeId);

    /**
     * Find employees by their department
     * @param department The department to search for
     * @return List of employees in the specified department
     */
    List<Employee> findByDepartment(String department);

    /**
     * Find employees by their manager
     * @param managerId The ID of the manager
     * @return List of employees under the specified manager
     */
    @Query("SELECT e FROM Employee e WHERE e.manager.id = :managerId")
    List<Employee> findByManagerId(Long managerId);

    /**
     * Find employees by their job title
     * @param jobTitle The job title to search for
     * @return List of employees with the specified job title
     */
    List<Employee> findByJobTitle(String jobTitle);

    /**
     * Find employees by their employment type
     * @param employmentType The employment type to search for
     * @return List of employees with the specified employment type
     */
    List<Employee> findByEmploymentType(Employee.EmploymentType employmentType);

    /**
     * Find employees by their employment status
     * @param employmentStatus The employment status to search for
     * @return List of employees with the specified employment status
     */
    List<Employee> findByEmploymentStatus(Employee.EmploymentStatus employmentStatus);

    /**
     * Find employees hired after a specific date
     * @param date The date to compare against
     * @return List of employees hired after the specified date
     */
    List<Employee> findByHireDateAfter(LocalDate date);

    /**
     * Find employees by their email address
     * @param email The email address to search for
     * @return Optional containing the employee if found
     */
    Optional<Employee> findByEmail(String email);
}
