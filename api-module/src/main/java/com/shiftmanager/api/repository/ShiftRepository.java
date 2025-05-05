package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.Location;
import com.shiftmanager.api.model.Shift;
import com.shiftmanager.api.model.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Shift entity
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    
    /**
     * Find shifts by date
     * @param shiftDate Shift date
     * @return List of shifts
     */
    List<Shift> findByShiftDate(LocalDate shiftDate);
    
    /**
     * Find shifts by date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of shifts
     */
    List<Shift> findByShiftDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find shifts by location
     * @param location Location
     * @return List of shifts
     */
    List<Shift> findByLocation(Location location);
    
    /**
     * Find shifts by shift type
     * @param shiftType Shift type
     * @return List of shifts
     */
    List<Shift> findByShiftType(ShiftType shiftType);
    
    /**
     * Find shifts by location and date range
     * @param location Location
     * @param startDate Start date
     * @param endDate End date
     * @return List of shifts
     */
    List<Shift> findByLocationAndShiftDateBetween(Location location, LocalDate startDate, LocalDate endDate);
}