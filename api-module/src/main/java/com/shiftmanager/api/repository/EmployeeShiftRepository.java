package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.EmployeeShift;
import com.shiftmanager.api.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for EmployeeShift entity
 */
@Repository
public interface EmployeeShiftRepository extends JpaRepository<EmployeeShift, Long> {
    
    /**
     * Find employee shift by employee and shift
     * @param employee Employee
     * @param shift Shift
     * @return Optional EmployeeShift
     */
    Optional<EmployeeShift> findByEmployeeAndShift(Employee employee, Shift shift);
    
    /**
     * Find employee shifts by employee
     * @param employee Employee
     * @return List of employee shifts
     */
    List<EmployeeShift> findByEmployee(Employee employee);
    
    /**
     * Find employee shifts by shift
     * @param shift Shift
     * @return List of employee shifts
     */
    List<EmployeeShift> findByShift(Shift shift);
    
    /**
     * Find employee shifts by employee and shift date between
     * @param employee Employee
     * @param startDate Start date
     * @param endDate End date
     * @return List of employee shifts
     */
    @Query("SELECT es FROM EmployeeShift es WHERE es.employee = ?1 AND es.shift.shiftDate BETWEEN ?2 AND ?3")
    List<EmployeeShift> findByEmployeeAndShiftDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);
    
    /**
     * Find employee shifts by status
     * @param status Status
     * @return List of employee shifts
     */
    List<EmployeeShift> findByStatus(String status);
}