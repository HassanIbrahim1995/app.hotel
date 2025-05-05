package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.AddressDTO;
import com.shiftmanager.api.model.Address;
import com.shiftmanager.api.model.Person;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T22:02:40+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (N/A)"
)
@Component
public class AddressMapperImpl extends AddressMapper {

    @Override
    public AddressDTO toDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDTO.AddressDTOBuilder addressDTO = AddressDTO.builder();

        addressDTO.personId( addressPersonId( address ) );
        addressDTO.id( address.getId() );
        addressDTO.street( address.getStreet() );
        addressDTO.city( address.getCity() );
        addressDTO.state( address.getState() );
        addressDTO.zipCode( address.getZipCode() );
        addressDTO.country( address.getCountry() );

        return addressDTO.build();
    }

    @Override
    public Address toEntity(AddressDTO addressDTO) {
        if ( addressDTO == null ) {
            return null;
        }

        Address address = new Address();

        address.setId( addressDTO.getId() );
        address.setStreet( addressDTO.getStreet() );
        address.setCity( addressDTO.getCity() );
        address.setState( addressDTO.getState() );
        address.setZipCode( addressDTO.getZipCode() );
        address.setCountry( addressDTO.getCountry() );

        setPerson( addressDTO, address );

        return address;
    }

    @Override
    public Address updateEntityFromDto(AddressDTO addressDTO, Address address) {
        if ( addressDTO == null ) {
            return address;
        }

        address.setId( addressDTO.getId() );
        address.setStreet( addressDTO.getStreet() );
        address.setCity( addressDTO.getCity() );
        address.setState( addressDTO.getState() );
        address.setZipCode( addressDTO.getZipCode() );
        address.setCountry( addressDTO.getCountry() );

        setPerson( addressDTO, address );

        return address;
    }

    private Long addressPersonId(Address address) {
        if ( address == null ) {
            return null;
        }
        Person person = address.getPerson();
        if ( person == null ) {
            return null;
        }
        Long id = person.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
