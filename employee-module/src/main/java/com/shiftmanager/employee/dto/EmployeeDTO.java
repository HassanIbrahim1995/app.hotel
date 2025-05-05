package com.shiftmanager.employee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for Employee entity operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDTO {

    private Long id;
    
    @Valid
    @NotNull(message = "Name is required")
    private NameDTO name;
    
    @Valid
    @NotNull(message = "Address is required")
    private AddressDTO address;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    private String nationalId;
    
    private Long locationId;
    private String locationName;
    
    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;
    
    private LocalDate terminationDate;
    
    @NotBlank(message = "Employee number is required")
    private String employeeNumber;
    
    private String position;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    private Long managerId;
    private String managerName;

    /**
     * DTO for Name embedded in Employee
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NameDTO {
        @NotBlank(message = "First name is required")
        private String firstName;
        
        private String middleName;
        
        @NotBlank(message = "Last name is required")
        private String lastName;
    }

    /**
     * DTO for Address embedded in Employee
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDTO {
        @NotBlank(message = "Street is required")
        private String street;
        
        @NotBlank(message = "City is required")
        private String city;
        
        @NotBlank(message = "State is required")
        private String state;
        
        @NotBlank(message = "Zip code is required")
        private String zipCode;
        
        private String country;
    }
}
