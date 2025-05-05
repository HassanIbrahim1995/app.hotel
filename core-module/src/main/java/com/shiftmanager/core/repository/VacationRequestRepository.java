package com.shiftmanager.core.repository;

import com.shiftmanager.core.domain.VacationRequest;
import com.shiftmanager.core.domain.VacationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for VacationRequest entity operations
 */
@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
    
    /**
     * Find vacation requests by employee
     * @param employeeId The employee ID
     * @return List of vacation requests for the specified employee
     */
    List<VacationRequest> findByEmployeeId(Long employeeId);
    
    /**
     * Find vacation requests by manager
     * @param managerId The manager ID
     * @return List of vacation requests handled by the specified manager
     */
    List<VacationRequest> findByManagerId(Long managerId);
    
    /**
     * Find vacation requests by status
     * @param status The vacation request status
     * @return List of vacation requests with the specified status
     */
    List<VacationRequest> findByStatus(VacationStatus status);
    
    /**
     * Find vacation requests by employee and status
     * @param employeeId The employee ID
     * @param status The vacation request status
     * @return List of vacation requests for the specified employee with the specified status
     */
    List<VacationRequest> findByEmployeeIdAndStatus(Long employeeId, VacationStatus status);
    
    /**
     * Find vacation requests by date range
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return List of vacation requests within the specified date range
     */
    @Query("SELECT v FROM VacationRequest v WHERE " +
           "(v.startDate <= :endDate AND v.endDate >= :startDate)")
    List<VacationRequest> findByDateRange(
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find vacation requests by employee and date range
     * @param employeeId The employee ID
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return List of vacation requests for the specified employee within the date range
     */
    @Query("SELECT v FROM VacationRequest v WHERE v.employee.id = :employeeId AND " +
           "(v.startDate <= :endDate AND v.endDate >= :startDate)")
    List<VacationRequest> findByEmployeeIdAndDateRange(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
    
    /**
     * Find pending vacation requests by manager's department
     * @param managerId The manager ID
     * @return List of pending vacation requests from employees in the manager's department
     */
    @Query("SELECT v FROM VacationRequest v WHERE v.status = 'PENDING' AND " +
           "v.employee.department = (SELECT m.department FROM Manager m WHERE m.id = :managerId)")
    List<VacationRequest> findPendingRequestsByManagerDepartment(@Param("managerId") Long managerId);
}
