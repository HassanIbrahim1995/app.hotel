package com.shiftmanager.core.repository;

import com.shiftmanager.core.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Schedule entity operations
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    /**
     * Find a schedule by employee
     * @param employeeId The employee ID
     * @return Optional containing the schedule if found
     */
    Optional<Schedule> findByEmployeeId(Long employeeId);
    
    /**
     * Find a schedule by manager
     * @param managerId The manager ID
     * @return Optional containing the schedule if found
     */
    Optional<Schedule> findByManagerId(Long managerId);
    
    /**
     * Find schedules by date range
     * @param startDate The minimum start date
     * @param endDate The maximum end date
     * @return List of schedules within the specified date range
     */
    List<Schedule> findByStartDateGreaterThanEqualAndEndDateLessThanEqual(
            LocalDate startDate, LocalDate endDate);
    
    /**
     * Find schedules that overlap with a date range
     * @param startDate The start date to check for overlaps
     * @param endDate The end date to check for overlaps
     * @return List of schedules that overlap with the specified date range
     */
    List<Schedule> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDate endDate, LocalDate startDate);
    
    /**
     * Find schedules by employee and date range
     * @param employeeId The employee ID
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return List of schedules for the specified employee within the date range
     */
    List<Schedule> findByEmployeeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long employeeId, LocalDate endDate, LocalDate startDate);
}
