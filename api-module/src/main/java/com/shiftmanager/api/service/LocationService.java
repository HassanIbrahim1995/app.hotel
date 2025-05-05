package com.shiftmanager.api.service;

import java.util.List;

import com.shiftmanager.api.dto.LocationDTO;

public interface LocationService {
    
    List<LocationDTO> getAllLocations();
    
    LocationDTO getLocationById(Long id);
    
    LocationDTO createLocation(LocationDTO locationDTO);
    
    LocationDTO updateLocation(Long id, LocationDTO locationDTO);
    
    void deleteLocation(Long id);
    
    List<LocationDTO> getActiveLocations();
}
