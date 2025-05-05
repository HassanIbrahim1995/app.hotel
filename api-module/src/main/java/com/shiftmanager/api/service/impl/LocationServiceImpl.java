package com.shiftmanager.api.service.impl;

import com.shiftmanager.api.dto.LocationDTO;
import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.mapper.LocationMapper;
import com.shiftmanager.api.model.Location;
import com.shiftmanager.api.repository.EmployeeRepository;
import com.shiftmanager.api.repository.LocationRepository;
import com.shiftmanager.api.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the LocationService interface
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {
    
    private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private LocationMapper locationMapper;
    
    @Override
    public List<LocationDTO> getAllLocations() {
        logger.debug("Getting all locations");
        return locationRepository.findAll().stream()
                .map(location -> {
                    LocationDTO dto = locationMapper.toDto(location);
                    dto.setEmployeeCount(employeeRepository.countByLocation(location));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<LocationDTO> getLocationById(Long id) {
        logger.debug("Getting location with ID: {}", id);
        return locationRepository.findById(id)
                .map(location -> {
                    LocationDTO dto = locationMapper.toDto(location);
                    dto.setEmployeeCount(employeeRepository.countByLocation(location));
                    return dto;
                });
    }
    
    @Override
    public LocationDTO createLocation(LocationDTO locationDTO) {
        logger.debug("Creating new location: {}", locationDTO);
        
        Location location = locationMapper.toEntity(locationDTO);
        location.setActive(true); // Default to active
        
        Location savedLocation = locationRepository.save(location);
        LocationDTO dto = locationMapper.toDto(savedLocation);
        dto.setEmployeeCount(0); // New location has no employees
        
        return dto;
    }
    
    @Override
    public LocationDTO updateLocation(Long id, LocationDTO locationDTO) {
        logger.debug("Updating location with ID: {}", id);
        
        return locationRepository.findById(id)
                .map(location -> {
                    locationMapper.updateEntityFromDto(locationDTO, location);
                    Location updatedLocation = locationRepository.save(location);
                    
                    LocationDTO dto = locationMapper.toDto(updatedLocation);
                    dto.setEmployeeCount(employeeRepository.countByLocation(updatedLocation));
                    
                    return dto;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + id));
    }
    
    @Override
    public void deleteLocation(Long id) {
        logger.debug("Deleting location with ID: {}", id);
        
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + id));
        
        // Check if any employees are assigned to this location
        int employeeCount = employeeRepository.countByLocation(location);
        if (employeeCount > 0) {
            throw new IllegalStateException("Cannot delete location with assigned employees. " +
                    "There are " + employeeCount + " employees at this location.");
        }
        
        locationRepository.delete(location);
    }
    
    @Override
    public int getEmployeeCountByLocation(Long locationId) {
        logger.debug("Getting employee count for location ID: {}", locationId);
        
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + locationId));
        
        return employeeRepository.countByLocation(location);
    }
    
    @Override
    public List<LocationDTO> getActiveLocations() {
        logger.debug("Getting active locations");
        
        return locationRepository.findByActiveTrue().stream()
                .map(location -> {
                    LocationDTO dto = locationMapper.toDto(location);
                    dto.setEmployeeCount(employeeRepository.countByLocation(location));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}