package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.CalendarEntryDTO;
import com.shiftmanager.api.model.Calendar;
import com.shiftmanager.api.model.CalendarEntry;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T01:56:19+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class CalendarEntryMapperImpl implements CalendarEntryMapper {

    @Override
    public CalendarEntryDTO toDto(CalendarEntry entity) {
        if ( entity == null ) {
            return null;
        }

        CalendarEntryDTO calendarEntryDTO = new CalendarEntryDTO();

        calendarEntryDTO.setCalendarId( entityCalendarId( entity ) );
        calendarEntryDTO.setId( entity.getId() );
        calendarEntryDTO.setEntryDate( entity.getEntryDate() );
        calendarEntryDTO.setStartTime( entity.getStartTime() );
        calendarEntryDTO.setEndTime( entity.getEndTime() );
        calendarEntryDTO.setEntryType( entity.getEntryType() );
        calendarEntryDTO.setTitle( entity.getTitle() );
        calendarEntryDTO.setDescription( entity.getDescription() );
        calendarEntryDTO.setAllDay( entity.isAllDay() );
        calendarEntryDTO.setReferenceId( entity.getReferenceId() );
        calendarEntryDTO.setColor( entity.getColor() );

        return calendarEntryDTO;
    }

    @Override
    public CalendarEntry toEntity(CalendarEntryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CalendarEntry calendarEntry = new CalendarEntry();

        calendarEntry.setId( dto.getId() );
        calendarEntry.setEntryDate( dto.getEntryDate() );
        calendarEntry.setStartTime( dto.getStartTime() );
        calendarEntry.setEndTime( dto.getEndTime() );
        calendarEntry.setEntryType( dto.getEntryType() );
        calendarEntry.setTitle( dto.getTitle() );
        calendarEntry.setDescription( dto.getDescription() );
        calendarEntry.setAllDay( dto.isAllDay() );
        calendarEntry.setReferenceId( dto.getReferenceId() );
        calendarEntry.setColor( dto.getColor() );

        calendarEntry.setVersion( (long) 0L );
        calendarEntry.setCreatedAt( java.time.LocalDateTime.now() );
        calendarEntry.setUpdatedAt( java.time.LocalDateTime.now() );

        return calendarEntry;
    }

    @Override
    public CalendarEntry updateEntityFromDto(CalendarEntryDTO dto, CalendarEntry entity) {
        if ( dto == null ) {
            return entity;
        }

        entity.setId( dto.getId() );
        entity.setEntryDate( dto.getEntryDate() );
        entity.setStartTime( dto.getStartTime() );
        entity.setEndTime( dto.getEndTime() );
        entity.setEntryType( dto.getEntryType() );
        entity.setTitle( dto.getTitle() );
        entity.setDescription( dto.getDescription() );
        entity.setAllDay( dto.isAllDay() );
        entity.setReferenceId( dto.getReferenceId() );
        entity.setColor( dto.getColor() );

        entity.setVersion( entity.getVersion() + 1 );
        entity.setCreatedAt( entity.getCreatedAt() );
        entity.setUpdatedAt( java.time.LocalDateTime.now() );

        return entity;
    }

    private Long entityCalendarId(CalendarEntry calendarEntry) {
        if ( calendarEntry == null ) {
            return null;
        }
        Calendar calendar = calendarEntry.getCalendar();
        if ( calendar == null ) {
            return null;
        }
        Long id = calendar.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
