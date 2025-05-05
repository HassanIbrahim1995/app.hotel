package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Calendar entity - represents an employee's schedule calendar
 */
@Entity
@Table(name = "calendar")
@Getter
@Setter
@NoArgsConstructor
public class Calendar extends BaseEntity {

    @NotNull(message = "Employee is required")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(name = "year")
    private int year;
    
    @Column(name = "month")
    private int month;
    
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CalendarEntry> entries = new HashSet<>();
    
    /**
     * Add an entry to the calendar
     * @param entry Calendar entry to add
     */
    public void addEntry(CalendarEntry entry) {
        entries.add(entry);
        entry.setCalendar(this);
    }
    
    /**
     * Remove an entry from the calendar
     * @param entry Calendar entry to remove
     */
    public void removeEntry(CalendarEntry entry) {
        entries.remove(entry);
        entry.setCalendar(null);
    }
    
    /**
     * Get entries for a specific date
     * @param date Date to get entries for
     * @return Set of entries for the date
     */
    @Transient
    public Set<CalendarEntry> getEntriesForDate(LocalDate date) {
        return entries.stream()
                .filter(entry -> entry.getEntryDate().equals(date))
                .collect(java.util.stream.Collectors.toSet());
    }
    
    /**
     * Get entries of a specific type
     * @param entryType Type of entries to get
     * @return Set of entries of the specified type
     */
    @Transient
    public Set<CalendarEntry> getEntriesByType(String entryType) {
        return entries.stream()
                .filter(entry -> entry.getEntryType().equals(entryType))
                .collect(java.util.stream.Collectors.toSet());
    }
    
    /**
     * Get entries for a date range
     * @param startDate Start date
     * @param endDate End date
     * @return Set of entries in the date range
     */
    @Transient
    public Set<CalendarEntry> getEntriesForDateRange(LocalDate startDate, LocalDate endDate) {
        return entries.stream()
                .filter(entry -> {
                    LocalDate entryDate = entry.getEntryDate();
                    return !entryDate.isBefore(startDate) && !entryDate.isAfter(endDate);
                })
                .collect(java.util.stream.Collectors.toSet());
    }
}