package com.shiftmanager.core.repository;

import com.shiftmanager.core.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Location entity operations
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    /**
     * Find a location by name
     * @param name The name to search for
     * @return Optional containing the location if found
     */
    Optional<Location> findByName(String name);
    
    /**
     * Find locations by city
     * @param city The city to search for
     * @return List of locations in the specified city
     */
    List<Location> findByAddressCity(String city);
    
    /**
     * Find locations by state
     * @param state The state to search for
     * @return List of locations in the specified state
     */
    List<Location> findByAddressState(String state);
    
    /**
     * Find locations by zip code
     * @param zipCode The zip code to search for
     * @return List of locations with the specified zip code
     */
    List<Location> findByAddressZipCode(String zipCode);
    
    /**
     * Count employees at a specific location
     * @param locationId The location ID
     * @return The count of employees at this location
     */
    @Query("SELECT COUNT(e) FROM Employee e WHERE e.location.id = :locationId")
    Long countEmployeesAtLocation(@Param("locationId") Long locationId);
    
    /**
     * Count managers at a specific location
     * @param locationId The location ID
     * @return The count of managers at this location
     */
    @Query("SELECT COUNT(m) FROM Manager m WHERE m.location.id = :locationId")
    Long countManagersAtLocation(@Param("locationId") Long locationId);
}
