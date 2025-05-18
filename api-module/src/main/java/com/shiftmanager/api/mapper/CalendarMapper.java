package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.CalendarDTO;
import com.shiftmanager.api.model.Calendar;
import com.shiftmanager.api.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for Calendar entity and DTO
 */
@Mapper(componentModel = "spring")
public interface CalendarMapper extends EntityMapper<CalendarDTO, Calendar> {

    /**
     * Map entity to DTO
     * @param entity Calendar entity
     * @return Calendar DTO
     */
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", expression = "java(entity.getEmployee().getFullName())")
    CalendarDTO toDto(Calendar entity);

    /**
     * Map DTO to entity
     * @param dto Calendar DTO
     * @return Calendar entity
     */
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "entries", expression = "java(new ArrayList<>())")
    @Mapping(target = "version", constant = "0L")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Calendar toEntity(CalendarDTO dto);

    /**
     * Update entity from DTO
     * @param dto DTO with updated values
     * @param entity Entity to update
     * @return Updated entity
     */
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "entries", expression = "java(entity.getEntries())")
    @Mapping(target = "version", expression = "java(entity.getVersion() + 1)")
    @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Calendar updateEntityFromDto(CalendarDTO dto, @MappingTarget Calendar entity);

    /**
     * Map employeeId to Employee
     * @param employeeId the employee ID
     * @return the Employee entity
     */
    default Employee mapEmployee(Long employeeId) {
        if (employeeId == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(employeeId);
        return employee;
    }
}
