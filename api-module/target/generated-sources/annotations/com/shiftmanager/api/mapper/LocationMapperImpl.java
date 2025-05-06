package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.LocationDTO;
import com.shiftmanager.api.model.Location;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-06T01:20:51+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class LocationMapperImpl implements LocationMapper {

    @Override
    public LocationDTO toDTO(Location location) {
        if ( location == null ) {
            return null;
        }

        LocationDTO locationDTO = new LocationDTO();

        locationDTO.setId( location.getId() );
        locationDTO.setName( location.getName() );
        locationDTO.setAddress( location.getAddress() );
        locationDTO.setCity( location.getCity() );
        locationDTO.setState( location.getState() );
        locationDTO.setZipCode( location.getZipCode() );
        locationDTO.setCountry( location.getCountry() );
        locationDTO.setPhoneNumber( location.getPhoneNumber() );
        locationDTO.setEmail( location.getEmail() );
        locationDTO.setActive( location.getActive() );
        locationDTO.setNote( location.getNote() );

        return locationDTO;
    }

    @Override
    public Location toEntity(LocationDTO locationDTO) {
        if ( locationDTO == null ) {
            return null;
        }

        Location location = new Location();

        location.setId( locationDTO.getId() );
        location.setName( locationDTO.getName() );
        location.setAddress( locationDTO.getAddress() );
        location.setCity( locationDTO.getCity() );
        location.setState( locationDTO.getState() );
        location.setZipCode( locationDTO.getZipCode() );
        location.setCountry( locationDTO.getCountry() );
        location.setPhoneNumber( locationDTO.getPhoneNumber() );
        location.setEmail( locationDTO.getEmail() );
        location.setActive( locationDTO.getActive() );
        location.setNote( locationDTO.getNote() );

        return location;
    }

    @Override
    public void updateEntityFromDTO(LocationDTO locationDTO, Location location) {
        if ( locationDTO == null ) {
            return;
        }

        location.setId( locationDTO.getId() );
        location.setName( locationDTO.getName() );
        location.setAddress( locationDTO.getAddress() );
        location.setCity( locationDTO.getCity() );
        location.setState( locationDTO.getState() );
        location.setZipCode( locationDTO.getZipCode() );
        location.setCountry( locationDTO.getCountry() );
        location.setPhoneNumber( locationDTO.getPhoneNumber() );
        location.setEmail( locationDTO.getEmail() );
        location.setActive( locationDTO.getActive() );
        location.setNote( locationDTO.getNote() );
    }
}
