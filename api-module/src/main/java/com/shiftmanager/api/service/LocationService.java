package com.shiftmanager.api.service;

import com.shiftmanager.api.dto.LocationDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing locations
 */
public interface LocationService {
    
    /**
     * Get all locations
     * @return List of location DTOs
     */
    List<LocationDTO> getAllLocations();
    
    /**
     * Get location by ID
     * @param id Location ID
     * @return Optional location DTO
     */
    Optional<LocationDTO> getLocationById(Long id);
    
    /**
     * Create a new location
     * @param locationDTO Location data
     * @return Created location DTO
     */
    LocationDTO createLocation(LocationDTO locationDTO);
    
    /**
     * Update an existing location
     * @param id Location ID
     * @param locationDTO Updated location data
     * @return Updated location DTO
     */
    LocationDTO updateLocation(Long id, LocationDTO locationDTO);
    
    /**
     * Delete a location
     * @param id Location ID
     */
    void deleteLocation(Long id);
    
    /**
     * Get employees assigned to a location
     * @param locationId Location ID
     * @return Count of employees at the location
     */
    int getEmployeeCountByLocation(Long locationId);
    
    /**
     * Get active locations
     * @return List of active location DTOs
     */
    List<LocationDTO> getActiveLocations();
}