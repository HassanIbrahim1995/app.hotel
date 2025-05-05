package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for ShiftType entity
 */
@Repository
public interface ShiftTypeRepository extends JpaRepository<ShiftType, Long> {
    
    /**
     * Find a shift type by name
     * @param name The name to search for
     * @return Optional shift type
     */
    Optional<ShiftType> findByNameIgnoreCase(String name);
    
    /**
     * Find shift types by start time
     * @param startTime The start time to search for
     * @return List of shift types
     */
    List<ShiftType> findByStartTime(LocalTime startTime);
    
    /**
     * Find shift types by end time
     * @param endTime The end time to search for
     * @return List of shift types
     */
    List<ShiftType> findByEndTime(LocalTime endTime);
    
    /**
     * Find shift types with duration greater than or equal to a minimum
     * @return List of shift types
     */
    @Query("SELECT st FROM ShiftType st WHERE " +
           "(CASE WHEN st.endTime < st.startTime THEN 24 + (st.endTime.hour + st.endTime.minute/60.0) ELSE (st.endTime.hour + st.endTime.minute/60.0) END) - " +
           "(st.startTime.hour + st.startTime.minute/60.0) >= :minHours")
    List<ShiftType> findByDurationGreaterThanEqual(double minHours);
    
    /**
     * Check if a shift type exists with the given name
     * @param name The name to check
     * @return true if exists
     */
    boolean existsByNameIgnoreCase(String name);
}
