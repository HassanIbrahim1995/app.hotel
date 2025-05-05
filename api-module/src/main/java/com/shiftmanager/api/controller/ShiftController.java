package com.shiftmanager.api.controller;

import com.shiftmanager.api.dto.EmployeeShiftDTO;
import com.shiftmanager.api.dto.ShiftDTO;
import com.shiftmanager.api.dto.ShiftTypeDTO;
import com.shiftmanager.api.mapper.EmployeeShiftMapper;
import com.shiftmanager.api.mapper.ShiftMapper;
import com.shiftmanager.api.model.EmployeeShift;
import com.shiftmanager.api.model.Shift;
import com.shiftmanager.api.model.ShiftType;
import com.shiftmanager.api.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for shift operations
 */
@RestController
@RequestMapping("/api/shifts")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private ShiftMapper shiftMapper;

    @Autowired
    private EmployeeShiftMapper employeeShiftMapper;

    /**
     * Get all shifts
     * @param startDate Start date
     * @param endDate End date
     * @return List of shifts
     */
    @GetMapping
    public ResponseEntity<List<ShiftDTO>> getAllShifts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Shift> shifts = shiftService.getAllShifts(startDate, endDate);
        List<ShiftDTO> shiftDTOs = shifts.stream()
                .map(shiftMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(shiftDTOs);
    }

    /**
     * Get shift by ID
     * @param id Shift ID
     * @return Shift
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShiftDTO> getShiftById(@PathVariable Long id) {
        Shift shift = shiftService.getShiftById(id);
        ShiftDTO shiftDTO = shiftMapper.toDto(shift);

        return ResponseEntity.ok(shiftDTO);
    }

    /**
     * Get all shift types
     * @return List of shift types
     */
    @GetMapping("/types")
    public ResponseEntity<List<ShiftTypeDTO>> getAllShiftTypes() {
        List<ShiftType> shiftTypes = shiftService.getAllShiftTypes();
        List<ShiftTypeDTO> shiftTypeDTOs = shiftTypes.stream()
                .map(shiftType -> {
                    ShiftTypeDTO dto = new ShiftTypeDTO();
                    dto.setId(shiftType.getId());
                    dto.setName(shiftType.getName());
                    dto.setDescription(shiftType.getDescription());
                    dto.setDefaultStartTime(shiftType.getDefaultStartTime());
                    dto.setDefaultEndTime(shiftType.getDefaultEndTime());
                    dto.setColor(shiftType.getColor());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(shiftTypeDTOs);
    }

    /**
     * Get shift type by ID
     * @param id Shift type ID
     * @return Shift type
     */
    @GetMapping("/types/{id}")
    public ResponseEntity<ShiftTypeDTO> getShiftTypeById(@PathVariable Long id) {
        ShiftType shiftType = shiftService.getShiftTypeById(id);
        
        ShiftTypeDTO shiftTypeDTO = new ShiftTypeDTO();
        shiftTypeDTO.setId(shiftType.getId());
        shiftTypeDTO.setName(shiftType.getName());
        shiftTypeDTO.setDescription(shiftType.getDescription());
        shiftTypeDTO.setDefaultStartTime(shiftType.getDefaultStartTime());
        shiftTypeDTO.setDefaultEndTime(shiftType.getDefaultEndTime());
        shiftTypeDTO.setColor(shiftType.getColor());

        return ResponseEntity.ok(shiftTypeDTO);
    }

    /**
     * Create shift type (admin only)
     * @param shiftTypeDTO Shift type data
     * @return Created shift type
     */
    @PostMapping("/types")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ShiftTypeDTO> createShiftType(@Valid @RequestBody ShiftTypeDTO shiftTypeDTO) {
        ShiftType shiftType = new ShiftType();
        shiftType.setName(shiftTypeDTO.getName());
        shiftType.setDescription(shiftTypeDTO.getDescription());
        shiftType.setDefaultStartTime(shiftTypeDTO.getDefaultStartTime());
        shiftType.setDefaultEndTime(shiftTypeDTO.getDefaultEndTime());
        shiftType.setColor(shiftTypeDTO.getColor());

        ShiftType createdShiftType = shiftService.createShiftType(shiftType);
        
        ShiftTypeDTO createdShiftTypeDTO = new ShiftTypeDTO();
        createdShiftTypeDTO.setId(createdShiftType.getId());
        createdShiftTypeDTO.setName(createdShiftType.getName());
        createdShiftTypeDTO.setDescription(createdShiftType.getDescription());
        createdShiftTypeDTO.setDefaultStartTime(createdShiftType.getDefaultStartTime());
        createdShiftTypeDTO.setDefaultEndTime(createdShiftType.getDefaultEndTime());
        createdShiftTypeDTO.setColor(createdShiftType.getColor());

        return ResponseEntity.ok(createdShiftTypeDTO);
    }

    /**
     * Update shift type (admin only)
     * @param id Shift type ID
     * @param shiftTypeDTO Shift type data
     * @return Updated shift type
     */
    @PutMapping("/types/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ShiftTypeDTO> updateShiftType(
            @PathVariable Long id,
            @Valid @RequestBody ShiftTypeDTO shiftTypeDTO) {

        ShiftType shiftType = new ShiftType();
        shiftType.setName(shiftTypeDTO.getName());
        shiftType.setDescription(shiftTypeDTO.getDescription());
        shiftType.setDefaultStartTime(shiftTypeDTO.getDefaultStartTime());
        shiftType.setDefaultEndTime(shiftTypeDTO.getDefaultEndTime());
        shiftType.setColor(shiftTypeDTO.getColor());

        ShiftType updatedShiftType = shiftService.updateShiftType(id, shiftType);
        
        ShiftTypeDTO updatedShiftTypeDTO = new ShiftTypeDTO();
        updatedShiftTypeDTO.setId(updatedShiftType.getId());
        updatedShiftTypeDTO.setName(updatedShiftType.getName());
        updatedShiftTypeDTO.setDescription(updatedShiftType.getDescription());
        updatedShiftTypeDTO.setDefaultStartTime(updatedShiftType.getDefaultStartTime());
        updatedShiftTypeDTO.setDefaultEndTime(updatedShiftType.getDefaultEndTime());
        updatedShiftTypeDTO.setColor(updatedShiftType.getColor());

        return ResponseEntity.ok(updatedShiftTypeDTO);
    }

    /**
     * Delete shift type (admin only)
     * @param id Shift type ID
     * @return Delete status
     */
    @DeleteMapping("/types/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteShiftType(@PathVariable Long id) {
        shiftService.deleteShiftType(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return ResponseEntity.ok(response);
    }

    /**
     * Get all employee shifts for a shift
     * @param id Shift ID
     * @return List of employee shifts
     */
    @GetMapping("/{id}/employees")
    public ResponseEntity<List<EmployeeShiftDTO>> getEmployeesForShift(@PathVariable Long id) {
        List<EmployeeShift> employeeShifts = shiftService.getEmployeesForShift(id);
        List<EmployeeShiftDTO> employeeShiftDTOs = employeeShifts.stream()
                .map(employeeShiftMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(employeeShiftDTOs);
    }
}