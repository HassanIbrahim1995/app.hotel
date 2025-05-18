package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.CalendarEntryDTO;
import com.shiftmanager.api.model.CalendarEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.shiftmanager.api.dto.CalendarEntryDTO;
import com.shiftmanager.api.model.CalendarEntry;
import com.shiftmanager.api.model.Calendar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
    @Mapping(target = "version", constant = "0L")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    CalendarEntry toEntity(CalendarEntryDTO dto);

    /**
     * Update entity from DTO
     * @param dto DTO with updated values
     * @param entity Entity to update
     * @return Updated entity
     */
    @Mapping(target = "calendar", ignore = true)
    @Mapping(target = "version", expression = "java(entity.getVersion() + 1)")
    @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    CalendarEntry updateEntityFromDto(CalendarEntryDTO dto, @MappingTarget CalendarEntry entity);

    /**
     * Map calendarId to Calendar
     * @param calendarId the calendar ID
     * @return the Calendar entity
     */
    default Calendar mapCalendar(Long calendarId) {
        if (calendarId == null) {
            return null;
        }
        Calendar calendar = new Calendar();
        calendar.setId(calendarId);
        return calendar;
    }
}