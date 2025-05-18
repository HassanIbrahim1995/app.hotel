package com.shiftmanager.api.service.impl;

import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.EmployeeShift;
import com.shiftmanager.api.model.Location;
import com.shiftmanager.api.model.Shift;
import com.shiftmanager.api.model.VacationRequest;
import com.shiftmanager.api.repository.EmployeeRepository;
import com.shiftmanager.api.repository.EmployeeShiftRepository;
import com.shiftmanager.api.repository.LocationRepository;
import com.shiftmanager.api.repository.ShiftRepository;
import com.shiftmanager.api.repository.VacationRequestRepository;
import com.shiftmanager.api.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the ReportService interface
 */
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private EmployeeRepository employeeRepository;
    private ShiftRepository shiftRepository;
    private EmployeeShiftRepository employeeShiftRepository;
    private LocationRepository locationRepository;
    private VacationRequestRepository vacationRequestRepository;

    @Override
    public byte[] generateEmployeeScheduleReport(Long employeeId, LocalDate startDate, LocalDate endDate) {
        log.debug("Generating employee schedule report for employee ID: {} from {} to {}",
                employeeId, startDate, endDate);

        // Verify employee exists
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        // Get all employee shifts for the date range
        List<EmployeeShift> employeeShifts = employeeShiftRepository.findByEmployeeAndShiftDateBetween(
                employee, startDate, endDate);

        // Generate report content
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("EMPLOYEE SCHEDULE REPORT\n");
        reportContent.append("=========================\n\n");
        reportContent.append("Employee: ").append(employee.getFirstName()).append(" ").append(employee.getLastName())
                .append(" (ID: ").append(employee.getId()).append(")\n");
        reportContent.append("Employee Number: ").append(employee.getEmployeeNumber()).append("\n");
        reportContent.append("Report Period: ").append(startDate.format(DATE_FORMATTER)).append(" to ")
                .append(endDate.format(DATE_FORMATTER)).append("\n");
        reportContent.append("Generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("\n\n");

        // Schedule summary
        reportContent.append("SCHEDULE SUMMARY\n");
        reportContent.append("----------------\n");
        reportContent.append("Total Shifts: ").append(employeeShifts.size()).append("\n");

        // Calculate total hours
        double totalHours = 0;
        for (EmployeeShift shift : employeeShifts) {
            if (shift.getShift().getStartTime() != null && shift.getShift().getEndTime() != null) {
                Duration duration = Duration.between(shift.getShift().getStartTime(), shift.getShift().getEndTime());
                totalHours += duration.toMinutes() / 60.0;
            }
        }
        reportContent.append("Total Hours: ").append(String.format("%.2f", totalHours)).append("\n\n");

        // Shift details
        reportContent.append("SHIFT DETAILS\n");
        reportContent.append("-------------\n\n");

        if (employeeShifts.isEmpty()) {
            reportContent.append("No shifts scheduled for this period.\n\n");
        } else {
            // Group shifts by date
            Map<LocalDate, List<EmployeeShift>> shiftsByDate = new HashMap<>();
            for (EmployeeShift shift : employeeShifts) {
                LocalDate date = shift.getShift().getShiftDate();
                if (!shiftsByDate.containsKey(date)) {
                    shiftsByDate.put(date, new ArrayList<>());
                }
                shiftsByDate.get(date).add(shift);
            }

            // Sort dates and display shifts
            shiftsByDate.keySet().stream().sorted().forEach(date -> {
                reportContent.append("Date: ").append(date.format(DATE_FORMATTER)).append("\n");

                List<EmployeeShift> shiftsOnDate = shiftsByDate.get(date);
                for (EmployeeShift shift : shiftsOnDate) {
                    Shift shiftInfo = shift.getShift();
                    reportContent.append("  - ")
                            .append(shiftInfo.getStartTime().format(TIME_FORMATTER))
                            .append(" to ")
                            .append(shiftInfo.getEndTime().format(TIME_FORMATTER))
                            .append(" (")
                            .append(shiftInfo.getShiftType().getName())
                            .append(") at ")
                            .append(shiftInfo.getLocation().getName());

                    if (shift.getStatus() != null) {
                        reportContent.append(" - Status: ").append(shift.getStatus());
                    }

                    reportContent.append("\n");
                }
                reportContent.append("\n");
            });
        }

        // Add footer
        reportContent.append("\n");
        reportContent.append("End of Report\n");
        reportContent.append("=============\n");

        return reportContent.toString().getBytes();
    }

    @Override
    public byte[] generateTeamScheduleReport(Long managerId, LocalDate startDate, LocalDate endDate) {
        log.debug("Generating team schedule report for manager ID: {} from {} to {}",
                managerId, startDate, endDate);

        // Verify manager exists
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + managerId));

        // Get all team members (employees reporting to this manager)
        List<Employee> teamMembers = employeeRepository.findByManagerId(managerId);

        if (teamMembers.isEmpty()) {
            throw new ResourceNotFoundException("No team members found for manager with ID: " + managerId);
        }

        // Generate report content
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("TEAM SCHEDULE REPORT\n");
        reportContent.append("====================\n\n");
        reportContent.append("Manager: ").append(manager.getFirstName()).append(" ").append(manager.getLastName())
                .append(" (ID: ").append(manager.getId()).append(")\n");
        reportContent.append("Report Period: ").append(startDate.format(DATE_FORMATTER)).append(" to ")
                .append(endDate.format(DATE_FORMATTER)).append("\n");
        reportContent.append("Generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("\n\n");

        // Team summary
        reportContent.append("TEAM SUMMARY\n");
        reportContent.append("------------\n");
        reportContent.append("Total Team Members: ").append(teamMembers.size()).append("\n\n");

        reportContent.append("Team Members:\n");
        for (Employee employee : teamMembers) {
            reportContent.append("  - ").append(employee.getFirstName()).append(" ").append(employee.getLastName())
                    .append(" (").append(employee.getPosition()).append(")\n");
        }
        reportContent.append("\n");

        // Schedule details by date
        reportContent.append("SCHEDULE BY DATE\n");
        reportContent.append("----------------\n\n");

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            final LocalDate dateToFind = currentDate;

            reportContent.append("Date: ").append(currentDate.format(DATE_FORMATTER)).append("\n");

            boolean shiftsFound = false;

            for (Employee employee : teamMembers) {
                List<EmployeeShift> employeeShifts = employeeShiftRepository.findByEmployeeAndShift_ShiftDate(employee, dateToFind);

                if (!employeeShifts.isEmpty()) {
                    shiftsFound = true;

                    for (EmployeeShift shift : employeeShifts) {
                        Shift shiftInfo = shift.getShift();
                        reportContent.append("  - ")
                                .append(employee.getFirstName()).append(" ").append(employee.getLastName())
                                .append(": ")
                                .append(shiftInfo.getStartTime().format(TIME_FORMATTER))
                                .append(" to ")
                                .append(shiftInfo.getEndTime().format(TIME_FORMATTER))
                                .append(" (")
                                .append(shiftInfo.getShiftType().getName())
                                .append(") at ")
                                .append(shiftInfo.getLocation().getName())
                                .append("\n");
                    }
                }
            }

            if (!shiftsFound) {
                reportContent.append("  No shifts scheduled\n");
            }

            reportContent.append("\n");
            currentDate = currentDate.plusDays(1);
        }

        // Schedule details by employee
        reportContent.append("SCHEDULE BY EMPLOYEE\n");
        reportContent.append("-------------------\n\n");

        for (Employee employee : teamMembers) {
            reportContent.append(employee.getFirstName()).append(" ").append(employee.getLastName()).append("\n");
            reportContent.append(new String(new char[employee.getFirstName().length() + employee.getLastName().length() + 1]).replace('\0', '-')).append("\n");

            List<EmployeeShift> employeeShifts = employeeShiftRepository.findByEmployeeAndShiftDateBetween(
                    employee, startDate, endDate);

            if (employeeShifts.isEmpty()) {
                reportContent.append("  No shifts scheduled for this period.\n\n");
            } else {
                for (EmployeeShift shift : employeeShifts) {
                    Shift shiftInfo = shift.getShift();
                    reportContent.append("  ")
                            .append(shiftInfo.getShiftDate().format(DATE_FORMATTER))
                            .append(": ")
                            .append(shiftInfo.getStartTime().format(TIME_FORMATTER))
                            .append(" to ")
                            .append(shiftInfo.getEndTime().format(TIME_FORMATTER))
                            .append(" (")
                            .append(shiftInfo.getShiftType().getName())
                            .append(") at ")
                            .append(shiftInfo.getLocation().getName())
                            .append("\n");
                }
                reportContent.append("\n");
            }
        }

        // Add footer
        reportContent.append("\n");
        reportContent.append("End of Report\n");
        reportContent.append("=============\n");

        return reportContent.toString().getBytes();
    }

    @Override
    public byte[] generateLocationScheduleReport(Long locationId, LocalDate startDate, LocalDate endDate) {
        log.debug("Generating location schedule report for location ID: {} from {} to {}",
                locationId, startDate, endDate);

        // Verify location exists
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + locationId));

        // Get all shifts for this location in the date range
        List<Shift> shifts = shiftRepository.findByLocationAndDateRange(locationId, startDate, endDate);

        // Generate report content
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("LOCATION SCHEDULE REPORT\n");
        reportContent.append("=======================\n\n");
        reportContent.append("Location: ").append(location.getName()).append(" (ID: ").append(location.getId()).append(")\n");
        reportContent.append("Address: ").append(location.getAddress()).append("\n");
        reportContent.append("Report Period: ").append(startDate.format(DATE_FORMATTER)).append(" to ")
                .append(endDate.format(DATE_FORMATTER)).append("\n");
        reportContent.append("Generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("\n\n");

        // Schedule summary
        reportContent.append("SCHEDULE SUMMARY\n");
        reportContent.append("----------------\n");
        reportContent.append("Total Shifts: ").append(shifts.size()).append("\n\n");

        // Schedule details by date
        reportContent.append("SCHEDULE BY DATE\n");
        reportContent.append("----------------\n\n");

        if (shifts.isEmpty()) {
            reportContent.append("No shifts scheduled for this location during the specified period.\n\n");
        } else {
            // Group shifts by date
            Map<LocalDate, List<Shift>> shiftsByDate = new HashMap<>();
            for (Shift shift : shifts) {
                LocalDate date = shift.getShiftDate();
                if (!shiftsByDate.containsKey(date)) {
                    shiftsByDate.put(date, new ArrayList<>());
                }
                shiftsByDate.get(date).add(shift);
            }

            // Sort dates and display shifts
            shiftsByDate.keySet().stream().sorted().forEach(date -> {
                reportContent.append("Date: ").append(date.format(DATE_FORMATTER)).append("\n");

                List<Shift> shiftsOnDate = shiftsByDate.get(date);
                for (Shift shift : shiftsOnDate) {
                    reportContent.append("  - ")
                            .append(shift.getStartTime().format(TIME_FORMATTER))
                            .append(" to ")
                            .append(shift.getEndTime().format(TIME_FORMATTER))
                            .append(" (")
                            .append(shift.getShiftType().getName())
                            .append(")");

                    // Get employee assigned to this shift
                    List<EmployeeShift> assignments = employeeShiftRepository.findByShift(shift);
                    if (!assignments.isEmpty()) {
                        reportContent.append(" - Assigned to: ");
                        boolean first = true;
                        for (EmployeeShift assignment : assignments) {
                            if (!first) {
                                reportContent.append(", ");
                            }
                            Employee employee = assignment.getEmployee();
                            reportContent.append(employee.getFirstName()).append(" ").append(employee.getLastName());
                            first = false;
                        }
                    } else {
                        reportContent.append(" - Unassigned");
                    }

                    reportContent.append("\n");
                }
                reportContent.append("\n");
            });
        }

        // Add staffing coverage analysis
        reportContent.append("STAFFING COVERAGE ANALYSIS\n");
        reportContent.append("-------------------------\n");

        if (shifts.isEmpty()) {
            reportContent.append("No shifts to analyze for this period.\n\n");
        } else {
            int totalShifts = shifts.size();
            long assignedShifts = shifts.stream()
                    .filter(shift -> !employeeShiftRepository.findByShift(shift).isEmpty())
                    .count();

            double coveragePercent = (double) assignedShifts / totalShifts * 100;

            reportContent.append("Total Shifts: ").append(totalShifts).append("\n");
            reportContent.append("Assigned Shifts: ").append(assignedShifts).append("\n");
            reportContent.append("Unassigned Shifts: ").append(totalShifts - assignedShifts).append("\n");
            reportContent.append("Coverage: ").append(String.format("%.2f", coveragePercent)).append("%\n\n");
        }

        // Add footer
        reportContent.append("\n");
        reportContent.append("End of Report\n");
        reportContent.append("=============\n");

        return reportContent.toString().getBytes();
    }

    @Override
    public byte[] generateVacationSummaryReport(Integer year, Integer month, Long departmentId) {
        log.debug("Generating vacation summary report for year: {}, month: {}, department ID: {}",
                year, month, departmentId);

        // Determine date range
        LocalDate startDate, endDate;
        if (month != null) {
            // Month-specific report
            startDate = LocalDate.of(year, month, 1);
            endDate = startDate.plusMonths(1).minusDays(1);
        } else {
            // Full year report
            startDate = LocalDate.of(year, 1, 1);
            endDate = LocalDate.of(year, 12, 31);
        }

        List<VacationRequest> vacationRequests;
        vacationRequests = vacationRequestRepository.findByDateRange(startDate, endDate);

        StringBuilder reportContent = new StringBuilder();
        reportContent.append("VACATION SUMMARY REPORT\n");
        reportContent.append("======================\n\n");

        if (month != null) {
            reportContent.append("Period: ").append(startDate.getMonth()).append(" ").append(year).append("\n");
        } else {
            reportContent.append("Period: Year ").append(year).append("\n");
        }

        if (departmentId != null) {
            reportContent.append("Department ID: ").append(departmentId).append("\n");
        } else {
            reportContent.append("All Departments\n");
        }

        reportContent.append("Generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("\n\n");

        reportContent.append("SUMMARY STATISTICS\n");
        reportContent.append("------------------\n");
        reportContent.append("Total Vacation Requests: ").append(vacationRequests.size()).append("\n");

        long approvedRequests = vacationRequests.stream()
                .filter(vr -> "APPROVED".equals(vr.getStatus()))
                .count();

        long pendingRequests = vacationRequests.stream()
                .filter(vr -> "PENDING".equals(vr.getStatus()))
                .count();

        long rejectedRequests = vacationRequests.stream()
                .filter(vr -> "REJECTED".equals(vr.getStatus()))
                .count();

        reportContent.append("Approved Requests: ").append(approvedRequests).append("\n");
        reportContent.append("Pending Requests: ").append(pendingRequests).append("\n");
        reportContent.append("Rejected Requests: ").append(rejectedRequests).append("\n\n");

        int totalVacationDays = 0;
        for (VacationRequest request : vacationRequests) {
            if ("APPROVED".equals(request.getStatus())) {
                // Count only the days that fall within our report period
                LocalDate requestStart = request.getStartDate().isBefore(startDate) ? startDate : request.getStartDate();
                LocalDate requestEnd = request.getEndDate().isAfter(endDate) ? endDate : request.getEndDate();

                totalVacationDays += (int) (requestEnd.toEpochDay() - requestStart.toEpochDay() + 1);
            }
        }

        reportContent.append("Total Vacation Days (Approved): ").append(totalVacationDays).append("\n\n");

        reportContent.append("VACATION DETAILS BY EMPLOYEE\n");
        reportContent.append("----------------------------\n\n");

        if (vacationRequests.isEmpty()) {
            reportContent.append("No vacation requests for this period.\n\n");
        } else {
            // Group vacation requests by employee
            Map<Long, List<VacationRequest>> requestsByEmployee = new HashMap<>();

            for (VacationRequest request : vacationRequests) {
                Long employeeId = request.getEmployee().getId();
                if (!requestsByEmployee.containsKey(employeeId)) {
                    requestsByEmployee.put(employeeId, new ArrayList<>());
                }
                requestsByEmployee.get(employeeId).add(request);
            }

            for (Long employeeId : requestsByEmployee.keySet()) {
                Employee employee = employeeRepository.findById(employeeId).orElse(null);
                if (employee != null) {
                    reportContent.append(employee.getFirstName()).append(" ").append(employee.getLastName())
                            .append(" (").append(employee.getPosition()).append(")\n");

                    List<VacationRequest> employeeRequests = requestsByEmployee.get(employeeId);

                    int employeeVacationDays = 0;
                    for (VacationRequest request : employeeRequests) {
                        if ("APPROVED".equals(request.getStatus())) {
                            LocalDate requestStart = request.getStartDate().isBefore(startDate) ? startDate : request.getStartDate();
                            LocalDate requestEnd = request.getEndDate().isAfter(endDate) ? endDate : request.getEndDate();

                            employeeVacationDays += (int) (requestEnd.toEpochDay() - requestStart.toEpochDay() + 1);
                        }
                    }

                    reportContent.append("  Total Vacation Days: ").append(employeeVacationDays).append("\n");

                    for (VacationRequest request : employeeRequests) {
                        reportContent.append("  - ")
                                .append(request.getStartDate().format(DATE_FORMATTER))
                                .append(" to ")
                                .append(request.getEndDate().format(DATE_FORMATTER))
                                .append(" (")
                                .append(request.getStatus())
                                .append(")");

                        if (request.getRequestNotes() != null && !request.getRequestNotes().isEmpty()) {
                            reportContent.append(" - Reason: ").append(request.getRequestNotes());
                        }

                        reportContent.append("\n");
                    }

                    reportContent.append("\n");
                }
            }
        }

        reportContent.append("\n");
        reportContent.append("End of Report\n");
        reportContent.append("=============\n");

        return reportContent.toString().getBytes();
    }

    @Override
    public byte[] generateHoursWorkedReport(List<Long> employeeIds, LocalDate startDate, LocalDate endDate) {
        log.debug("Generating hours worked report for employees: {} from {} to {}",
                employeeIds, startDate, endDate);

        List<Employee> employees;
        if (employeeIds != null && !employeeIds.isEmpty()) {
            // Get specific employees
            employees = employeeRepository.findAllById(employeeIds);
            if (employees.size() != employeeIds.size()) {
                List<Long> foundIds = employees.stream().map(Employee::getId).collect(Collectors.toList());
                List<Long> missingIds = employeeIds.stream()
                        .filter(id -> !foundIds.contains(id))
                        .collect(Collectors.toList());

                log.warn("Some employee IDs were not found: {}", missingIds);
            }
        } else {
            employees = employeeRepository.findAll();
        }

        StringBuilder reportContent = new StringBuilder();
        reportContent.append("HOURS WORKED REPORT\n");
        reportContent.append("==================\n\n");
        reportContent.append("Report Period: ").append(startDate.format(DATE_FORMATTER)).append(" to ")
                .append(endDate.format(DATE_FORMATTER)).append("\n");
        reportContent.append("Generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("\n\n");

        reportContent.append("SUMMARY STATISTICS\n");
        reportContent.append("------------------\n");
        reportContent.append("Total Employees: ").append(employees.size()).append("\n\n");

        reportContent.append("HOURS WORKED BY EMPLOYEE\n");
        reportContent.append("-----------------------\n\n");

        if (employees.isEmpty()) {
            reportContent.append("No employees found for this report.\n\n");
        } else {
            double grandTotalHours = 0;

            for (Employee employee : employees) {
                List<EmployeeShift> completedShifts = employeeShiftRepository.findByEmployeeAndStatusAndShiftDateBetween(
                        employee, "COMPLETED", startDate, endDate);

                double totalHours = 0;
                for (EmployeeShift shift : completedShifts) {
                    if (shift.getClockInTime() != null && shift.getClockOutTime() != null) {
                        Duration duration = Duration.between(shift.getClockInTime(), shift.getClockOutTime());
                        totalHours += duration.toMinutes() / 60.0;
                    } else if (shift.getShift().getStartTime() != null && shift.getShift().getEndTime() != null) {
                        Duration duration = Duration.between(shift.getShift().getStartTime(), shift.getShift().getEndTime());
                        totalHours += duration.toMinutes() / 60.0;
                    }
                }

                grandTotalHours += totalHours;

                reportContent.append(employee.getFirstName()).append(" ").append(employee.getLastName())
                        .append(" (").append(employee.getPosition()).append(")\n");

                reportContent.append("  Employee ID: ").append(employee.getId()).append("\n");
                reportContent.append("  Employee Number: ").append(employee.getEmployeeNumber()).append("\n");
                reportContent.append("  Total Shifts: ").append(completedShifts.size()).append("\n");
                reportContent.append("  Total Hours: ").append(String.format("%.2f", totalHours)).append("\n");

                reportContent.append("  Daily Breakdown:\n");

                if (completedShifts.isEmpty()) {
                    reportContent.append("    No shifts completed in this period.\n");
                } else {
                    Map<LocalDate, List<EmployeeShift>> shiftsByDate = new HashMap<>();
                    for (EmployeeShift shift : completedShifts) {
                        LocalDate date = shift.getShift().getShiftDate();
                        if (!shiftsByDate.containsKey(date)) {
                            shiftsByDate.put(date, new ArrayList<>());
                        }
                        shiftsByDate.get(date).add(shift);
                    }

                    shiftsByDate.keySet().stream().sorted().forEach(date -> {
                        double dailyHours = 0;
                        List<EmployeeShift> shiftsOnDate = shiftsByDate.get(date);

                        for (EmployeeShift shift : shiftsOnDate) {
                            if (shift.getClockInTime() != null && shift.getClockOutTime() != null) {
                                Duration duration = Duration.between(shift.getClockInTime(), shift.getClockOutTime());
                                dailyHours += duration.toMinutes() / 60.0;
                            } else if (shift.getShift().getStartTime() != null && shift.getShift().getEndTime() != null) {
                                Duration duration = Duration.between(shift.getShift().getStartTime(), shift.getShift().getEndTime());
                                dailyHours += duration.toMinutes() / 60.0;
                            }
                        }

                        reportContent.append("    ")
                                .append(date.format(DATE_FORMATTER))
                                .append(": ")
                                .append(String.format("%.2f", dailyHours))
                                .append(" hours");

                        if (shiftsOnDate.size() > 1) {
                            reportContent.append(" (").append(shiftsOnDate.size()).append(" shifts)");
                        }

                        reportContent.append("\n");
                    });
                }

                reportContent.append("\n");
            }

            reportContent.append("GRAND TOTAL HOURS: ").append(String.format("%.2f", grandTotalHours)).append("\n\n");
        }

        reportContent.append("\n");
        reportContent.append("End of Report\n");
        reportContent.append("=============\n");

        return reportContent.toString().getBytes();
    }
}