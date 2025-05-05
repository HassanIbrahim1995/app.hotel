package com.shiftmanager.schedule.service.impl;

import com.shiftmanager.core.domain.*;
import com.shiftmanager.core.repository.*;
import com.shiftmanager.notification.service.NotificationService;
import com.shiftmanager.schedule.dto.CalendarDTO;
import com.shiftmanager.schedule.dto.ScheduleDTO;
import com.shiftmanager.schedule.service.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ScheduleService
 */
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final ShiftRepository shiftRepository;
    private final VacationRequestRepository vacationRequestRepository;
    private final NotificationService notificationService;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    @Transactional
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        validateScheduleDates(scheduleDTO);
        
        Schedule schedule = mapDtoToEntity(scheduleDTO);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        
        // Notify the associated employee if there is one
        if (savedSchedule.getEmployee() != null) {
            notifyAboutSchedule(savedSchedule.getEmployee().getId(), "New schedule created", 
                    "A new schedule has been created for you from " + 
                    savedSchedule.getStartDate() + " to " + savedSchedule.getEndDate());
        }
        
        return mapEntityToDto(savedSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleDTO getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + id));
        return mapEntityToDto(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + id));
        
        validateScheduleDates(scheduleDTO);
        
        // Keep track of the old dates to see if they changed
        LocalDate oldStartDate = schedule.getStartDate();
        LocalDate oldEndDate = schedule.getEndDate();
        
        // Update the schedule properties
        updateScheduleFromDto(schedule, scheduleDTO);
        
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        
        // Notify employee about updates if dates changed
        if (updatedSchedule.getEmployee() != null && 
            (!oldStartDate.equals(updatedSchedule.getStartDate()) || !oldEndDate.equals(updatedSchedule.getEndDate()))) {
            notifyAboutSchedule(updatedSchedule.getEmployee().getId(), "Schedule updated", 
                    "Your schedule has been updated. The new dates are from " + 
                    updatedSchedule.getStartDate() + " to " + updatedSchedule.getEndDate());
        }
        
        return mapEntityToDto(updatedSchedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + id));
        
        // Notify the associated employee if there is one
        if (schedule.getEmployee() != null) {
            notifyAboutSchedule(schedule.getEmployee().getId(), "Schedule deleted", 
                    "Your schedule for " + schedule.getStartDate() + " to " + schedule.getEndDate() + " has been deleted");
        }
        
        scheduleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleDTO getScheduleByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
        
        Schedule schedule = scheduleRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found for employee with id: " + employeeId));
        
        return mapEntityToDto(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleDTO getScheduleByManager(Long managerId) {
        if (!managerRepository.existsById(managerId)) {
            throw new EntityNotFoundException("Manager not found with id: " + managerId);
        }
        
        Schedule schedule = scheduleRepository.findByManagerId(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found for manager with id: " + managerId));
        
        return mapEntityToDto(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate)
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CalendarDTO getEmployeeCalendar(Long employeeId, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));
        
        CalendarDTO calendar = initializeCalendar(employee.getName().getFullName(), employeeId, "EMPLOYEE", startDate, endDate);
        
        // Add shifts to calendar
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        List<Shift> shifts = shiftRepository.findByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                employeeId, startDateTime, endDateTime);
        
        double totalHours = 0.0;
        
        for (Shift shift : shifts) {
            CalendarDTO.CalendarEntryDTO entry = CalendarDTO.CalendarEntryDTO.fromShift(
                    shift.getId(),
                    shift.getShiftType().getDisplayName(),
                    shift.getStartTime().toLocalDate(),
                    shift.getStartTime().format(TIME_FORMATTER),
                    shift.getEndTime().format(TIME_FORMATTER),
                    shift.getDurationInHours(),
                    shift.getNotes()
            );
            
            calendar.addEntry(entry);
            totalHours += shift.getDurationInHours();
        }
        
        // Add vacation periods to calendar
        List<VacationRequest> vacations = vacationRequestRepository.findByEmployeeIdAndDateRange(
                employeeId, startDate, endDate);
        
        for (VacationRequest vacation : vacations) {
            // For each day in the vacation period, add a calendar entry
            LocalDate currentDate = vacation.getStartDate();
            while (!currentDate.isAfter(vacation.getEndDate())) {
                if (!currentDate.isBefore(startDate) && !currentDate.isAfter(endDate)) {
                    CalendarDTO.CalendarEntryDTO entry = CalendarDTO.CalendarEntryDTO.fromVacation(
                            vacation.getId(),
                            currentDate,
                            vacation.getStatus().toString(),
                            vacation.getReason()
                    );
                    calendar.addEntry(entry);
                }
                currentDate = currentDate.plusDays(1);
            }
        }
        
        calendar.setTotalScheduledHours(totalHours);
        
        return calendar;
    }

    @Override
    @Transactional(readOnly = true)
    public CalendarDTO getManagerCalendar(Long managerId, LocalDate startDate, LocalDate endDate) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + managerId));
        
        CalendarDTO calendar = initializeCalendar(manager.getName().getFullName(), managerId, "MANAGER", startDate, endDate);
        
        // Add manager's shifts to calendar
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        // Note: In a real application, managers might have their own shifts
        // For now, we're fetching any shifts they might have (they might be both employees and managers)
        List<Shift> shifts = shiftRepository.findAll().stream()
                .filter(shift -> shift.getEmployee().getId().equals(managerId))
                .filter(shift -> !shift.getStartTime().isBefore(startDateTime) && !shift.getEndTime().isAfter(endDateTime))
                .collect(Collectors.toList());
        
        double totalHours = 0.0;
        
        for (Shift shift : shifts) {
            CalendarDTO.CalendarEntryDTO entry = CalendarDTO.CalendarEntryDTO.fromShift(
                    shift.getId(),
                    shift.getShiftType().getDisplayName(),
                    shift.getStartTime().toLocalDate(),
                    shift.getStartTime().format(TIME_FORMATTER),
                    shift.getEndTime().format(TIME_FORMATTER),
                    shift.getDurationInHours(),
                    shift.getNotes()
            );
            
            calendar.addEntry(entry);
            totalHours += shift.getDurationInHours();
        }
        
        // Add vacation periods to calendar (if manager can take vacations)
        List<VacationRequest> vacations = vacationRequestRepository.findAll().stream()
                .filter(vr -> {
                    if (vr.getEmployee() != null && vr.getEmployee().getId().equals(managerId)) {
                        return true;
                    }
                    return false;
                })
                .filter(vr -> !vr.getStartDate().isAfter(endDate) && !vr.getEndDate().isBefore(startDate))
                .collect(Collectors.toList());
        
        for (VacationRequest vacation : vacations) {
            // For each day in the vacation period, add a calendar entry
            LocalDate currentDate = vacation.getStartDate();
            while (!currentDate.isAfter(vacation.getEndDate())) {
                if (!currentDate.isBefore(startDate) && !currentDate.isAfter(endDate)) {
                    CalendarDTO.CalendarEntryDTO entry = CalendarDTO.CalendarEntryDTO.fromVacation(
                            vacation.getId(),
                            currentDate,
                            vacation.getStatus().toString(),
                            vacation.getReason()
                    );
                    calendar.addEntry(entry);
                }
                currentDate = currentDate.plusDays(1);
            }
        }
        
        calendar.setTotalScheduledHours(totalHours);
        
        return calendar;
    }

    @Override
    @Transactional
    public ScheduleDTO createOrUpdateEmployeeSchedule(Long employeeId, ScheduleDTO scheduleDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));
        
        // Check if employee already has a schedule
        Schedule existingSchedule = scheduleRepository.findByEmployeeId(employeeId).orElse(null);
        
        if (existingSchedule != null) {
            // Update existing schedule
            updateScheduleFromDto(existingSchedule, scheduleDTO);
            Schedule updatedSchedule = scheduleRepository.save(existingSchedule);
            
            // Notify employee about schedule update
            notifyAboutSchedule(employeeId, "Schedule updated", 
                    "Your schedule has been updated for the period " + 
                    updatedSchedule.getStartDate() + " to " + updatedSchedule.getEndDate());
            
            return mapEntityToDto(updatedSchedule);
        } else {
            // Create new schedule
            validateScheduleDates(scheduleDTO);
            
            Schedule schedule = new Schedule();
            schedule.setName(scheduleDTO.getName());
            schedule.setEmployee(employee);
            schedule.setStartDate(scheduleDTO.getStartDate());
            schedule.setEndDate(scheduleDTO.getEndDate());
            schedule.setNotes(scheduleDTO.getNotes());
            
            Schedule savedSchedule = scheduleRepository.save(schedule);
            
            // Notify employee about new schedule
            notifyAboutSchedule(employeeId, "New schedule created", 
                    "A new schedule has been created for you from " + 
                    savedSchedule.getStartDate() + " to " + savedSchedule.getEndDate());
            
            return mapEntityToDto(savedSchedule);
        }
    }

    @Override
    @Transactional
    public ScheduleDTO createOrUpdateManagerSchedule(Long managerId, ScheduleDTO scheduleDTO) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + managerId));
        
        // Check if manager already has a schedule
        Schedule existingSchedule = scheduleRepository.findByManagerId(managerId).orElse(null);
        
        if (existingSchedule != null) {
            // Update existing schedule
            updateScheduleFromDto(existingSchedule, scheduleDTO);
            Schedule updatedSchedule = scheduleRepository.save(existingSchedule);
            return mapEntityToDto(updatedSchedule);
        } else {
            // Create new schedule
            validateScheduleDates(scheduleDTO);
            
            Schedule schedule = new Schedule();
            schedule.setName(scheduleDTO.getName());
            schedule.setManager(manager);
            schedule.setStartDate(scheduleDTO.getStartDate());
            schedule.setEndDate(scheduleDTO.getEndDate());
            schedule.setNotes(scheduleDTO.getNotes());
            
            Schedule savedSchedule = scheduleRepository.save(schedule);
            return mapEntityToDto(savedSchedule);
        }
    }

    /**
     * Validates that schedule dates are valid (end date not before start date)
     * @param scheduleDTO The schedule to validate
     */
    private void validateScheduleDates(ScheduleDTO scheduleDTO) {
        if (scheduleDTO.getStartDate() == null || scheduleDTO.getEndDate() == null) {
            throw new IllegalArgumentException("Schedule start and end dates cannot be null");
        }
        
        if (scheduleDTO.getEndDate().isBefore(scheduleDTO.getStartDate())) {
            throw new IllegalArgumentException("Schedule end date must not be before start date");
        }
    }

    /**
     * Maps ScheduleDTO to Schedule entity
     * @param dto The DTO to map
     * @return The mapped entity
     */
    private Schedule mapDtoToEntity(ScheduleDTO dto) {
        Schedule schedule = new Schedule();
        schedule.setId(dto.getId());
        schedule.setName(dto.getName());
        schedule.setStartDate(dto.getStartDate());
        schedule.setEndDate(dto.getEndDate());
        schedule.setNotes(dto.getNotes());
        
        // Set employee if provided
        if (dto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
            schedule.setEmployee(employee);
        }
        
        // Set manager if provided
        if (dto.getManagerId() != null) {
            Manager manager = managerRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + dto.getManagerId()));
            schedule.setManager(manager);
        }
        
        return schedule;
    }

    /**
     * Maps Schedule entity to ScheduleDTO
     * @param schedule The entity to map
     * @return The mapped DTO
     */
    private ScheduleDTO mapEntityToDto(Schedule schedule) {
        ScheduleDTO dto = ScheduleDTO.builder()
                .id(schedule.getId())
                .name(schedule.getName())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .notes(schedule.getNotes())
                .totalHours(schedule.getTotalHours())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
        
        // Set employee if available
        if (schedule.getEmployee() != null) {
            dto.setEmployeeId(schedule.getEmployee().getId());
            dto.setEmployeeName(schedule.getEmployee().getName().getFullName());
        }
        
        // Set manager if available
        if (schedule.getManager() != null) {
            dto.setManagerId(schedule.getManager().getId());
            dto.setManagerName(schedule.getManager().getName().getFullName());
        }
        
        // Add shift summaries
        List<ScheduleDTO.ShiftSummaryDTO> shiftSummaries = schedule.getShifts().stream()
                .map(shift -> ScheduleDTO.ShiftSummaryDTO.builder()
                        .id(shift.getId())
                        .shiftType(shift.getShiftType().getDisplayName())
                        .startTime(shift.getStartTime())
                        .endTime(shift.getEndTime())
                        .durationInHours(shift.getDurationInHours())
                        .build())
                .collect(Collectors.toList());
        
        dto.setShifts(shiftSummaries);
        
        return dto;
    }

    /**
     * Updates a Schedule entity from DTO
     * @param schedule The entity to update
     * @param dto The DTO containing updated data
     */
    private void updateScheduleFromDto(Schedule schedule, ScheduleDTO dto) {
        if (dto.getName() != null) {
            schedule.setName(dto.getName());
        }
        
        if (dto.getStartDate() != null) {
            schedule.setStartDate(dto.getStartDate());
        }
        
        if (dto.getEndDate() != null) {
            schedule.setEndDate(dto.getEndDate());
        }
        
        if (dto.getNotes() != null) {
            schedule.setNotes(dto.getNotes());
        }
        
        // Update employee if provided
        if (dto.getEmployeeId() != null && 
                (schedule.getEmployee() == null || !dto.getEmployeeId().equals(schedule.getEmployee().getId()))) {
            Employee employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
            schedule.setEmployee(employee);
        }
        
        // Update manager if provided
        if (dto.getManagerId() != null && 
                (schedule.getManager() == null || !dto.getManagerId().equals(schedule.getManager().getId()))) {
            Manager manager = managerRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + dto.getManagerId()));
            schedule.setManager(manager);
        }
    }

    /**
     * Initializes a calendar DTO with basic information
     */
    private CalendarDTO initializeCalendar(String personName, Long personId, String personType, 
                                          LocalDate startDate, LocalDate endDate) {
        return CalendarDTO.builder()
                .personName(personName)
                .personId(personId)
                .personType(personType)
                .startDate(startDate)
                .endDate(endDate)
                .entries(new ArrayList<>())
                .build();
    }

    /**
     * Notifies an employee about schedule changes
     */
    private void notifyAboutSchedule(Long employeeId, String title, String message) {
        notificationService.createNotification(employeeId, title, message);
    }
}
