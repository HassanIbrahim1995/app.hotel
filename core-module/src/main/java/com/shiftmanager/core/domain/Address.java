package com.shiftmanager.core.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Embeddable class for address information
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @NotBlank
    private String street;
    
    @NotBlank
    private String city;
    
    @NotBlank
    private String state;
    
    @NotBlank
    private String zipCode;
    
    private String country;
    
    /**
     * Returns the full address as a formatted string
     * @return Formatted address string
     */
    public String getFullAddress() {
        return street + ", " + city + ", " + state + " " + zipCode + 
               (country != null && !country.isEmpty() ? ", " + country : "");
    }
}
