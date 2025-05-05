package com.shiftmanager.api.dto;

import java.time.LocalTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftTypeDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Default start time is required")
    private LocalTime defaultStartTime;
    
    @NotNull(message = "Default end time is required")
    private LocalTime defaultEndTime;
    
    @NotBlank(message = "Color is required")
    private String color;
    
    @NotNull(message = "Active status is required")
    private Boolean active = true;
}
