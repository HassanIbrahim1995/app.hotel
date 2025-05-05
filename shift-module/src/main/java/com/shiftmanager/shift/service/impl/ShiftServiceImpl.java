package com.shiftmanager.shift.service.impl;

import com.shiftmanager.core.domain.Employee;
import com.shiftmanager.core.domain.Schedule;
import com.shiftmanager.core.domain.Shift;
import com.shiftmanager.core.domain.ShiftType;
import com.shiftmanager.core.repository.EmployeeRepository;
import com.shiftmanager.core.repository.ScheduleRepository;
import com.shiftmanager.core.repository.ShiftRepository;
import com.shiftmanager.notification.service.NotificationService;
import com.shiftmanager.shift.dto.ShiftDTO;
import com.shiftmanager.shift.service.ShiftService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ShiftService
 */
@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final ScheduleRepository scheduleRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public ShiftDTO createShift(ShiftDTO shiftDTO) {
        validateShiftTimes(shiftDTO);
        validateNoOverlaps(shiftDTO);
        
        Shift shift = mapDtoToEntity(shiftDTO);
        Shift savedShift = shiftRepository.save(shift);
        
        // Add the shift to the employee's schedule
        addShiftToSchedule(savedShift);
        
        // Notify the employee about the new shift
        notifyEmployeeAboutShift(savedShift, "New shift assigned");
        
        return mapEntityToDto(savedShift);
    }

    @Override
    @Transactional
    public List<ShiftDTO> createShifts(List<ShiftDTO> shiftDTOs) {
        List<ShiftDTO> createdShifts = new ArrayList<>();
        
        for (ShiftDTO shiftDTO : shiftDTOs) {
            createdShifts.add(createShift(shiftDTO));
        }
        
        return createdShifts;
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftDTO getShiftById(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        return mapEntityToDto(shift);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDTO> getAllShifts() {
        return shiftRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ShiftDTO updateShift(Long id, ShiftDTO shiftDTO) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        
        // Save the old shift data for comparison
        LocalDateTime oldStartTime = shift.getStartTime();
        LocalDateTime oldEndTime = shift.getEndTime();
        
        // Update with new values
        shiftDTO.setId(id);
        validateShiftTimes(shiftDTO);
        
        // Check for overlaps excluding the current shift
        if (hasOverlappingShifts(shiftDTO)) {
            throw new IllegalStateException("The updated shift would overlap with existing shifts for the employee");
        }
        
        // Update shift properties
        updateShiftFromDto(shift, shiftDTO);
        
        Shift updatedShift = shiftRepository.save(shift);
        
        // Notify employee about the shift update if times changed
        if (!oldStartTime.equals(updatedShift.getStartTime()) || !oldEndTime.equals(updatedShift.getEndTime())) {
            notifyEmployeeAboutShift(updatedShift, "Shift updated");
        }
        
        return mapEntityToDto(updatedShift);
    }

    @Override
    @Transactional
    public void deleteShift(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with id: " + id));
        
        // Notify the employee about the canceled shift
        notifyEmployeeAboutShift(shift, "Shift canceled");
        
        // Remove the shift
        shiftRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDTO> getShiftsByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
        
        return shiftRepository.findByEmployeeId(employeeId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDTO> getShiftsBySchedule(Long scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new EntityNotFoundException("Schedule not found with id: " + scheduleId);
        }
        
        return shiftRepository.findByScheduleId(scheduleId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDTO> getShiftsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        // Custom implementation since there's no direct repository method for this
        return shiftRepository.findAll().stream()
                .filter(shift -> 
                    (shift.getStartTime().isEqual(startTime) || shift.getStartTime().isAfter(startTime)) && 
                    (shift.getEndTime().isEqual(endTime) || shift.getEndTime().isBefore(endTime)))
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDTO> getShiftsByEmployeeAndDateRange(Long employeeId, LocalDateTime startTime, LocalDateTime endTime) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
        
        return shiftRepository
                .findByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(employeeId, startTime, endTime)
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDTO> getShiftsByType(ShiftType shiftType) {
        return shiftRepository.findByShiftType(shiftType).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasOverlappingShifts(ShiftDTO shiftDTO) {
        List<Shift> overlappingShifts = shiftRepository.findOverlappingShifts(
                shiftDTO.getEmployeeId(), 
                shiftDTO.getStartTime(), 
                shiftDTO.getEndTime(), 
                shiftDTO.getId());
        
        return !overlappingShifts.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateTotalHoursForEmployee(Long employeeId, LocalDateTime startTime, LocalDateTime endTime) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
        
        Double totalHours = shiftRepository.calculateTotalHoursInRange(employeeId, startTime, endTime);
        return totalHours != null ? totalHours : 0.0;
    }

    /**
     * Validates that shift times are valid (end time after start time)
     * @param shiftDTO The shift data to validate
     */
    private void validateShiftTimes(ShiftDTO shiftDTO) {
        if (shiftDTO.getStartTime() == null || shiftDTO.getEndTime() == null) {
            throw new IllegalArgumentException("Shift start and end times cannot be null");
        }
        
        if (!shiftDTO.getEndTime().isAfter(shiftDTO.getStartTime())) {
            throw new IllegalArgumentException("Shift end time must be after start time");
        }
    }

    /**
     * Validates that the shift doesn't overlap with existing shifts
     * @param shiftDTO The shift data to validate
     */
    private void validateNoOverlaps(ShiftDTO shiftDTO) {
        if (hasOverlappingShifts(shiftDTO)) {
            throw new IllegalStateException("The shift overlaps with existing shifts for the employee");
        }
    }

    /**
     * Maps ShiftDTO to Shift entity
     * @param dto The DTO to map
     * @return The mapped entity
     */
    private Shift mapDtoToEntity(ShiftDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
        
        Schedule schedule = scheduleRepository.findById(dto.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + dto.getScheduleId()));
        
        return Shift.builder()
                .id(dto.getId())
                .employee(employee)
                .schedule(schedule)
                .shiftType(dto.getShiftType())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .notes(dto.getNotes())
                .build();
    }

    /**
     * Maps Shift entity to ShiftDTO
     * @param shift The entity to map
     * @return The mapped DTO
     */
    private ShiftDTO mapEntityToDto(Shift shift) {
        return ShiftDTO.builder()
                .id(shift.getId())
                .employeeId(shift.getEmployee().getId())
                .employeeName(shift.getEmployee().getName().getFullName())
                .scheduleId(shift.getSchedule().getId())
                .shiftType(shift.getShiftType())
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .notes(shift.getNotes())
                .durationInHours(shift.getDurationInHours())
                .createdAt(shift.getCreatedAt())
                .updatedAt(shift.getUpdatedAt())
                .build();
    }

    /**
     * Updates a Shift entity from DTO
     * @param shift The entity to update
     * @param dto The DTO containing updated data
     */
    private void updateShiftFromDto(Shift shift, ShiftDTO dto) {
        if (dto.getEmployeeId() != null && !dto.getEmployeeId().equals(shift.getEmployee().getId())) {
            Employee employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
            shift.setEmployee(employee);
        }
        
        if (dto.getScheduleId() != null && !dto.getScheduleId().equals(shift.getSchedule().getId())) {
            Schedule schedule = scheduleRepository.findById(dto.getScheduleId())
                    .orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + dto.getScheduleId()));
            shift.setSchedule(schedule);
        }
        
        if (dto.getShiftType() != null) {
            shift.setShiftType(dto.getShiftType());
        }
        
        if (dto.getStartTime() != null) {
            shift.setStartTime(dto.getStartTime());
        }
        
        if (dto.getEndTime() != null) {
            shift.setEndTime(dto.getEndTime());
        }
        
        if (dto.getNotes() != null) {
            shift.setNotes(dto.getNotes());
        }
    }

    /**
     * Adds a shift to the employee's schedule
     * @param shift The shift to add
     */
    private void addShiftToSchedule(Shift shift) {
        Schedule schedule = shift.getSchedule();
        schedule.addShift(shift);
        scheduleRepository.save(schedule);
    }

    /**
     * Notifies the employee about a shift
     * @param shift The shift
     * @param title The notification title
     */
    private void notifyEmployeeAboutShift(Shift shift, String title) {
        String message = String.format(
                "%s: %s from %s to %s (%s hours)",
                title,
                shift.getShiftType().getDisplayName(),
                shift.getStartTime().toString(),
                shift.getEndTime().toString(),
                shift.getDurationInHours()
        );
        
        notificationService.createNotification(
                shift.getEmployee().getId(),
                title,
                message
        );
    }
}
