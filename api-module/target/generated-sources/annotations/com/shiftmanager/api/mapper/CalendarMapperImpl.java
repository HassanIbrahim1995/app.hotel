package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.CalendarDTO;
import com.shiftmanager.api.model.Calendar;
import com.shiftmanager.api.model.Employee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T01:56:19+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class CalendarMapperImpl implements CalendarMapper {

    @Override
    public CalendarDTO toDto(Calendar entity) {
        if ( entity == null ) {
            return null;
        }

        CalendarDTO calendarDTO = new CalendarDTO();

        calendarDTO.setEmployeeId( entityEmployeeId( entity ) );
        calendarDTO.setId( entity.getId() );
        calendarDTO.setYear( entity.getYear() );
        calendarDTO.setMonth( entity.getMonth() );

        calendarDTO.setEmployeeName( entity.getEmployee().getFullName() );

        return calendarDTO;
    }

    @Override
    public Calendar toEntity(CalendarDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Calendar calendar = new Calendar();

        calendar.setId( dto.getId() );
        calendar.setYear( dto.getYear() );
        calendar.setMonth( dto.getMonth() );

        calendar.setEntries( new ArrayList<>() );
        calendar.setVersion( (long) 0L );
        calendar.setCreatedAt( java.time.LocalDateTime.now() );
        calendar.setUpdatedAt( java.time.LocalDateTime.now() );

        return calendar;
    }

    @Override
    public Calendar updateEntityFromDto(CalendarDTO dto, Calendar entity) {
        if ( dto == null ) {
            return entity;
        }

        entity.setId( dto.getId() );
        entity.setYear( dto.getYear() );
        entity.setMonth( dto.getMonth() );

        entity.setEntries( entity.getEntries() );
        entity.setVersion( entity.getVersion() + 1 );
        entity.setCreatedAt( entity.getCreatedAt() );
        entity.setUpdatedAt( java.time.LocalDateTime.now() );

        return entity;
    }

    private Long entityEmployeeId(Calendar calendar) {
        if ( calendar == null ) {
            return null;
        }
        Employee employee = calendar.getEmployee();
        if ( employee == null ) {
            return null;
        }
        Long id = employee.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
