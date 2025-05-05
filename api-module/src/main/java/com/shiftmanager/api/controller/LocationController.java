package com.shiftmanager.api.controller;

import com.shiftmanager.api.dto.LocationDTO;
import com.shiftmanager.api.mapper.LocationMapper;
import com.shiftmanager.api.model.Location;
import com.shiftmanager.api.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for location operations
 */
@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationMapper locationMapper;

    /**
     * Get all locations
     * @return List of locations
     */
    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        List<LocationDTO> locationDTOs = locations.stream()
                .map(locationMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(locationDTOs);
    }

    /**
     * Get location by ID
     * @param id Location ID
     * @return Location
     */
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        Location location = locationService.getLocationById(id);
        LocationDTO locationDTO = locationMapper.toDto(location);

        return ResponseEntity.ok(locationDTO);
    }

    /**
     * Create location (admin only)
     * @param locationDTO Location data
     * @return Created location
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<LocationDTO> createLocation(@Valid @RequestBody LocationDTO locationDTO) {
        Location location = locationMapper.toEntity(locationDTO);
        Location createdLocation = locationService.createLocation(location);
        LocationDTO createdLocationDTO = locationMapper.toDto(createdLocation);

        return ResponseEntity.ok(createdLocationDTO);
    }

    /**
     * Update location (admin only)
     * @param id Location ID
     * @param locationDTO Location data
     * @return Updated location
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<LocationDTO> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationDTO locationDTO) {

        Location location = locationMapper.toEntity(locationDTO);
        Location updatedLocation = locationService.updateLocation(id, location);
        LocationDTO updatedLocationDTO = locationMapper.toDto(updatedLocation);

        return ResponseEntity.ok(updatedLocationDTO);
    }

    /**
     * Delete location (admin only)
     * @param id Location ID
     * @return Delete status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return ResponseEntity.ok(response);
    }
}