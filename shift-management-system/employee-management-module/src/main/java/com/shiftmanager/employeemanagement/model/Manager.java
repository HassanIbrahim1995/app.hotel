package com.shiftmanager.employeemanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a manager in the system
 */
@Entity
@Table(name = "managers")
@Getter
@Setter
@NoArgsConstructor
public class Manager extends Employee {

    @Column(name = "management_level")
    @Enumerated(EnumType.STRING)
    private ManagementLevel managementLevel;

    @Column(name = "promoted_to_manager_date")
    private LocalDate promotedToManagerDate;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "max_direct_reports")
    private Integer maxDirectReports;

    @OneToMany(mappedBy = "manager")
    private Set<Employee> directReports = new HashSet<>();

    @Column(name = "management_responsibilities")
    private String managementResponsibilities;

    /**
     * Enumeration for different management levels
     */
    public enum ManagementLevel {
        TEAM_LEADER, SUPERVISOR, MANAGER, SENIOR_MANAGER, DIRECTOR, EXECUTIVE
    }
}
