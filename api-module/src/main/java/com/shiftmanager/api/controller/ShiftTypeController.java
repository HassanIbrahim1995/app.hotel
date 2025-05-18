package com.shiftmanager.api.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.shiftmanager.api.dto.ShiftTypeDTO;
import com.shiftmanager.api.service.ShiftTypeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shift-types")
@AllArgsConstructor
public class ShiftTypeController {
    
    private final ShiftTypeService shiftTypeService;
    
    @GetMapping
    public ResponseEntity<List<ShiftTypeDTO>> getAllShiftTypes() {
        List<ShiftTypeDTO> shiftTypes = shiftTypeService.getAllShiftTypes();
        return ResponseEntity.ok(shiftTypes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ShiftTypeDTO> getShiftTypeById(@PathVariable Long id) {
        ShiftTypeDTO shiftType = shiftTypeService.getShiftTypeById(id);
        return ResponseEntity.ok(shiftType);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShiftTypeDTO> createShiftType(@Valid @RequestBody ShiftTypeDTO shiftTypeDTO) {
        ShiftTypeDTO createdShiftType = shiftTypeService.createShiftType(shiftTypeDTO);
        return new ResponseEntity<>(createdShiftType, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShiftTypeDTO> updateShiftType(
            @PathVariable Long id, 
            @Valid @RequestBody ShiftTypeDTO shiftTypeDTO) {
        ShiftTypeDTO updatedShiftType = shiftTypeService.updateShiftType(id, shiftTypeDTO);
        return ResponseEntity.ok(updatedShiftType);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteShiftType(@PathVariable Long id) {
        shiftTypeService.deleteShiftType(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ShiftTypeDTO>> getActiveShiftTypes() {
        List<ShiftTypeDTO> shiftTypes = shiftTypeService.getActiveShiftTypes();
        return ResponseEntity.ok(shiftTypes);
    }
}
