package com.shiftmanager.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a physical address
 */
@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends BaseEntity {

    @NotBlank
    @Size(max = 255)
    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Size(max = 255)
    @Column(name = "street_address2")
    private String streetAddress2;

    @NotBlank
    @Size(max = 100)
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank
    @Size(max = 100)
    @Column(name = "state", nullable = false)
    private String state;

    @NotBlank
    @Size(max = 20)
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @NotBlank
    @Size(max = 100)
    @Column(name = "country", nullable = false)
    private String country;

    @Override
    public String toString() {
        StringBuilder addressBuilder = new StringBuilder();
        addressBuilder.append(streetAddress);
        
        if (streetAddress2 != null && !streetAddress2.isBlank()) {
            addressBuilder.append(", ").append(streetAddress2);
        }
        
        addressBuilder.append(", ")
                      .append(city)
                      .append(", ")
                      .append(state)
                      .append(" ")
                      .append(postalCode)
                      .append(", ")
                      .append(country);
        
        return addressBuilder.toString();
    }
}
