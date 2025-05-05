package com.shiftmanager.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.shiftmanager.api.dto.ShiftDTO;
import com.shiftmanager.api.model.Shift;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, ShiftTypeMapper.class})
public interface ShiftMapper {
    
    ShiftDTO toDto(Shift shift);
    
    // Alias for backward compatibility
    default ShiftDTO toDTO(Shift shift) {
        return toDto(shift);
    }
    
    @Mapping(target = "createdById", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Shift toEntity(ShiftDTO shiftDTO);
    
    @Mapping(target = "createdById", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(ShiftDTO shiftDTO, @MappingTarget Shift shift);
}
