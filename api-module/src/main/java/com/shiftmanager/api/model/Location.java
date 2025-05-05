package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Location entity for storing work locations
 */
@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
public class Location extends BaseEntity {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    @Column(name = "description")
    private String description;
    
    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    @Column(name = "address", nullable = false)
    private String address;
    
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    @Column(name = "city", nullable = false)
    private String city;
    
    @NotBlank(message = "State/Province is required")
    @Size(max = 100, message = "State/Province cannot exceed 100 characters")
    @Column(name = "state_province", nullable = false)
    private String stateProvince;
    
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    @Column(name = "country", nullable = false)
    private String country;
    
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    @Column(name = "postal_code")
    private String postalCode;
    
    @Size(max = 20, message = "Phone cannot exceed 20 characters")
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "location")
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "location")
    private Set<Shift> shifts = new HashSet<>();

    /**
     * Constructor with name and description
     * @param name Location name
     * @param description Location description
     */
    public Location(String name, String description) {
        this.name = name;
        this.description = description;
        this.active = true; // Default to active
    }
    
    /**
     * Full constructor
     * @param name Location name
     * @param description Location description
     * @param address Address
     * @param city City
     * @param stateProvince State/Province
     * @param country Country
     * @param postalCode Postal code
     * @param phone Phone
     */
    public Location(String name, String description, String address, String city, 
                   String stateProvince, String country, String postalCode, String phone) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.stateProvince = stateProvince;
        this.country = country;
        this.postalCode = postalCode;
        this.phone = phone;
        this.active = true; // Default to active
    }
}
