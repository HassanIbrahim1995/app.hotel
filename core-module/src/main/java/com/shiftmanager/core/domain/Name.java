package com.shiftmanager.core.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Embeddable class for a person's name with first, middle, and last name
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Name {

    @NotBlank
    private String firstName;
    
    private String middleName;
    
    @NotBlank
    private String lastName;
    
    /**
     * Returns the full name of the person
     * @return Full name including middle name if present
     */
    public String getFullName() {
        if (middleName != null && !middleName.isEmpty()) {
            return firstName + " " + middleName + " " + lastName;
        }
        return firstName + " " + lastName;
    }
}
