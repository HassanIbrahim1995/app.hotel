package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.CalendarEntryDTO;
import com.shiftmanager.api.model.CalendarEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for CalendarEntry entity and DTO
 */
@Mapper(componentModel = "spring")
public interface CalendarEntryMapper extends EntityMapper<CalendarEntryDTO, CalendarEntry> {

    /**
     * Map entity to DTO
     * @param entity CalendarEntry entity
     * @return CalendarEntryDTO
     */
    @Mapping(target = "calendarId", source = "calendar.id")
    CalendarEntryDTO toDto(CalendarEntry entity);

    /**
     * Map DTO to entity
     * @param dto CalendarEntryDTO
     * @return CalendarEntry entity
     */
    @Mapping(target = "calendar", ignore = true)
    CalendarEntry toEntity(CalendarEntryDTO dto);
}