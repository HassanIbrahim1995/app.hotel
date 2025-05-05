package com.shiftmanager.api.service.impl;

import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.model.*;
import com.shiftmanager.api.repository.*;
import com.shiftmanager.api.service.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CalendarService
 */
@Service
@Transactional
public class CalendarServiceImpl implements CalendarService {

    private static final Logger logger = LoggerFactory.getLogger(CalendarServiceImpl.class);

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private CalendarEntryRepository calendarEntryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeShiftRepository employeeShiftRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @Override
    public Calendar getCalendarById(Long calendarId) {
        logger.debug("Getting calendar with ID: {}", calendarId);
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar not found with ID: " + calendarId));
    }

    @Override
    public Calendar getCalendarByEmployeeId(Long employeeId) {
        logger.debug("Getting calendar for employee ID: {}", employeeId);
        return calendarRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Calendar not found for employee with ID: " + employeeId));
    }

    @Override
    public Calendar getOrCreateCalendarForEmployee(Long employeeId) {
        logger.debug("Getting or creating calendar for employee ID: {}", employeeId);
        
        // Try to find existing calendar
        return calendarRepository.findByEmployeeId(employeeId)
                .orElseGet(() -> {
                    // Create new calendar if not found
                    Employee employee = employeeRepository.findById(employeeId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Employee not found with ID: " + employeeId));
                    
                    Calendar calendar = new Calendar();
                    calendar.setEmployee(employee);
                    calendar.setYear(LocalDate.now().getYear());
                    calendar.setMonth(LocalDate.now().getMonthValue());
                    
                    return calendarRepository.save(calendar);
                });
    }

    @Override
    public List<CalendarEntry> getCalendarEntries(Long calendarId) {
        logger.debug("Getting all entries for calendar ID: {}", calendarId);
        // Verify calendar exists
        if (!calendarRepository.existsById(calendarId)) {
            throw new ResourceNotFoundException("Calendar not found with ID: " + calendarId);
        }
        return calendarEntryRepository.findByCalendarId(calendarId);
    }

    @Override
    public List<CalendarEntry> getCalendarEntriesForDate(Long calendarId, LocalDate date) {
        logger.debug("Getting entries for calendar ID: {} and date: {}", calendarId, date);
        // Verify calendar exists
        if (!calendarRepository.existsById(calendarId)) {
            throw new ResourceNotFoundException("Calendar not found with ID: " + calendarId);
        }
        return calendarEntryRepository.findByCalendarIdAndEntryDate(calendarId, date);
    }

    @Override
    public List<CalendarEntry> getCalendarEntriesForDateRange(Long calendarId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting entries for calendar ID: {} between {} and {}", calendarId, startDate, endDate);
        // Verify calendar exists
        if (!calendarRepository.existsById(calendarId)) {
            throw new ResourceNotFoundException("Calendar not found with ID: " + calendarId);
        }
        return calendarEntryRepository.findByCalendarIdAndDateRange(calendarId, startDate, endDate);
    }

    @Override
    public List<CalendarEntry> getCalendarEntriesByType(Long calendarId, String entryType) {
        logger.debug("Getting entries for calendar ID: {} with type: {}", calendarId, entryType);
        // Verify calendar exists
        if (!calendarRepository.existsById(calendarId)) {
            throw new ResourceNotFoundException("Calendar not found with ID: " + calendarId);
        }
        return calendarEntryRepository.findByCalendarIdAndEntryType(calendarId, entryType);
    }

    @Override
    public CalendarEntry addCalendarEntry(Long calendarId, CalendarEntry entry) {
        logger.debug("Adding entry to calendar ID: {}", calendarId);
        
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar not found with ID: " + calendarId));
        
        entry.setCalendar(calendar);
        return calendarEntryRepository.save(entry);
    }

    @Override
    public CalendarEntry updateCalendarEntry(Long entryId, CalendarEntry updatedEntry) {
        logger.debug("Updating calendar entry ID: {}", entryId);
        
        CalendarEntry existingEntry = calendarEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar entry not found with ID: " + entryId));
        
        // Update fields but preserve relationships
        existingEntry.setEntryDate(updatedEntry.getEntryDate());
        existingEntry.setStartTime(updatedEntry.getStartTime());
        existingEntry.setEndTime(updatedEntry.getEndTime());
        existingEntry.setEntryType(updatedEntry.getEntryType());
        existingEntry.setTitle(updatedEntry.getTitle());
        existingEntry.setDescription(updatedEntry.getDescription());
        existingEntry.setAllDay(updatedEntry.isAllDay());
        existingEntry.setColor(updatedEntry.getColor());
        
        return calendarEntryRepository.save(existingEntry);
    }

    @Override
    public boolean deleteCalendarEntry(Long entryId) {
        logger.debug("Deleting calendar entry ID: {}", entryId);
        
        if (!calendarEntryRepository.existsById(entryId)) {
            throw new ResourceNotFoundException("Calendar entry not found with ID: " + entryId);
        }
        
        calendarEntryRepository.deleteById(entryId);
        return true;
    }

    @Override
    public int syncEmployeeShiftsToCalendar(Long employeeId) {
        logger.debug("Syncing shifts to calendar for employee ID: {}", employeeId);
        
        Calendar calendar = getOrCreateCalendarForEmployee(employeeId);
        Employee employee = calendar.getEmployee();
        
        // Get all employee shifts
        List<EmployeeShift> employeeShifts = employeeShiftRepository.findByEmployee(employee);
        
        int entriesCreated = 0;
        
        for (EmployeeShift employeeShift : employeeShifts) {
            Shift shift = employeeShift.getShift();
            
            // Check if we already have an entry for this shift
            List<CalendarEntry> existingEntries = calendarEntryRepository
                    .findByEntryTypeAndReferenceId("SHIFT", shift.getId());
            
            if (existingEntries.isEmpty()) {
                // Create new calendar entry
                CalendarEntry entry = new CalendarEntry(calendar, shift);
                calendarEntryRepository.save(entry);
                entriesCreated++;
            }
        }
        
        logger.info("Created {} new calendar entries for employee ID: {}", entriesCreated, employeeId);
        return entriesCreated;
    }

    @Override
    public int syncEmployeeVacationRequestsToCalendar(Long employeeId) {
        logger.debug("Syncing vacation requests to calendar for employee ID: {}", employeeId);
        
        Calendar calendar = getOrCreateCalendarForEmployee(employeeId);
        Employee employee = calendar.getEmployee();
        
        // Get all approved vacation requests
        List<VacationRequest> vacationRequests = vacationRequestRepository
                .findByEmployeeAndStatus(employee, "APPROVED");
        
        int entriesCreated = 0;
        
        for (VacationRequest vacationRequest : vacationRequests) {
            // Check if we already have an entry for this vacation request
            List<CalendarEntry> existingEntries = calendarEntryRepository
                    .findByEntryTypeAndReferenceId("VACATION", vacationRequest.getId());
            
            if (existingEntries.isEmpty()) {
                // Create entries for each day in the vacation request range
                LocalDate currentDate = vacationRequest.getStartDate();
                while (!currentDate.isAfter(vacationRequest.getEndDate())) {
                    CalendarEntry entry = new CalendarEntry(
                            calendar,
                            currentDate,
                            null,
                            null,
                            "VACATION",
                            "Vacation",
                            vacationRequest.getReason(),
                            true,
                            vacationRequest.getId(),
                            "#F44336" // Red
                    );
                    calendarEntryRepository.save(entry);
                    entriesCreated++;
                    currentDate = currentDate.plusDays(1);
                }
            }
        }
        
        logger.info("Created {} new vacation calendar entries for employee ID: {}", entriesCreated, employeeId);
        return entriesCreated;
    }

    @Override
    public List<Calendar> getTeamCalendars(Long managerId) {
        logger.debug("Getting team calendars for manager ID: {}", managerId);
        
        // Verify the manager exists
        if (!employeeRepository.existsById(managerId)) {
            throw new ResourceNotFoundException("Manager not found with ID: " + managerId);
        }
        
        return calendarRepository.findByManagerId(managerId);
    }
    
    @Override
    public List<Calendar> getAllCalendars() {
        logger.debug("Getting all calendars");
        return calendarRepository.findAll();
    }
    
    @Override
    public CalendarEntry createCalendarEntry(Long calendarId, CalendarEntry entry) {
        logger.debug("Creating calendar entry for calendar ID: {}", calendarId);
        
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar not found with ID: " + calendarId));
        
        entry.setCalendar(calendar);
        return calendarEntryRepository.save(entry);
    }
    
    @Override
    public CalendarEntry updateCalendarEntry(Long calendarId, Long entryId, CalendarEntry entry) {
        logger.debug("Updating entry ID: {} in calendar ID: {}", entryId, calendarId);
        
        // Verify calendar exists
        if (!calendarRepository.existsById(calendarId)) {
            throw new ResourceNotFoundException("Calendar not found with ID: " + calendarId);
        }
        
        CalendarEntry existingEntry = calendarEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar entry not found with ID: " + entryId));
        
        // Verify entry belongs to the specified calendar
        if (!existingEntry.getCalendar().getId().equals(calendarId)) {
            throw new IllegalArgumentException("Entry ID: " + entryId + " does not belong to calendar ID: " + calendarId);
        }
        
        // Update fields preserving relationships
        existingEntry.setEntryDate(entry.getEntryDate());
        existingEntry.setStartTime(entry.getStartTime());
        existingEntry.setEndTime(entry.getEndTime());
        existingEntry.setEntryType(entry.getEntryType());
        existingEntry.setTitle(entry.getTitle());
        existingEntry.setDescription(entry.getDescription());
        existingEntry.setAllDay(entry.isAllDay());
        existingEntry.setColor(entry.getColor());
        
        return calendarEntryRepository.save(existingEntry);
    }
    
    @Override
    public boolean deleteCalendarEntry(Long calendarId, Long entryId) {
        logger.debug("Deleting entry ID: {} from calendar ID: {}", entryId, calendarId);
        
        // Verify calendar exists
        if (!calendarRepository.existsById(calendarId)) {
            throw new ResourceNotFoundException("Calendar not found with ID: " + calendarId);
        }
        
        CalendarEntry existingEntry = calendarEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar entry not found with ID: " + entryId));
        
        // Verify entry belongs to the specified calendar
        if (!existingEntry.getCalendar().getId().equals(calendarId)) {
            throw new IllegalArgumentException("Entry ID: " + entryId + " does not belong to calendar ID: " + calendarId);
        }
        
        calendarEntryRepository.delete(existingEntry);
        return true;
    }
    
    @Override
    public List<CalendarEntry> getCalendarEntries(Long calendarId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting entries for calendar ID: {} between {} and {}", calendarId, startDate, endDate);
        
        // Verify calendar exists
        if (!calendarRepository.existsById(calendarId)) {
            throw new ResourceNotFoundException("Calendar not found with ID: " + calendarId);
        }
        
        return calendarEntryRepository.findByCalendarIdAndDateRange(calendarId, startDate, endDate);
    }
    
    @Override
    public byte[] exportCalendarToPdf(Long employeeId, Integer month, Integer year) {
        logger.debug("Exporting calendar to PDF for employee ID: {}, month: {}, year: {}", employeeId, month, year);
        
        // Get employee calendar
        Calendar calendar = getOrCreateCalendarForEmployee(employeeId);
        
        // Get start and end dates for the month
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        // Get all entries for the month
        List<CalendarEntry> entries = getCalendarEntriesForDateRange(calendar.getId(), startDate, endDate);
        
        // TODO: Generate PDF with the entries
        // This would typically be handled by a PDF generation library like iText or PDFBox
        // For now, just return a placeholder byte array
        String content = "Calendar for " + calendar.getEmployee().getFirstName() + " " + 
                calendar.getEmployee().getLastName() + "\n";
        content += "Month: " + month + ", Year: " + year + "\n\n";
        
        for (CalendarEntry entry : entries) {
            content += entry.getEntryDate() + ": " + entry.getTitle() + "\n";
        }
        
        return content.getBytes();
    }
    
    @Override
    public List<CalendarEntry> getEmployeeCalendar(Long employeeId, Integer month, Integer year) {
        logger.debug("Getting calendar for employee ID: {}, month: {}, year: {}", employeeId, month, year);
        
        // Get employee calendar
        Calendar calendar = getOrCreateCalendarForEmployee(employeeId);
        
        // Get start and end dates for the month
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        // Get all entries for the month
        return getCalendarEntriesForDateRange(calendar.getId(), startDate, endDate);
    }
}
