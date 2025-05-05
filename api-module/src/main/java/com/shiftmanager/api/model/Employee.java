package com.shiftmanager.api.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@DiscriminatorValue("EMPLOYEE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Employee extends Person {
    
    @Column(name = "employee_number", nullable = false, unique = true)
    private String employeeNumber;
    
    @Column(nullable = false)
    private String position;
    
    private String department;
    
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Employee manager;
    
    @Column(name = "hourly_rate")
    private Double hourlyRate;
    
    @Column(name = "full_time")
    private Boolean fullTime = true;
    
    @Column(name = "max_hours_per_week")
    private Integer maxHoursPerWeek = 40;
    
    private String note;
    
    @Column(name = "status")
    private String status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<Employee> subordinates = new ArrayList<>();
    
    // Getter for fields used in InitialDataLoader
    public String getEmployeeId() {
        return employeeNumber;
    }
    
    // Setter for fields used in InitialDataLoader
    public void setEmployeeId(String employeeId) {
        this.employeeNumber = employeeId;
    }
    
    public String getJobTitle() {
        return position;
    }
    
    public void setJobTitle(String jobTitle) {
        this.position = jobTitle;
    }
    
    // Helper method to get manager name if available
    public String getManagerName() {
        return manager != null ? manager.getFullName() : null;
    }
    
    // Helper method to get manager ID if available
    public Long getManagerId() {
        return manager != null ? manager.getId() : null;
    }
    
    // Helper method to get subordinates
    public List<Employee> getSubordinates() {
        return subordinates;
    }
}
