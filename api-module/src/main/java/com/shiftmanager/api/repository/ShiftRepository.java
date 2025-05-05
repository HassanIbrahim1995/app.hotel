package com.shiftmanager.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shiftmanager.api.model.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    
    @Query("SELECT s FROM Shift s WHERE s.shiftDate BETWEEN :startDate AND :endDate ORDER BY s.shiftDate, s.startTime")
    List<Shift> findByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT s FROM Shift s WHERE s.location.id = :locationId AND s.shiftDate BETWEEN :startDate AND :endDate ORDER BY s.shiftDate, s.startTime")
    List<Shift> findByLocationAndDateRange(
        @Param("locationId") Long locationId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT s FROM Shift s WHERE s.shiftType.id = :shiftTypeId AND s.shiftDate BETWEEN :startDate AND :endDate ORDER BY s.shiftDate, s.startTime")
    List<Shift> findByShiftTypeAndDateRange(
        @Param("shiftTypeId") Long shiftTypeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    /**
     * Find shifts for employees in a manager's team within a date range
     * @param managerId Manager ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of shifts
     */
    @Query("SELECT DISTINCT s FROM Shift s JOIN EmployeeShift es ON es.shift = s WHERE es.employee.manager.id = :managerId AND s.shiftDate BETWEEN :startDate AND :endDate ORDER BY s.shiftDate, s.startTime")
    List<Shift> findByEmployeesInManagerTeamAndDateRange(
        @Param("managerId") Long managerId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
