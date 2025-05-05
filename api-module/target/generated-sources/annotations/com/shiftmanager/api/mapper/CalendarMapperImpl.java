package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.CalendarDTO;
import com.shiftmanager.api.model.Calendar;
import com.shiftmanager.api.model.Employee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T22:02:40+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (N/A)"
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

        return calendar;
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
