package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Person entity, base class for all personnel
 */
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "person_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PERSON")
@Getter
@Setter
@NoArgsConstructor
public class Person extends BaseEntity {

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses = new HashSet<>();

    /**
     * Add an address to the person
     * @param address the address to add
     * @return the address
     */
    public Address addAddress(Address address) {
        addresses.add(address);
        address.setPerson(this);
        return address;
    }

    /**
     * Remove an address from the person
     * @param address the address to remove
     */
    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setPerson(null);
    }

    /**
     * Get the full name of the person
     * @return the full name
     */
    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
