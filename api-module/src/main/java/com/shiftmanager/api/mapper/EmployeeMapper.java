package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.EmployeeDTO;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.repository.LocationRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the Employee entity and its DTO
 */
@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public abstract class EmployeeMapper implements EntityMapper<EmployeeDTO, Employee> {
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Override
    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "locationName", source = "location.name")
    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "upcomingShifts", ignore = true)
    @Mapping(target = "roleId", ignore = true) // No role in the Employee entity
    @Mapping(target = "roleName", ignore = true) // No role in the Employee entity
    @Mapping(target = "isActive", expression = "java(employee.isActive())")
    @Mapping(target = "terminationDate", ignore = true) // No terminationDate in the entity
    public abstract EmployeeDTO toDto(Employee employee);
    
    @Override
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "shifts", ignore = true)
    @Mapping(target = "subordinates", ignore = true)
    @Mapping(target = "vacationRequests", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    public abstract Employee toEntity(EmployeeDTO employeeDTO);
    
    @Override
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "shifts", ignore = true)
    @Mapping(target = "subordinates", ignore = true)
    @Mapping(target = "vacationRequests", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    public abstract Employee updateEntityFromDto(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
    
    /**
     * Set the location and manager relations based on IDs from the DTO
     * @param dto The source DTO
     * @param employee The target entity
     */
    @AfterMapping
    protected void setRelations(EmployeeDTO dto, @MappingTarget Employee employee) {
        // Set location
        if (dto.getLocationId() != null) {
            locationRepository.findById(dto.getLocationId())
                    .ifPresent(employee::setLocation);
        }
    }
}
