package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Location entity
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    /**
     * Find location by name
     * @param name Location name
     * @return List of locations
     */
    List<Location> findByName(String name);
    
    /**
     * Find location by name containing
     * @param nameFragment Name fragment
     * @return List of locations
     */
    List<Location> findByNameContainingIgnoreCase(String nameFragment);
    
    /**
     * Find active locations
     * @return List of active locations
     */
    List<Location> findByActiveTrue();
    
    /**
     * Find inactive locations
     * @return List of inactive locations
     */
    List<Location> findByActiveFalse();
}