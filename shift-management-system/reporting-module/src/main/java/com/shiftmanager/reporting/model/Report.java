package com.shiftmanager.reporting.model;

import com.shiftmanager.common.model.BaseEntity;
import com.shiftmanager.common.model.Location;
import com.shiftmanager.employeemanagement.model.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a generated report
 */
@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "generation_time")
    private LocalDateTime generationTime;

    @ManyToOne
    @JoinColumn(name = "generated_by")
    private Employee generatedBy;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "parameters", columnDefinition = "TEXT")
    private String parameters;

    @Size(max = 255)
    @Column(name = "file_path")
    private String filePath;

    @Column(name = "scheduled")
    private boolean scheduled = false;

    @Size(max = 100)
    @Column(name = "schedule_cron")
    private String scheduleCron;

    @Column(name = "last_run")
    private LocalDateTime lastRun;

    @Column(name = "next_run")
    private LocalDateTime nextRun;

    @Column(name = "active")
    private boolean active = true;

    /**
     * Check if this is a scheduled report
     * @return true if scheduled
     */
    public boolean isScheduled() {
        return scheduled && scheduleCron != null && !scheduleCron.isBlank();
    }

    /**
     * Get the date range for this report as a formatted string
     * @return formatted date range
     */
    public String getDateRangeFormatted() {
        return String.format("%s to %s", startDate.toString(), endDate.toString());
    }
}
