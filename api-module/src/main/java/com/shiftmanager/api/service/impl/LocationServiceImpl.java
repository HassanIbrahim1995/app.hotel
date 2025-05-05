package com.shiftmanager.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shiftmanager.api.dto.LocationDTO;
import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.mapper.LocationMapper;
import com.shiftmanager.api.model.Location;
import com.shiftmanager.api.repository.LocationRepository;
import com.shiftmanager.api.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(locationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LocationDTO getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        return locationMapper.toDTO(location);
    }

    @Override
    @Transactional
    public LocationDTO createLocation(LocationDTO locationDTO) {
        // Check if location with same name already exists
        locationRepository.findByName(locationDTO.getName())
                .ifPresent(l -> {
                    throw new IllegalArgumentException("Location with name " + locationDTO.getName() + " already exists");
                });
        
        Location location = locationMapper.toEntity(locationDTO);
        Location savedLocation = locationRepository.save(location);
        return locationMapper.toDTO(savedLocation);
    }

    @Override
    @Transactional
    public LocationDTO updateLocation(Long id, LocationDTO locationDTO) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        
        // Check name uniqueness if changed
        if (!existingLocation.getName().equals(locationDTO.getName())) {
            locationRepository.findByName(locationDTO.getName())
                    .ifPresent(l -> {
                        throw new IllegalArgumentException("Location with name " + locationDTO.getName() + " already exists");
                    });
        }
        
        locationMapper.updateEntityFromDTO(locationDTO, existingLocation);
        Location updatedLocation = locationRepository.save(existingLocation);
        return locationMapper.toDTO(updatedLocation);
    }

    @Override
    @Transactional
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Location not found with id: " + id);
        }
        locationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDTO> getActiveLocations() {
        return locationRepository.findByActiveTrue()
                .stream()
                .map(locationMapper::toDTO)
                .collect(Collectors.toList());
    }
}
