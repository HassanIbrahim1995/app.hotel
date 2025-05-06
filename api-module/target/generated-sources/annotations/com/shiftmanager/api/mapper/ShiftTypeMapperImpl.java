package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.ShiftTypeDTO;
import com.shiftmanager.api.model.ShiftType;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-06T01:20:51+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class ShiftTypeMapperImpl implements ShiftTypeMapper {

    @Override
    public ShiftTypeDTO toDTO(ShiftType shiftType) {
        if ( shiftType == null ) {
            return null;
        }

        ShiftTypeDTO shiftTypeDTO = new ShiftTypeDTO();

        shiftTypeDTO.setId( shiftType.getId() );
        shiftTypeDTO.setName( shiftType.getName() );
        shiftTypeDTO.setDescription( shiftType.getDescription() );
        shiftTypeDTO.setDefaultStartTime( shiftType.getDefaultStartTime() );
        shiftTypeDTO.setDefaultEndTime( shiftType.getDefaultEndTime() );
        shiftTypeDTO.setColor( shiftType.getColor() );
        shiftTypeDTO.setActive( shiftType.getActive() );

        return shiftTypeDTO;
    }

    @Override
    public ShiftType toEntity(ShiftTypeDTO shiftTypeDTO) {
        if ( shiftTypeDTO == null ) {
            return null;
        }

        ShiftType shiftType = new ShiftType();

        shiftType.setId( shiftTypeDTO.getId() );
        shiftType.setName( shiftTypeDTO.getName() );
        shiftType.setDescription( shiftTypeDTO.getDescription() );
        shiftType.setDefaultStartTime( shiftTypeDTO.getDefaultStartTime() );
        shiftType.setDefaultEndTime( shiftTypeDTO.getDefaultEndTime() );
        shiftType.setColor( shiftTypeDTO.getColor() );
        shiftType.setActive( shiftTypeDTO.getActive() );

        return shiftType;
    }

    @Override
    public void updateEntityFromDTO(ShiftTypeDTO shiftTypeDTO, ShiftType shiftType) {
        if ( shiftTypeDTO == null ) {
            return;
        }

        shiftType.setId( shiftTypeDTO.getId() );
        shiftType.setName( shiftTypeDTO.getName() );
        shiftType.setDescription( shiftTypeDTO.getDescription() );
        shiftType.setDefaultStartTime( shiftTypeDTO.getDefaultStartTime() );
        shiftType.setDefaultEndTime( shiftTypeDTO.getDefaultEndTime() );
        shiftType.setColor( shiftTypeDTO.getColor() );
        shiftType.setActive( shiftTypeDTO.getActive() );
    }
}
