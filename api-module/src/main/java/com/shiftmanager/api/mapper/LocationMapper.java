package com.shiftmanager.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.shiftmanager.api.dto.LocationDTO;
import com.shiftmanager.api.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    
    LocationDTO toDTO(Location location);
    
    Location toEntity(LocationDTO locationDTO);
    
    void updateEntityFromDTO(LocationDTO locationDTO, @MappingTarget Location location);
}
