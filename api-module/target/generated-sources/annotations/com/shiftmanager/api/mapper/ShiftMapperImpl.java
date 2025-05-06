package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.ShiftDTO;
import com.shiftmanager.api.model.Location;
import com.shiftmanager.api.model.Shift;
import com.shiftmanager.api.model.ShiftType;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-06T01:20:51+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class ShiftMapperImpl implements ShiftMapper {

    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private ShiftTypeMapper shiftTypeMapper;

    @Override
    public ShiftDTO toDto(Shift shift) {
        if ( shift == null ) {
            return null;
        }

        ShiftDTO shiftDTO = new ShiftDTO();

        shiftDTO.setId( shift.getId() );
        shiftDTO.setShiftDate( shift.getShiftDate() );
        shiftDTO.setStartTime( shift.getStartTime() );
        shiftDTO.setEndTime( shift.getEndTime() );
        shiftDTO.setLocation( locationMapper.toDTO( shift.getLocation() ) );
        shiftDTO.setShiftType( shiftTypeMapper.toDTO( shift.getShiftType() ) );
        shiftDTO.setNote( shift.getNote() );
        shiftDTO.setCreatedById( shift.getCreatedById() );
        shiftDTO.setCreatedAt( shift.getCreatedAt() );
        shiftDTO.setUpdatedAt( shift.getUpdatedAt() );

        return shiftDTO;
    }

    @Override
    public Shift toEntity(ShiftDTO shiftDTO) {
        if ( shiftDTO == null ) {
            return null;
        }

        Shift shift = new Shift();

        shift.setId( shiftDTO.getId() );
        shift.setShiftDate( shiftDTO.getShiftDate() );
        shift.setStartTime( shiftDTO.getStartTime() );
        shift.setEndTime( shiftDTO.getEndTime() );
        shift.setLocation( locationMapper.toEntity( shiftDTO.getLocation() ) );
        shift.setShiftType( shiftTypeMapper.toEntity( shiftDTO.getShiftType() ) );
        shift.setNote( shiftDTO.getNote() );

        return shift;
    }

    @Override
    public void updateEntityFromDTO(ShiftDTO shiftDTO, Shift shift) {
        if ( shiftDTO == null ) {
            return;
        }

        shift.setId( shiftDTO.getId() );
        shift.setShiftDate( shiftDTO.getShiftDate() );
        shift.setStartTime( shiftDTO.getStartTime() );
        shift.setEndTime( shiftDTO.getEndTime() );
        if ( shiftDTO.getLocation() != null ) {
            if ( shift.getLocation() == null ) {
                shift.setLocation( new Location() );
            }
            locationMapper.updateEntityFromDTO( shiftDTO.getLocation(), shift.getLocation() );
        }
        else {
            shift.setLocation( null );
        }
        if ( shiftDTO.getShiftType() != null ) {
            if ( shift.getShiftType() == null ) {
                shift.setShiftType( new ShiftType() );
            }
            shiftTypeMapper.updateEntityFromDTO( shiftDTO.getShiftType(), shift.getShiftType() );
        }
        else {
            shift.setShiftType( null );
        }
        shift.setNote( shiftDTO.getNote() );
    }
}
