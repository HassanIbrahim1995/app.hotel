package com.shiftmanager.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shiftmanager.api.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    Optional<Location> findByName(String name);
    
    List<Location> findByActiveTrue();
}
