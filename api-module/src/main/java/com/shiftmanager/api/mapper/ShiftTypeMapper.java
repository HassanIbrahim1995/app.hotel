package com.shiftmanager.api.mapper;

import org.mapstruct.*;

import com.shiftmanager.api.dto.ShiftTypeDTO;
import com.shiftmanager.api.model.ShiftType;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.shiftmanager.api.dto.ShiftTypeDTO;
import com.shiftmanager.api.model.ShiftType;

import java.time.LocalTime;

@Mapper(componentModel = "spring")
public interface ShiftTypeMapper {

    ShiftTypeDTO toDTO(ShiftType shiftType);

    @Mapping(target = "startTime", source = "defaultStartTime") // Fixed: map from defaultStartTime
    @Mapping(target = "endTime", source = "defaultEndTime") // Fixed: map from defaultEndTime
    ShiftType toEntity(ShiftTypeDTO shiftTypeDTO);

    @Mapping(target = "startTime", source = "defaultStartTime") // Fixed: map from defaultStartTime
    @Mapping(target = "endTime", source = "defaultEndTime") // Fixed: map from defaultEndTime
    void updateEntityFromDTO(ShiftTypeDTO shiftTypeDTO, @MappingTarget ShiftType shiftType);
}