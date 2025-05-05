package com.shiftmanager.api.dto;

import java.time.LocalTime;

/**
 * DTO for shift type
 */
public class ShiftTypeDTO {
    private Long id;
    private String name;
    private String description;
    private LocalTime defaultStartTime;
    private LocalTime defaultEndTime;
    private String color;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalTime getDefaultStartTime() {
        return defaultStartTime;
    }
    
    public void setDefaultStartTime(LocalTime defaultStartTime) {
        this.defaultStartTime = defaultStartTime;
    }
    
    public LocalTime getDefaultEndTime() {
        return defaultEndTime;
    }
    
    public void setDefaultEndTime(LocalTime defaultEndTime) {
        this.defaultEndTime = defaultEndTime;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}