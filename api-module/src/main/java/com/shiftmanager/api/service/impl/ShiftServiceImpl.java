package com.shiftmanager.api.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shiftmanager.api.dto.EmployeeShiftDTO;
import com.shiftmanager.api.dto.ShiftDTO;
import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.mapper.EmployeeShiftMapper;
import com.shiftmanager.api.mapper.ShiftMapper;
import com.shiftmanager.api.model.EmployeeShift;
import com.shiftmanager.api.model.Location;
import com.shiftmanager.api.model.Shift;
import com.shiftmanager.api.model.ShiftType;
import com.shiftmanager.api.repository.EmployeeShiftRepository;
import com.shiftmanager.api.repository.LocationRepository;
import com.shiftmanager.api.repository.ShiftRepository;
import com.shiftmanager.api.repository.ShiftTypeRepository;
import com.shiftmanager.api.service.ShiftService;

@Service
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final LocationRepository locationRepository;
    private final ShiftTypeRepository shiftTypeRepository;
    private final EmployeeShiftRepository employeeShiftRepository;
    private final ShiftMapper shiftMapper;
    private final EmployeeShiftMapper employeeShiftMapper;

    @Autowired
    public ShiftServiceImpl(
            ShiftRepository shiftRepository,
            LocationRepository locationRepository,
            ShiftTypeRepository shiftTypeRepository,
            EmployeeShiftRepository employeeShiftRepository,
            ShiftMapper shiftMapper,
            EmployeeShiftMapper employeeShiftMapper) {
        this.shiftRepository = shiftRepository;
        this.locationRepository = locationRepository;
        this.shiftTypeRepository = shiftTypeRepository;
        this.employeeShiftRepository = employeeShiftRepository;
        this.shiftMapper = shiftMapper;
        this.employeeShiftMapper = employeeShiftMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDTO> getAllShifts(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = startDate.plusDays(30);
        }
        
        return shiftRepository.findByDateRange(startDate, endDate)
                .stream()
                .map(shiftMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftDTO getShiftById(Long id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id: " + id));
        return shiftMapper.toDTO(shift);
    }

    @Override
    @Transactional
    public ShiftDTO createShift(ShiftDTO shiftDTO) {
        Shift shift = new Shift();
        shift.setShiftDate(shiftDTO.getShiftDate());
        shift.setStartTime(shiftDTO.getStartTime());
        shift.setEndTime(shiftDTO.getEndTime());
        shift.setNote(shiftDTO.getNote());
        
        // Set location
        Location location = locationRepository.findById(shiftDTO.getLocation().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + shiftDTO.getLocation().getId()));
        shift.setLocation(location);
        
        // Set shift type
        ShiftType shiftType = shiftTypeRepository.findById(shiftDTO.getShiftType().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift type not found with id: " + shiftDTO.getShiftType().getId()));
        shift.setShiftType(shiftType);
        
        Shift savedShift = shiftRepository.save(shift);
        return shiftMapper.toDTO(savedShift);
    }

    @Override
    @Transactional
    public ShiftDTO updateShift(Long id, ShiftDTO shiftDTO) {
        Shift existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with id: " + id));
        
        existingShift.setShiftDate(shiftDTO.getShiftDate());
        existingShift.setStartTime(shiftDTO.getStartTime());
        existingShift.setEndTime(shiftDTO.getEndTime());
        existingShift.setNote(shiftDTO.getNote());
        
        // Update location if changed
        if (!existingShift.getLocation().getId().equals(shiftDTO.getLocation().getId())) {
            Location location = locationRepository.findById(shiftDTO.getLocation().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + shiftDTO.getLocation().getId()));
            existingShift.setLocation(location);
        }
        
        // Update shift type if changed
        if (!existingShift.getShiftType().getId().equals(shiftDTO.getShiftType().getId())) {
            ShiftType shiftType = shiftTypeRepository.findById(shiftDTO.getShiftType().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Shift type not found with id: " + shiftDTO.getShiftType().getId()));
            existingShift.setShiftType(shiftType);
        }
        
        Shift updatedShift = shiftRepository.save(existingShift);
        return shiftMapper.toDTO(updatedShift);
    }

    @Override
    @Transactional
    public void deleteShift(Long id) {
        if (!shiftRepository.existsById(id)) {
            throw new ResourceNotFoundException("Shift not found with id: " + id);
        }
        shiftRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDTO> getShiftsByLocation(Long locationId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = startDate.plusDays(30);
        }
        
        return shiftRepository.findByLocationAndDateRange(locationId, startDate, endDate)
                .stream()
                .map(shiftMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftDTO> getShiftsByShiftType(Long shiftTypeId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        if (endDate == null) {
            endDate = startDate.plusDays(30);
        }
        
        return shiftRepository.findByShiftTypeAndDateRange(shiftTypeId, startDate, endDate)
                .stream()
                .map(shiftMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public EmployeeShiftDTO updateClockOutTime(Long employeeShiftId, LocalTime clockOutTime) {
        // Verify employee shift exists
        EmployeeShift employeeShift = employeeShiftRepository.findById(employeeShiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee shift not found with ID: " + employeeShiftId));
        
        // Update clock out time
        employeeShift.setClockOutTime(clockOutTime);
        
        // Save updated employee shift
        employeeShift = employeeShiftRepository.save(employeeShift);
        
        return employeeShiftMapper.toDTO(employeeShift);
    }

    @Override
    public List<Shift> getEmployeeShifts(Long employeeId, LocalDate startDate, LocalDate endDate) {
        // Implementation will be added in future iterations
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public List<EmployeeShiftDTO> getEmployeeShiftAssignments(Long employeeId, LocalDate startDate, LocalDate endDate) {
        // Implementation will be added in future iterations
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public List<EmployeeShiftDTO> getMyShifts(LocalDate startDate, LocalDate endDate) {
        // Implementation will be added in future iterations
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public boolean assignShift(Long shiftId, Long employeeId) {
        // Implementation will be added in future iterations
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public EmployeeShiftDTO confirmShift(Long employeeShiftId) {
        // Implementation will be added in future iterations
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public EmployeeShiftDTO declineShift(Long employeeShiftId, String reason) {
        // Implementation will be added in future iterations
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public EmployeeShiftDTO clockIn(Long employeeShiftId) {
        // Implementation will be added in future iterations
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public EmployeeShiftDTO clockOut(Long employeeShiftId) {
        // Implementation will be added in future iterations
        throw new UnsupportedOperationException("Method not yet implemented");
    }

    @Override
    public EmployeeShiftDTO updateClockInTime(Long employeeShiftId, LocalTime clockInTime) {
        // Implementation will be added in future iterations
        throw new UnsupportedOperationException("Method not yet implemented");
    }
}
