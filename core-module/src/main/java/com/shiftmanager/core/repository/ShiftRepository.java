package com.shiftmanager.core.repository;

import com.shiftmanager.core.domain.Shift;
import com.shiftmanager.core.domain.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Shift entity operations
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    
    /**
     * Find shifts by employee
     * @param employeeId The employee ID
     * @return List of shifts for the specified employee
     */
    List<Shift> findByEmployeeId(Long employeeId);
    
    /**
     * Find shifts by schedule
     * @param scheduleId The schedule ID
     * @return List of shifts for the specified schedule
     */
    List<Shift> findByScheduleId(Long scheduleId);
    
    /**
     * Find shifts by employee and date range
     * @param employeeId The employee ID
     * @param startTime The start of the date range
     * @param endTime The end of the date range
     * @return List of shifts for the specified employee within the date range
     */
    List<Shift> findByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
            Long employeeId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find shifts by shift type
     * @param shiftType The shift type
     * @return List of shifts of the specified type
     */
    List<Shift> findByShiftType(ShiftType shiftType);
    
    /**
     * Check for overlapping shifts for an employee
     * @param employeeId The employee ID
     * @param startTime The start time to check
     * @param endTime The end time to check
     * @param shiftId The ID of the shift to exclude from the check (optional)
     * @return List of overlapping shifts
     */
    @Query("SELECT s FROM Shift s WHERE s.employee.id = :employeeId " +
           "AND (:shiftId IS NULL OR s.id != :shiftId) " +
           "AND ((s.startTime <= :endTime AND s.endTime >= :startTime))")
    List<Shift> findOverlappingShifts(
            @Param("employeeId") Long employeeId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("shiftId") Long shiftId);
    
    /**
     * Calculate total hours worked by an employee in a date range
     * @param employeeId The employee ID
     * @param startTime The start of the date range
     * @param endTime The end of the date range
     * @return The total hours worked as a double
     */
    @Query("SELECT SUM(TIMESTAMPDIFF(SECOND, s.startTime, s.endTime))/3600.0 FROM Shift s " +
           "WHERE s.employee.id = :employeeId " +
           "AND s.startTime >= :startTime AND s.endTime <= :endTime")
    Double calculateTotalHoursInRange(
            @Param("employeeId") Long employeeId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
