package com.shiftmanager.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shiftmanager.api.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByEmail(String email);
    
    Optional<Employee> findByEmployeeNumber(String employeeNumber);
    
    @Query("SELECT e FROM Employee e WHERE e.manager.id = :managerId")
    List<Employee> findByManagerId(@Param("managerId") Long managerId);
    
    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(e.employeeNumber) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Employee> searchEmployees(@Param("query") String query);
}
