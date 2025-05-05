package com.shiftmanager.api.model;

import java.time.LocalTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shift_types")
@Data
@NoArgsConstructor
public class ShiftType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    private String description;
    
    @Column(name = "default_start_time", nullable = false)
    private LocalTime defaultStartTime;
    
    @Column(name = "default_end_time", nullable = false)
    private LocalTime defaultEndTime;
    
    @Column(nullable = false)
    private String color;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    /**
     * Set start time (compatibility method for InitialDataLoader)
     * @param startTime The start time
     */
    public void setStartTime(LocalTime startTime) {
        this.defaultStartTime = startTime;
    }
    
    /**
     * Set end time (compatibility method for InitialDataLoader)
     * @param endTime The end time
     */
    public void setEndTime(LocalTime endTime) {
        this.defaultEndTime = endTime;
    }
}
