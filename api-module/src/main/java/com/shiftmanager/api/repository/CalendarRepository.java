package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.Calendar;
import com.shiftmanager.api.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Calendar entity
 */
@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    
    /**
     * Find calendar by employee
     * @param employee Employee
     * @return Optional calendar
     */
    Optional<Calendar> findByEmployee(Employee employee);
    
    /**
     * Find calendar by employee ID
     * @param employeeId Employee ID
     * @return Optional calendar
     */
    Optional<Calendar> findByEmployeeId(Long employeeId);
    
    /**
     * Find calendars by year and month
     * @param year Year
     * @param month Month
     * @return List of calendars
     */
    List<Calendar> findByYearAndMonth(int year, int month);
    
    /**
     * Find calendars for a manager's subordinates
     * @param managerId Manager ID
     * @return List of calendars
     */
    @Query("SELECT c FROM Calendar c WHERE c.employee.manager.id = :managerId")
    List<Calendar> findByManagerId(@Param("managerId") Long managerId);
    
    /**
     * Find calendars for a location
     * @param locationId Location ID
     * @return List of calendars
     */
    @Query("SELECT c FROM Calendar c WHERE c.employee.location.id = :locationId")
    List<Calendar> findByLocationId(@Param("locationId") Long locationId);
}