package com.shiftmanager.schedule.service;

import com.shiftmanager.schedule.dto.CalendarDTO;
import com.shiftmanager.schedule.dto.ScheduleDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Schedule operations
 */
public interface ScheduleService {

    /**
     * Create a new schedule
     * @param scheduleDTO The schedule data
     * @return The created schedule
     */
    ScheduleDTO createSchedule(ScheduleDTO scheduleDTO);

    /**
     * Get a schedule by ID
     * @param id The schedule ID
     * @return The schedule data
     */
    ScheduleDTO getScheduleById(Long id);

    /**
     * Get all schedules
     * @return List of all schedules
     */
    List<ScheduleDTO> getAllSchedules();

    /**
     * Update a schedule
     * @param id The schedule ID
     * @param scheduleDTO The updated schedule data
     * @return The updated schedule
     */
    ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO);

    /**
     * Delete a schedule
     * @param id The schedule ID
     */
    void deleteSchedule(Long id);

    /**
     * Get schedule by employee
     * @param employeeId The employee ID
     * @return The employee's schedule
     */
    ScheduleDTO getScheduleByEmployee(Long employeeId);

    /**
     * Get schedule by manager
     * @param managerId The manager ID
     * @return The manager's schedule
     */
    ScheduleDTO getScheduleByManager(Long managerId);

    /**
     * Get schedules by date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of schedules within the specified date range
     */
    List<ScheduleDTO> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get a calendar for an employee with all scheduled shifts and vacations
     * @param employeeId The employee ID
     * @param startDate The start date
     * @param endDate The end date
     * @return Calendar data with all entries
     */
    CalendarDTO getEmployeeCalendar(Long employeeId, LocalDate startDate, LocalDate endDate);

    /**
     * Get a calendar for a manager with all scheduled shifts and vacations
     * @param managerId The manager ID
     * @param startDate The start date
     * @param endDate The end date
     * @return Calendar data with all entries
     */
    CalendarDTO getManagerCalendar(Long managerId, LocalDate startDate, LocalDate endDate);

    /**
     * Create or update an employee's schedule
     * @param employeeId The employee ID
     * @param scheduleDTO The schedule data
     * @return The created or updated schedule
     */
    ScheduleDTO createOrUpdateEmployeeSchedule(Long employeeId, ScheduleDTO scheduleDTO);

    /**
     * Create or update a manager's schedule
     * @param managerId The manager ID
     * @param scheduleDTO The schedule data
     * @return The created or updated schedule
     */
    ScheduleDTO createOrUpdateManagerSchedule(Long managerId, ScheduleDTO scheduleDTO);
}
