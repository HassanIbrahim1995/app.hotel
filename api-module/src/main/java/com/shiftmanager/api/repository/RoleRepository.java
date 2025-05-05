package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Role entity
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Find a role by name
     * @param name Role name
     * @return Optional role
     */
    Optional<Role> findByName(String name);
}