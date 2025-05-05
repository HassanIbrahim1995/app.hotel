package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.EmployeeShiftDTO;
import com.shiftmanager.api.dto.ShiftDTO;
import com.shiftmanager.api.model.EmployeeShift;
import com.shiftmanager.api.model.Shift;
import com.shiftmanager.api.repository.LocationRepository;
import com.shiftmanager.api.repository.ShiftTypeRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the Shift entity and its DTO
 */
@Mapper(componentModel = "spring", uses = {EmployeeShiftMapper.class})
public abstract class ShiftMapper implements EntityMapper<ShiftDTO, Shift> {
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private ShiftTypeRepository shiftTypeRepository;
    
    @Override
    @Mapping(target = "shiftTypeId", source = "shiftType.id")
    @Mapping(target = "shiftTypeName", source = "shiftType.name")
    @Mapping(target = "startTime", source = "shiftType.startTime")
    @Mapping(target = "endTime", source = "shiftType.endTime")
    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "locationName", source = "location.name")
    @Mapping(target = "durationHours", expression = "java(shift.getShiftType() != null ? shift.getShiftType().getDurationHours() : null)")
    @Mapping(target = "isOvertime", expression = "java(shift.getShiftType() != null ? shift.getShiftType().getDurationHours() > 8.0 : false)")
    public abstract ShiftDTO toDto(Shift shift);
    
    @Override
    @Mapping(target = "shiftType", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "employeeShifts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Shift toEntity(ShiftDTO shiftDTO);
    
    @Override
    @Mapping(target = "shiftType", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "employeeShifts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Shift updateEntityFromDto(ShiftDTO shiftDTO, @MappingTarget Shift shift);
    
    /**
     * Set the location and shift type relations
     * @param shiftDTO The DTO
     * @param shift The entity
     */
    @AfterMapping
    protected void setRelations(ShiftDTO shiftDTO, @MappingTarget Shift shift) {
        // Set location
        if (shiftDTO.getLocationId() != null) {
            locationRepository.findById(shiftDTO.getLocationId())
                    .ifPresent(shift::setLocation);
        }
        
        // Set shift type
        if (shiftDTO.getShiftTypeId() != null) {
            shiftTypeRepository.findById(shiftDTO.getShiftTypeId())
                    .ifPresent(shift::setShiftType);
        }
    }
    
    /**
     * Map employee shifts to DTOs
     * @param employeeShifts Employee shifts from entity
     * @return Set of employee shift DTOs
     */
    protected Set<EmployeeShiftDTO> mapEmployeeShifts(Set<EmployeeShift> employeeShifts) {
        if (employeeShifts == null) {
            return new HashSet<>();
        }
        
        return employeeShifts.stream()
                .map(es -> EmployeeShiftDTO.builder()
                        .id(es.getId())
                        .employeeId(es.getEmployee().getId())
                        .employeeName(es.getEmployee().getFirstName() + " " + es.getEmployee().getLastName())
                        .shiftId(es.getShift().getId())
                        .status(es.getStatus())
                        .build())
                .collect(Collectors.toSet());
    }
}