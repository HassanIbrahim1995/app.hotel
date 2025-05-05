package com.shiftmanager.api.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    private String phoneNumber;
    
    private AddressDTO address;
    
    @NotBlank(message = "Employee number is required")
    private String employeeNumber;
    
    @NotBlank(message = "Position is required")
    private String position;
    
    private String department;
    
    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    private LocalDate hireDate;
    
    private Long managerId;
    
    private String managerName;
    
    @Positive(message = "Hourly rate must be positive")
    private Double hourlyRate;
    
    private Boolean fullTime = true;
    
    @Min(value = 1, message = "Max hours per week must be at least 1")
    private Integer maxHoursPerWeek = 40;
    
    private String note;
}
