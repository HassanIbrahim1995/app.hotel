package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.CalendarDTO;
import com.shiftmanager.api.model.Calendar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    @Mapping(target = "entries", ignore = true)
    Calendar toEntity(CalendarDTO dto);
}