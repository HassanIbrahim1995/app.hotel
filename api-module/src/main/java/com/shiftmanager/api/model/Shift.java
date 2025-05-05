package com.shiftmanager.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "shifts")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class Shift {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_type_id", nullable = false)
    private ShiftType shiftType;
    
    private String note;
    
    @CreatedBy
    @Column(name = "created_by_id")
    private Long createdById;
    
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "updated_by_id")
    private Long updatedById;
    
    /**
     * Get notes (compatibility method for CalendarEntry)
     * @return The note for this shift
     */
    public String getNotes() {
        return this.note;
    }
    
    /**
     * Set notes (compatibility method for CalendarEntry)
     * @param notes The notes to set
     */
    public void setNotes(String notes) {
        this.note = notes;
    }
}
