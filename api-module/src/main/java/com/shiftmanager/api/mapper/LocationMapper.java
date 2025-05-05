package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.LocationDTO;
import com.shiftmanager.api.model.Location;
import org.mapstruct.*;

/**
 * Mapper for the Location entity and its DTO
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class LocationMapper implements EntityMapper<LocationDTO, Location> {
    
    @Override
    @Mapping(target = "employeeCount", ignore = true)
    public abstract LocationDTO toDto(Location location);
    
    @Override
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "shifts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Location toEntity(LocationDTO locationDTO);
    
    @Override
    @Mapping(target = "employees", ignore = true)
    @Mapping(target = "shifts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Location updateEntityFromDto(LocationDTO locationDTO, @MappingTarget Location location);
}