package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for VacationRequest entity
 */
@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
    
    /**
     * Find vacation requests by employee
     * @param employee Employee
     * @return List of vacation requests
     */
    List<VacationRequest> findByEmployee(Employee employee);
    
    /**
     * Find vacation requests by employee and status
     * @param employee Employee
     * @param status Status
     * @return List of vacation requests
     */
    List<VacationRequest> findByEmployeeAndStatus(Employee employee, String status);
    
    /**
     * Find vacation requests by employee's manager and status
     * @param manager Manager
     * @param status Status
     * @return List of vacation requests
     */
    List<VacationRequest> findByEmployeeManagerAndStatus(Employee manager, String status);
    
    /**
     * Find vacation requests in a date range (where either start or end date falls within the range)
     * @param startDate Start date of range
     * @param endDate End date of range
     * @return List of vacation requests
     */
    @Query("SELECT vr FROM VacationRequest vr WHERE " +
            "(vr.startDate BETWEEN :startDate AND :endDate) OR " +
            "(vr.endDate BETWEEN :startDate AND :endDate) OR " +
            "(:startDate BETWEEN vr.startDate AND vr.endDate)")
    List<VacationRequest> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find approved vacation requests that overlap with a date range for a specific employee
     * @param employee Employee
     * @param startDate Start date of range
     * @param endDate End date of range
     * @return List of overlapping approved vacation requests
     */
    @Query("SELECT vr FROM VacationRequest vr WHERE vr.employee = :employee AND vr.status = 'APPROVED' AND " +
            "((vr.startDate BETWEEN :startDate AND :endDate) OR " +
            "(vr.endDate BETWEEN :startDate AND :endDate) OR " +
            "(:startDate BETWEEN vr.startDate AND vr.endDate) OR " +
            "(:endDate BETWEEN vr.startDate AND vr.endDate))")
    List<VacationRequest> findOverlappingApprovedRequests(Employee employee, LocalDate startDate, LocalDate endDate);
}