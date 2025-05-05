package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.LocationDTO;
import com.shiftmanager.api.model.Location;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T22:02:40+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (N/A)"
)
@Component
public class LocationMapperImpl extends LocationMapper {

    @Override
    public LocationDTO toDto(Location location) {
        if ( location == null ) {
            return null;
        }

        LocationDTO.LocationDTOBuilder locationDTO = LocationDTO.builder();

        locationDTO.id( location.getId() );
        locationDTO.name( location.getName() );
        locationDTO.description( location.getDescription() );
        locationDTO.address( location.getAddress() );
        locationDTO.city( location.getCity() );
        locationDTO.stateProvince( location.getStateProvince() );
        locationDTO.country( location.getCountry() );
        locationDTO.postalCode( location.getPostalCode() );
        locationDTO.phone( location.getPhone() );
        locationDTO.active( location.isActive() );

        return locationDTO.build();
    }

    @Override
    public Location toEntity(LocationDTO locationDTO) {
        if ( locationDTO == null ) {
            return null;
        }

        Location location = new Location();

        location.setId( locationDTO.getId() );
        location.setName( locationDTO.getName() );
        location.setDescription( locationDTO.getDescription() );
        location.setAddress( locationDTO.getAddress() );
        location.setCity( locationDTO.getCity() );
        location.setStateProvince( locationDTO.getStateProvince() );
        location.setCountry( locationDTO.getCountry() );
        location.setPostalCode( locationDTO.getPostalCode() );
        location.setPhone( locationDTO.getPhone() );
        location.setActive( locationDTO.isActive() );

        return location;
    }

    @Override
    public Location updateEntityFromDto(LocationDTO locationDTO, Location location) {
        if ( locationDTO == null ) {
            return location;
        }

        location.setId( locationDTO.getId() );
        location.setName( locationDTO.getName() );
        location.setDescription( locationDTO.getDescription() );
        location.setAddress( locationDTO.getAddress() );
        location.setCity( locationDTO.getCity() );
        location.setStateProvince( locationDTO.getStateProvince() );
        location.setCountry( locationDTO.getCountry() );
        location.setPostalCode( locationDTO.getPostalCode() );
        location.setPhone( locationDTO.getPhone() );
        location.setActive( locationDTO.isActive() );

        return location;
    }
}
