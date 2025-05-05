package com.shiftmanager.api.service.impl;

import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.exception.ValidationErrorResponse;
import com.shiftmanager.api.model.*;
import com.shiftmanager.api.repository.EmployeeRepository;
import com.shiftmanager.api.repository.EmployeeShiftRepository;
import com.shiftmanager.api.repository.ShiftRepository;
import com.shiftmanager.api.repository.VacationRequestRepository;
import com.shiftmanager.api.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private EmployeeShiftRepository employeeShiftRepository;

    @Mock
    private VacationRequestRepository vacationRequestRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ManagerServiceImpl managerService;

    private Employee employee;
    private Manager manager;
    private Shift shift;
    private Location location;
    private ShiftType shiftType;
    private EmployeeShift employeeShift;
    private VacationRequest vacationRequest;

    @BeforeEach
    void setUp() {
        // Initialize test data
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setEmail("john.doe@example.com");
        person.setPhoneNumber("123-456-7890");
        
        employee = new Employee();
        employee.setId(2L);
        employee.setFirstName("Jane");
        employee.setLastName("Smith");
        employee.setEmail("jane.smith@example.com");
        employee.setPhoneNumber("987-654-3210");
        
        manager = new Manager();
        manager.setId(3L);
        manager.setFirstName("Michael");
        manager.setLastName("Johnson");
        manager.setEmail("michael.johnson@example.com");
        manager.setPhoneNumber("555-123-4567");
        
        // Set manager for employee
        employee.setManager(manager);
        
        // Initialize location
        location = new Location();
        location.setId(1L);
        location.setName("Main Office");
        location.setAddress("123 Main St");
        location.setCity("Metropolis");
        location.setState("NY");
        location.setZipCode("10001");
        
        // Initialize shift type
        shiftType = new ShiftType();
        shiftType.setId(1L);
        shiftType.setName("Day Shift");
        shiftType.setDescription("Standard day shift");
        
        // Initialize shift
        shift = new Shift();
        shift.setId(1L);
        shift.setShiftDate(LocalDate.now());
        shift.setStartTime(LocalTime.of(9, 0));
        shift.setEndTime(LocalTime.of(17, 0));
        shift.setLocation(location);
        shift.setShiftType(shiftType);
        shift.setCreatedById(manager.getId());
        
        // Initialize employee shift
        employeeShift = new EmployeeShift();
        employeeShift.setId(1L);
        employeeShift.setEmployee(employee);
        employeeShift.setShift(shift);
        employeeShift.setAssignedBy(manager);
        employeeShift.setAssignedAt(LocalDateTime.now());
        employeeShift.setStatus("ASSIGNED");
        
        // Initialize vacation request
        vacationRequest = new VacationRequest();
        vacationRequest.setId(1L);
        vacationRequest.setEmployee(employee);
        vacationRequest.setStartDate(LocalDate.now().plusDays(10));
        vacationRequest.setEndDate(LocalDate.now().plusDays(15));
        vacationRequest.setRequestDate(LocalDateTime.now());
        vacationRequest.setReason("Annual vacation");
        vacationRequest.setStatus("PENDING");
    }

    @Test
    void assignShiftToEmployee_Success() {
        // Arrange
        when(employeeRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(shiftRepository.findById(shift.getId())).thenReturn(Optional.of(shift));
        when(employeeShiftRepository.findByEmployeeAndShift(employee, shift)).thenReturn(Optional.empty());
        when(employeeShiftRepository.findConflictingShifts(employee.getId(), shift.getShiftDate(), 
                shift.getStartTime(), shift.getEndTime())).thenReturn(new ArrayList<>());
        
        // Act
        boolean result = managerService.assignShiftToEmployee(shift.getId(), employee.getId(), manager.getId());
        
        // Assert
        assertTrue(result);
        verify(employeeShiftRepository).save(any(EmployeeShift.class));
        verify(notificationService).sendShiftAssignmentNotification(employee.getId(), shift.getId());
    }

    @Test
    void assignShiftToEmployee_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
                managerService.assignShiftToEmployee(shift.getId(), employee.getId(), manager.getId()));
    }

    @Test
    void assignShiftToEmployee_NotAuthorized() {
        // Arrange
        Manager otherManager = new Manager();
        otherManager.setId(4L);
        employee.setManager(otherManager);
        
        when(employeeRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        
        // Act & Assert
        assertThrows(ValidationErrorResponse.class, () -> 
                managerService.assignShiftToEmployee(shift.getId(), employee.getId(), manager.getId()));
    }

    @Test
    void assignShiftToEmployee_AlreadyAssigned() {
        // Arrange
        when(employeeRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(shiftRepository.findById(shift.getId())).thenReturn(Optional.of(shift));
        when(employeeShiftRepository.findByEmployeeAndShift(employee, shift)).thenReturn(Optional.of(employeeShift));
        
        // Act & Assert
        assertThrows(ValidationErrorResponse.class, () -> 
                managerService.assignShiftToEmployee(shift.getId(), employee.getId(), manager.getId()));
    }

    @Test
    void assignShiftToEmployee_ConflictingShift() {
        // Arrange
        List<EmployeeShift> conflictingShifts = new ArrayList<>();
        conflictingShifts.add(employeeShift);
        
        when(employeeRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(shiftRepository.findById(shift.getId())).thenReturn(Optional.of(shift));
        when(employeeShiftRepository.findByEmployeeAndShift(employee, shift)).thenReturn(Optional.empty());
        when(employeeShiftRepository.findConflictingShifts(employee.getId(), shift.getShiftDate(), 
                shift.getStartTime(), shift.getEndTime())).thenReturn(conflictingShifts);
        
        // Act & Assert
        assertThrows(ValidationErrorResponse.class, () -> 
                managerService.assignShiftToEmployee(shift.getId(), employee.getId(), manager.getId()));
    }

    @Test
    void approveVacationRequest_Success() {
        // Arrange
        when(employeeRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(vacationRequestRepository.findById(vacationRequest.getId())).thenReturn(Optional.of(vacationRequest));
        when(vacationRequestRepository.findOverlappingApprovedRequests(employee.getId(), 
                vacationRequest.getStartDate(), vacationRequest.getEndDate())).thenReturn(new ArrayList<>());
        
        // Act
        VacationRequest result = managerService.approveVacationRequest(vacationRequest.getId(), manager.getId(), "Approved");
        
        // Assert
        assertEquals("APPROVED", result.getStatus());
        assertEquals(manager.getId(), result.getReviewedBy());
        assertEquals("Approved", result.getReviewNotes());
        verify(vacationRequestRepository).save(vacationRequest);
        verify(notificationService).sendVacationRequestStatusNotification(employee.getId(), vacationRequest.getId(), true);
    }

    @Test
    void getTeamMembers_Success() {
        // Arrange
        List<Employee> subordinates = new ArrayList<>();
        subordinates.add(employee);
        
        when(employeeRepository.findById(manager.getId())).thenReturn(Optional.of(manager));
        when(employeeRepository.findByManagerId(manager.getId())).thenReturn(subordinates);
        
        // Act
        List<Employee> result = managerService.getTeamMembers(manager.getId());
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(employee.getId(), result.get(0).getId());
    }
}