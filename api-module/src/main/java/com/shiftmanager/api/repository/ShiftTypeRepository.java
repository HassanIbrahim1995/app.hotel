package com.shiftmanager.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shiftmanager.api.model.ShiftType;

@Repository
public interface ShiftTypeRepository extends JpaRepository<ShiftType, Long> {
    
    Optional<ShiftType> findByName(String name);
    
    List<ShiftType> findByActiveTrue();
}
