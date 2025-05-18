package com.shiftmanager.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shiftmanager.api.dto.ShiftTypeDTO;
import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.mapper.ShiftTypeMapper;
import com.shiftmanager.api.model.ShiftType;
import com.shiftmanager.api.repository.ShiftTypeRepository;
import com.shiftmanager.api.service.ShiftTypeService;

@Service
@AllArgsConstructor
public class ShiftTypeServiceImpl implements ShiftTypeService {

    private final ShiftTypeRepository shiftTypeRepository;
    private final ShiftTypeMapper shiftTypeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ShiftTypeDTO> getAllShiftTypes() {
        return shiftTypeRepository.findAll()
                .stream()
                .map(shiftTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftTypeDTO getShiftTypeById(Long id) {
        ShiftType shiftType = shiftTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift type not found with id: " + id));
        return shiftTypeMapper.toDTO(shiftType);
    }

    @Override
    @Transactional
    public ShiftTypeDTO createShiftType(ShiftTypeDTO shiftTypeDTO) {
        // Check if shift type with same name already exists
        shiftTypeRepository.findByName(shiftTypeDTO.getName())
                .ifPresent(st -> {
                    throw new IllegalArgumentException("Shift type with name " + shiftTypeDTO.getName() + " already exists");
                });
        
        ShiftType shiftType = shiftTypeMapper.toEntity(shiftTypeDTO);
        ShiftType savedShiftType = shiftTypeRepository.save(shiftType);
        return shiftTypeMapper.toDTO(savedShiftType);
    }

    @Override
    @Transactional
    public ShiftTypeDTO updateShiftType(Long id, ShiftTypeDTO shiftTypeDTO) {
        ShiftType existingShiftType = shiftTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift type not found with id: " + id));
        
        // Check name uniqueness if changed
        if (!existingShiftType.getName().equals(shiftTypeDTO.getName())) {
            shiftTypeRepository.findByName(shiftTypeDTO.getName())
                    .ifPresent(st -> {
                        throw new IllegalArgumentException("Shift type with name " + shiftTypeDTO.getName() + " already exists");
                    });
        }
        
        shiftTypeMapper.updateEntityFromDTO(shiftTypeDTO, existingShiftType);
        ShiftType updatedShiftType = shiftTypeRepository.save(existingShiftType);
        return shiftTypeMapper.toDTO(updatedShiftType);
    }

    @Override
    @Transactional
    public void deleteShiftType(Long id) {
        if (!shiftTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Shift type not found with id: " + id);
        }
        shiftTypeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftTypeDTO> getActiveShiftTypes() {
        return shiftTypeRepository.findByActiveTrue()
                .stream()
                .map(shiftTypeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
