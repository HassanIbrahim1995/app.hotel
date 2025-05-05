package com.shiftmanager.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "State is required")
    private String state;
    
    @NotBlank(message = "Zip code is required")
    private String zipCode;
    
    private String country;
    
    private String phoneNumber;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @NotNull(message = "Active status is required")
    private Boolean active = true;
    
    private String note;
}
