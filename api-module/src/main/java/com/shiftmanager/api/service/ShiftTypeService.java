package com.shiftmanager.api.service;

import java.util.List;

import com.shiftmanager.api.dto.ShiftTypeDTO;

public interface ShiftTypeService {
    
    List<ShiftTypeDTO> getAllShiftTypes();
    
    ShiftTypeDTO getShiftTypeById(Long id);
    
    ShiftTypeDTO createShiftType(ShiftTypeDTO shiftTypeDTO);
    
    ShiftTypeDTO updateShiftType(Long id, ShiftTypeDTO shiftTypeDTO);
    
    void deleteShiftType(Long id);
    
    List<ShiftTypeDTO> getActiveShiftTypes();
}
