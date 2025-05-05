package com.shiftmanager.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shiftmanager.api.dto.ShiftDTO;
import com.shiftmanager.api.service.ShiftService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {
    
    private final ShiftService shiftService;
    
    @Autowired
    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }
    
    @GetMapping
    public ResponseEntity<List<ShiftDTO>> getAllShifts(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ShiftDTO> shifts = shiftService.getAllShifts(startDate, endDate);
        return ResponseEntity.ok(shifts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ShiftDTO> getShiftById(@PathVariable Long id) {
        ShiftDTO shift = shiftService.getShiftById(id);
        return ResponseEntity.ok(shift);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ShiftDTO> createShift(@Valid @RequestBody ShiftDTO shiftDTO) {
        ShiftDTO createdShift = shiftService.createShift(shiftDTO);
        return new ResponseEntity<>(createdShift, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ShiftDTO> updateShift(
            @PathVariable Long id, 
            @Valid @RequestBody ShiftDTO shiftDTO) {
        ShiftDTO updatedShift = shiftService.updateShift(id, shiftDTO);
        return ResponseEntity.ok(updatedShift);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<ShiftDTO>> getShiftsByLocation(
            @PathVariable Long locationId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ShiftDTO> shifts = shiftService.getShiftsByLocation(locationId, startDate, endDate);
        return ResponseEntity.ok(shifts);
    }
    
    @GetMapping("/shift-type/{shiftTypeId}")
    public ResponseEntity<List<ShiftDTO>> getShiftsByShiftType(
            @PathVariable Long shiftTypeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ShiftDTO> shifts = shiftService.getShiftsByShiftType(shiftTypeId, startDate, endDate);
        return ResponseEntity.ok(shifts);
    }
}
