package com.shiftmanager.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.shiftmanager.api.dto.ShiftTypeDTO;
import com.shiftmanager.api.model.ShiftType;

@Mapper(componentModel = "spring")
public interface ShiftTypeMapper {
    
    ShiftTypeDTO toDTO(ShiftType shiftType);
    
    ShiftType toEntity(ShiftTypeDTO shiftTypeDTO);
    
    void updateEntityFromDTO(ShiftTypeDTO shiftTypeDTO, @MappingTarget ShiftType shiftType);
}
