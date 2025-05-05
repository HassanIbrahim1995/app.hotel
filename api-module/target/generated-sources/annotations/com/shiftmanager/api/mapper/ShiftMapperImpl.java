package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.ShiftDTO;
import com.shiftmanager.api.model.Location;
import com.shiftmanager.api.model.Shift;
import com.shiftmanager.api.model.ShiftType;
import java.time.LocalTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T22:02:41+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (N/A)"
)
@Component
public class ShiftMapperImpl extends ShiftMapper {

    @Override
    public ShiftDTO toDto(Shift shift) {
        if ( shift == null ) {
            return null;
        }

        ShiftDTO.ShiftDTOBuilder shiftDTO = ShiftDTO.builder();

        shiftDTO.shiftTypeId( shiftShiftTypeId( shift ) );
        shiftDTO.shiftTypeName( shiftShiftTypeName( shift ) );
        shiftDTO.startTime( shiftShiftTypeStartTime( shift ) );
        shiftDTO.endTime( shiftShiftTypeEndTime( shift ) );
        shiftDTO.locationId( shiftLocationId( shift ) );
        shiftDTO.locationName( shiftLocationName( shift ) );
        shiftDTO.id( shift.getId() );
        shiftDTO.shiftDate( shift.getShiftDate() );
        shiftDTO.employeeShifts( mapEmployeeShifts( shift.getEmployeeShifts() ) );

        shiftDTO.durationHours( shift.getShiftType() != null ? shift.getShiftType().getDurationHours() : null );
        shiftDTO.isOvertime( shift.getShiftType() != null ? shift.getShiftType().getDurationHours() > 8.0 : false );

        return shiftDTO.build();
    }

    @Override
    public Shift toEntity(ShiftDTO shiftDTO) {
        if ( shiftDTO == null ) {
            return null;
        }

        Shift shift = new Shift();

        shift.setId( shiftDTO.getId() );
        shift.setShiftDate( shiftDTO.getShiftDate() );

        setRelations( shiftDTO, shift );

        return shift;
    }

    @Override
    public Shift updateEntityFromDto(ShiftDTO shiftDTO, Shift shift) {
        if ( shiftDTO == null ) {
            return shift;
        }

        shift.setId( shiftDTO.getId() );
        shift.setShiftDate( shiftDTO.getShiftDate() );

        setRelations( shiftDTO, shift );

        return shift;
    }

    private Long shiftShiftTypeId(Shift shift) {
        if ( shift == null ) {
            return null;
        }
        ShiftType shiftType = shift.getShiftType();
        if ( shiftType == null ) {
            return null;
        }
        Long id = shiftType.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String shiftShiftTypeName(Shift shift) {
        if ( shift == null ) {
            return null;
        }
        ShiftType shiftType = shift.getShiftType();
        if ( shiftType == null ) {
            return null;
        }
        String name = shiftType.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private LocalTime shiftShiftTypeStartTime(Shift shift) {
        if ( shift == null ) {
            return null;
        }
        ShiftType shiftType = shift.getShiftType();
        if ( shiftType == null ) {
            return null;
        }
        LocalTime startTime = shiftType.getStartTime();
        if ( startTime == null ) {
            return null;
        }
        return startTime;
    }

    private LocalTime shiftShiftTypeEndTime(Shift shift) {
        if ( shift == null ) {
            return null;
        }
        ShiftType shiftType = shift.getShiftType();
        if ( shiftType == null ) {
            return null;
        }
        LocalTime endTime = shiftType.getEndTime();
        if ( endTime == null ) {
            return null;
        }
        return endTime;
    }

    private Long shiftLocationId(Shift shift) {
        if ( shift == null ) {
            return null;
        }
        Location location = shift.getLocation();
        if ( location == null ) {
            return null;
        }
        Long id = location.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String shiftLocationName(Shift shift) {
        if ( shift == null ) {
            return null;
        }
        Location location = shift.getLocation();
        if ( location == null ) {
            return null;
        }
        String name = location.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
