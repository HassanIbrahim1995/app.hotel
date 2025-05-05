package com.shiftmanager.employeemanagement.model;

import com.shiftmanager.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Entity representing a time-off request from an employee
 */
@Entity
@Table(name = "time_off_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeOffRequest extends BaseEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private TimeOffRequestType requestType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TimeOffRequestStatus status = TimeOffRequestStatus.PENDING;

    @Size(max = 255)
    @Column(name = "reason")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Manager approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Size(max = 255)
    @Column(name = "approval_notes")
    private String approvalNotes;

    @Column(name = "full_day")
    private boolean fullDay = true;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * Calculate the number of calendar days this request covers
     * @return number of days
     */
    public int getDaysCount() {
        return Period.between(startDate, endDate).getDays() + 1; // +1 to include the end date
    }

    /**
     * Calculate the number of business days (excluding weekends) this request covers
     * @return number of business days
     */
    public int getBusinessDaysCount() {
        int businessDays = 0;
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            int dayOfWeek = date.getDayOfWeek().getValue();
            if (dayOfWeek != 6 && dayOfWeek != 7) { // Not Saturday or Sunday
                businessDays++;
            }
            date = date.plusDays(1);
        }
        return businessDays;
    }

    /**
     * Calculate the number of hours this request covers for partial days
     * @return number of hours or 0 if full days
     */
    public double getHoursCount() {
        if (fullDay || startTime == null || endTime == null) {
            return 0.0;
        }
        return Duration.between(startTime, endTime).toMinutes() / 60.0;
    }

    /**
     * Check if the request has been approved
     * @return true if approved
     */
    public boolean isApproved() {
        return status == TimeOffRequestStatus.APPROVED;
    }

    /**
     * Check if the request is still pending
     * @return true if pending
     */
    public boolean isPending() {
        return status == TimeOffRequestStatus.PENDING;
    }
}
