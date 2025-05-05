package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Manager entity - extends Employee (which extends Person)
 * This class represents managers who have additional responsibilities
 * beyond regular employees.
 */
@Entity
@DiscriminatorValue("MANAGER")
@Getter
@Setter
@NoArgsConstructor
public class Manager extends Employee {

    @Size(max = 100, message = "Management level cannot exceed 100 characters")
    @Column(name = "management_level")
    private String managementLevel;
    
    /**
     * Get the number of direct reports
     * @return number of direct reports
     */
    @Transient
    public int getNumberOfDirectReports() {
        return getSubordinates().size();
    }
}