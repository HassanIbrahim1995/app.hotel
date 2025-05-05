package com.shiftmanager.reporting.service.impl;

import com.shiftmanager.core.domain.*;
import com.shiftmanager.core.repository.*;
import com.shiftmanager.reporting.dto.ReportDTO;
import com.shiftmanager.reporting.service.PdfExportService;
import com.shiftmanager.reporting.service.ReportingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the ReportingService for generating various reports
 */
@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {

    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final ShiftRepository shiftRepository;
    private final VacationRequestRepository vacationRequestRepository;
    private final LocationRepository locationRepository;
    private final PdfExportService pdfExportService;

    @Override
    @Transactional(readOnly = true)
    public ReportDTO generateEmployeeShiftReport(Long employeeId, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Shift> shifts = shiftRepository.findByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                employeeId, startDateTime, endDateTime);

        double totalHours = shifts.stream()
                .mapToDouble(Shift::getDurationInHours)
                .sum();

        ReportDTO report = ReportDTO.builder()
                .reportType("EMPLOYEE_SHIFT_REPORT")
                .startDate(startDate)
                .endDate(endDate)
                .generatedAt(LocalDateTime.now())
                .employeeId(employeeId)
                .employeeName(employee.getName().getFullName())
                .department(employee.getDepartment())
                .totalHours(totalHours)
                .totalShifts((long) shifts.size())
                .shifts(mapShiftsToReportEntries(shifts))
                .build();

        if (employee.getLocation() != null) {
            report.setLocationId(employee.getLocation().getId());
            report.setLocationName(employee.getLocation().getName());
        }

        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO generateManagerShiftReport(Long managerId, LocalDate startDate, LocalDate endDate) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + managerId));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Employee> managedEmployees = new ArrayList<>(manager.getManagedEmployees());
        
        List<ReportDTO.EmployeeReportSummaryDTO> employeeSummaries = new ArrayList<>();
        double totalTeamHours = 0;
        long totalTeamShifts = 0;

        for (Employee employee : managedEmployees) {
            List<Shift> employeeShifts = shiftRepository.findByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                    employee.getId(), startDateTime, endDateTime);
            
            double employeeTotalHours = employeeShifts.stream()
                    .mapToDouble(Shift::getDurationInHours)
                    .sum();
            
            totalTeamHours += employeeTotalHours;
            totalTeamShifts += employeeShifts.size();
            
            ReportDTO.EmployeeReportSummaryDTO summary = ReportDTO.EmployeeReportSummaryDTO.builder()
                    .employeeId(employee.getId())
                    .employeeName(employee.getName().getFullName())
                    .department(employee.getDepartment())
                    .position(employee.getPosition())
                    .totalHours(employeeTotalHours)
                    .totalShifts((long) employeeShifts.size())
                    .build();
            
            employeeSummaries.add(summary);
        }

        ReportDTO report = ReportDTO.builder()
                .reportType("MANAGER_SHIFT_REPORT")
                .startDate(startDate)
                .endDate(endDate)
                .generatedAt(LocalDateTime.now())
                .managerId(managerId)
                .managerName(manager.getName().getFullName())
                .department(manager.getDepartment())
                .totalHours(totalTeamHours)
                .totalShifts(totalTeamShifts)
                .employees(employeeSummaries)
                .build();

        if (manager.getLocation() != null) {
            report.setLocationId(manager.getLocation().getId());
            report.setLocationName(manager.getLocation().getName());
        }

        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO generateDepartmentShiftReport(String department, LocalDate startDate, LocalDate endDate) {
        List<Employee> departmentEmployees = employeeRepository.findByDepartment(department);
        
        if (departmentEmployees.isEmpty()) {
            throw new EntityNotFoundException("No employees found in department: " + department);
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        double totalDepartmentHours = 0;
        long totalDepartmentShifts = 0;
        List<ReportDTO.EmployeeReportSummaryDTO> employeeSummaries = new ArrayList<>();

        for (Employee employee : departmentEmployees) {
            List<Shift> employeeShifts = shiftRepository.findByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                    employee.getId(), startDateTime, endDateTime);
            
            double employeeTotalHours = employeeShifts.stream()
                    .mapToDouble(Shift::getDurationInHours)
                    .sum();
            
            totalDepartmentHours += employeeTotalHours;
            totalDepartmentShifts += employeeShifts.size();
            
            ReportDTO.EmployeeReportSummaryDTO summary = ReportDTO.EmployeeReportSummaryDTO.builder()
                    .employeeId(employee.getId())
                    .employeeName(employee.getName().getFullName())
                    .department(employee.getDepartment())
                    .position(employee.getPosition())
                    .totalHours(employeeTotalHours)
                    .totalShifts((long) employeeShifts.size())
                    .build();
            
            employeeSummaries.add(summary);
        }

        double averageHoursPerEmployee = departmentEmployees.isEmpty() ? 0 : 
                totalDepartmentHours / departmentEmployees.size();

        List<ReportDTO.DepartmentReportDTO> departmentSummary = List.of(
                ReportDTO.DepartmentReportDTO.builder()
                        .department(department)
                        .employeeCount((long) departmentEmployees.size())
                        .totalHours(totalDepartmentHours)
                        .totalShifts(totalDepartmentShifts)
                        .averageHoursPerEmployee(averageHoursPerEmployee)
                        .build()
        );

        return ReportDTO.builder()
                .reportType("DEPARTMENT_SHIFT_REPORT")
                .startDate(startDate)
                .endDate(endDate)
                .generatedAt(LocalDateTime.now())
                .department(department)
                .totalHours(totalDepartmentHours)
                .totalShifts(totalDepartmentShifts)
                .employees(employeeSummaries)
                .departments(departmentSummary)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO generateLocationShiftReport(Long locationId, LocalDate startDate, LocalDate endDate) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + locationId));

        List<Employee> locationEmployees = employeeRepository.findByLocationId(locationId);
        
        if (locationEmployees.isEmpty()) {
            throw new EntityNotFoundException("No employees found at location: " + location.getName());
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        double totalLocationHours = 0;
        long totalLocationShifts = 0;
        
        // Group employees by department for department summaries
        Map<String, List<Employee>> employeesByDepartment = locationEmployees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
        
        List<ReportDTO.DepartmentReportDTO> departmentSummaries = new ArrayList<>();
        List<ReportDTO.EmployeeReportSummaryDTO> employeeSummaries = new ArrayList<>();

        for (Map.Entry<String, List<Employee>> entry : employeesByDepartment.entrySet()) {
            String department = entry.getKey();
            List<Employee> departmentEmployees = entry.getValue();
            
            double departmentHours = 0;
            long departmentShifts = 0;
            
            for (Employee employee : departmentEmployees) {
                List<Shift> employeeShifts = shiftRepository.findByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                        employee.getId(), startDateTime, endDateTime);
                
                double employeeTotalHours = employeeShifts.stream()
                        .mapToDouble(Shift::getDurationInHours)
                        .sum();
                
                departmentHours += employeeTotalHours;
                departmentShifts += employeeShifts.size();
                
                ReportDTO.EmployeeReportSummaryDTO summary = ReportDTO.EmployeeReportSummaryDTO.builder()
                        .employeeId(employee.getId())
                        .employeeName(employee.getName().getFullName())
                        .department(employee.getDepartment())
                        .position(employee.getPosition())
                        .totalHours(employeeTotalHours)
                        .totalShifts((long) employeeShifts.size())
                        .build();
                
                employeeSummaries.add(summary);
            }
            
            totalLocationHours += departmentHours;
            totalLocationShifts += departmentShifts;
            
            double averageHoursPerEmployee = departmentEmployees.isEmpty() ? 0 : 
                    departmentHours / departmentEmployees.size();
            
            ReportDTO.DepartmentReportDTO departmentSummary = ReportDTO.DepartmentReportDTO.builder()
                    .department(department)
                    .employeeCount((long) departmentEmployees.size())
                    .totalHours(departmentHours)
                    .totalShifts(departmentShifts)
                    .averageHoursPerEmployee(averageHoursPerEmployee)
                    .build();
            
            departmentSummaries.add(departmentSummary);
        }

        return ReportDTO.builder()
                .reportType("LOCATION_SHIFT_REPORT")
                .startDate(startDate)
                .endDate(endDate)
                .generatedAt(LocalDateTime.now())
                .locationId(locationId)
                .locationName(location.getName())
                .totalHours(totalLocationHours)
                .totalShifts(totalLocationShifts)
                .employees(employeeSummaries)
                .departments(departmentSummaries)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO generateEmployeeVacationReport(Long employeeId, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));

        List<VacationRequest> vacations = vacationRequestRepository.findByEmployeeIdAndDateRange(
                employeeId, startDate, endDate);

        long totalVacationDays = vacations.stream()
                .filter(v -> v.getStatus() == VacationStatus.APPROVED)
                .mapToLong(VacationRequest::getDaysCount)
                .sum();

        ReportDTO report = ReportDTO.builder()
                .reportType("EMPLOYEE_VACATION_REPORT")
                .startDate(startDate)
                .endDate(endDate)
                .generatedAt(LocalDateTime.now())
                .employeeId(employeeId)
                .employeeName(employee.getName().getFullName())
                .department(employee.getDepartment())
                .vacations(mapVacationsToReportEntries(vacations))
                .build();

        if (employee.getLocation() != null) {
            report.setLocationId(employee.getLocation().getId());
            report.setLocationName(employee.getLocation().getName());
        }

        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO generateDepartmentVacationReport(String department, LocalDate startDate, LocalDate endDate) {
        List<Employee> departmentEmployees = employeeRepository.findByDepartment(department);
        
        if (departmentEmployees.isEmpty()) {
            throw new EntityNotFoundException("No employees found in department: " + department);
        }
        
        List<ReportDTO.EmployeeReportSummaryDTO> employeeSummaries = new ArrayList<>();
        List<ReportDTO.VacationReportEntryDTO> allVacations = new ArrayList<>();

        for (Employee employee : departmentEmployees) {
            List<VacationRequest> vacations = vacationRequestRepository.findByEmployeeIdAndDateRange(
                    employee.getId(), startDate, endDate);
            
            long vacationDays = vacations.stream()
                    .filter(v -> v.getStatus() == VacationStatus.APPROVED)
                    .mapToLong(VacationRequest::getDaysCount)
                    .sum();
            
            ReportDTO.EmployeeReportSummaryDTO summary = ReportDTO.EmployeeReportSummaryDTO.builder()
                    .employeeId(employee.getId())
                    .employeeName(employee.getName().getFullName())
                    .department(employee.getDepartment())
                    .position(employee.getPosition())
                    .vacationDays(vacationDays)
                    .build();
            
            employeeSummaries.add(summary);
            allVacations.addAll(mapVacationsToReportEntries(vacations));
        }

        return ReportDTO.builder()
                .reportType("DEPARTMENT_VACATION_REPORT")
                .startDate(startDate)
                .endDate(endDate)
                .generatedAt(LocalDateTime.now())
                .department(department)
                .employees(employeeSummaries)
                .vacations(allVacations)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO generateComprehensiveEmployeeReport(Long employeeId, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Get shifts
        List<Shift> shifts = shiftRepository.findByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                employeeId, startDateTime, endDateTime);
        
        double totalHours = shifts.stream()
                .mapToDouble(Shift::getDurationInHours)
                .sum();

        // Get vacations
        List<VacationRequest> vacations = vacationRequestRepository.findByEmployeeIdAndDateRange(
                employeeId, startDate, endDate);
        
        long vacationDays = vacations.stream()
                .filter(v -> v.getStatus() == VacationStatus.APPROVED)
                .mapToLong(VacationRequest::getDaysCount)
                .sum();

        ReportDTO.EmployeeReportSummaryDTO summary = ReportDTO.EmployeeReportSummaryDTO.builder()
                .employeeId(employee.getId())
                .employeeName(employee.getName().getFullName())
                .department(employee.getDepartment())
                .position(employee.getPosition())
                .totalHours(totalHours)
                .totalShifts((long) shifts.size())
                .vacationDays(vacationDays)
                .build();

        ReportDTO report = ReportDTO.builder()
                .reportType("COMPREHENSIVE_EMPLOYEE_REPORT")
                .startDate(startDate)
                .endDate(endDate)
                .generatedAt(LocalDateTime.now())
                .employeeId(employeeId)
                .employeeName(employee.getName().getFullName())
                .department(employee.getDepartment())
                .totalHours(totalHours)
                .totalShifts((long) shifts.size())
                .shifts(mapShiftsToReportEntries(shifts))
                .vacations(mapVacationsToReportEntries(vacations))
                .employees(List.of(summary))
                .build();

        if (employee.getLocation() != null) {
            report.setLocationId(employee.getLocation().getId());
            report.setLocationName(employee.getLocation().getName());
        }

        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO generateComprehensiveManagerReport(Long managerId, LocalDate startDate, LocalDate endDate) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + managerId));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Employee> managedEmployees = new ArrayList<>(manager.getManagedEmployees());
        
        List<ReportDTO.EmployeeReportSummaryDTO> employeeSummaries = new ArrayList<>();
        List<ReportDTO.ShiftReportEntryDTO> allShifts = new ArrayList<>();
        List<ReportDTO.VacationReportEntryDTO> allVacations = new ArrayList<>();
        
        double totalTeamHours = 0;
        long totalTeamShifts = 0;

        for (Employee employee : managedEmployees) {
            // Get shifts
            List<Shift> employeeShifts = shiftRepository.findByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                    employee.getId(), startDateTime, endDateTime);
            
            double employeeTotalHours = employeeShifts.stream()
                    .mapToDouble(Shift::getDurationInHours)
                    .sum();
            
            totalTeamHours += employeeTotalHours;
            totalTeamShifts += employeeShifts.size();
            
            // Get vacations
            List<VacationRequest> employeeVacations = vacationRequestRepository.findByEmployeeIdAndDateRange(
                    employee.getId(), startDate, endDate);
            
            long vacationDays = employeeVacations.stream()
                    .filter(v -> v.getStatus() == VacationStatus.APPROVED)
                    .mapToLong(VacationRequest::getDaysCount)
                    .sum();
            
            ReportDTO.EmployeeReportSummaryDTO summary = ReportDTO.EmployeeReportSummaryDTO.builder()
                    .employeeId(employee.getId())
                    .employeeName(employee.getName().getFullName())
                    .department(employee.getDepartment())
                    .position(employee.getPosition())
                    .totalHours(employeeTotalHours)
                    .totalShifts((long) employeeShifts.size())
                    .vacationDays(vacationDays)
                    .build();
            
            employeeSummaries.add(summary);
            allShifts.addAll(mapShiftsToReportEntries(employeeShifts));
            allVacations.addAll(mapVacationsToReportEntries(employeeVacations));
        }

        // Group employees by department for department summaries
        Map<String, List<ReportDTO.EmployeeReportSummaryDTO>> summariesByDepartment = employeeSummaries.stream()
                .collect(Collectors.groupingBy(ReportDTO.EmployeeReportSummaryDTO::getDepartment));
        
        List<ReportDTO.DepartmentReportDTO> departmentSummaries = new ArrayList<>();
        
        for (Map.Entry<String, List<ReportDTO.EmployeeReportSummaryDTO>> entry : summariesByDepartment.entrySet()) {
            String department = entry.getKey();
            List<ReportDTO.EmployeeReportSummaryDTO> departmentEmployees = entry.getValue();
            
            double departmentHours = departmentEmployees.stream()
                    .mapToDouble(ReportDTO.EmployeeReportSummaryDTO::getTotalHours)
                    .sum();
            
            long departmentShifts = departmentEmployees.stream()
                    .mapToLong(ReportDTO.EmployeeReportSummaryDTO::getTotalShifts)
                    .sum();
            
            double averageHoursPerEmployee = departmentEmployees.isEmpty() ? 0 : 
                    departmentHours / departmentEmployees.size();
            
            ReportDTO.DepartmentReportDTO departmentSummary = ReportDTO.DepartmentReportDTO.builder()
                    .department(department)
                    .employeeCount((long) departmentEmployees.size())
                    .totalHours(departmentHours)
                    .totalShifts(departmentShifts)
                    .averageHoursPerEmployee(averageHoursPerEmployee)
                    .build();
            
            departmentSummaries.add(departmentSummary);
        }

        ReportDTO report = ReportDTO.builder()
                .reportType("COMPREHENSIVE_MANAGER_REPORT")
                .startDate(startDate)
                .endDate(endDate)
                .generatedAt(LocalDateTime.now())
                .managerId(managerId)
                .managerName(manager.getName().getFullName())
                .department(manager.getDepartment())
                .totalHours(totalTeamHours)
                .totalShifts(totalTeamShifts)
                .employees(employeeSummaries)
                .departments(departmentSummaries)
                .shifts(allShifts)
                .vacations(allVacations)
                .build();

        if (manager.getLocation() != null) {
            report.setLocationId(manager.getLocation().getId());
            report.setLocationName(manager.getLocation().getName());
        }

        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO generateComprehensiveDepartmentReport(String department, LocalDate startDate, LocalDate endDate) {
        List<Employee> departmentEmployees = employeeRepository.findByDepartment(department);
        
        if (departmentEmployees.isEmpty()) {
            throw new EntityNotFoundException("No employees found in department: " + department);
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        double totalDepartmentHours = 0;
        long totalDepartmentShifts = 0;
        
        List<ReportDTO.EmployeeReportSummaryDTO> employeeSummaries = new ArrayList<>();
        List<ReportDTO.ShiftReportEntryDTO> allShifts = new ArrayList<>();
        List<ReportDTO.VacationReportEntryDTO> allVacations = new ArrayList<>();

        for (Employee employee : departmentEmployees) {
            // Get shifts
            List<Shift> employeeShifts = shiftRepository.findByEmployeeIdAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                    employee.getId(), startDateTime, endDateTime);
            
            double employeeTotalHours = employeeShifts.stream()
                    .mapToDouble(Shift::getDurationInHours)
                    .sum();
            
            totalDepartmentHours += employeeTotalHours;
            totalDepartmentShifts += employeeShifts.size();
            
            // Get vacations
            List<VacationRequest> employeeVacations = vacationRequestRepository.findByEmployeeIdAndDateRange(
                    employee.getId(), startDate, endDate);
            
            long vacationDays = employeeVacations.stream()
                    .filter(v -> v.getStatus() == VacationStatus.APPROVED)
                    .mapToLong(VacationRequest::getDaysCount)
                    .sum();
            
            ReportDTO.EmployeeReportSummaryDTO summary = ReportDTO.EmployeeReportSummaryDTO.builder()
                    .employeeId(employee.getId())
                    .employeeName(employee.getName().getFullName())
                    .department(employee.getDepartment())
                    .position(employee.getPosition())
                    .totalHours(employeeTotalHours)
                    .totalShifts((long) employeeShifts.size())
                    .vacationDays(vacationDays)
                    .build();
            
            employeeSummaries.add(summary);
            allShifts.addAll(mapShiftsToReportEntries(employeeShifts));
            allVacations.addAll(mapVacationsToReportEntries(employeeVacations));
        }

        double averageHoursPerEmployee = departmentEmployees.isEmpty() ? 0 : 
                totalDepartmentHours / departmentEmployees.size();

        ReportDTO.DepartmentReportDTO departmentSummary = ReportDTO.DepartmentReportDTO.builder()
                .department(department)
                .employeeCount((long) departmentEmployees.size())
                .totalHours(totalDepartmentHours)
                .totalShifts(totalDepartmentShifts)
                .averageHoursPerEmployee(averageHoursPerEmployee)
                .build();

        return ReportDTO.builder()
                .reportType("COMPREHENSIVE_DEPARTMENT_REPORT")
                .startDate(startDate)
                .endDate(endDate)
                .generatedAt(LocalDateTime.now())
                .department(department)
                .totalHours(totalDepartmentHours)
                .totalShifts(totalDepartmentShifts)
                .employees(employeeSummaries)
                .departments(List.of(departmentSummary))
                .shifts(allShifts)
                .vacations(allVacations)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generateEmployeeShiftReportPdf(Long employeeId, LocalDate startDate, LocalDate endDate) {
        ReportDTO report = generateEmployeeShiftReport(employeeId, startDate, endDate);
        return pdfExportService.generateEmployeeShiftReportPdf(report);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generateManagerTeamReportPdf(Long managerId, LocalDate startDate, LocalDate endDate) {
        ReportDTO report = generateComprehensiveManagerReport(managerId, startDate, endDate);
        return pdfExportService.generateManagerTeamReportPdf(report);
    }

    /**
     * Maps Shift entities to ShiftReportEntryDTO objects
     * @param shifts List of shifts to map
     * @return List of shift report entries
     */
    private List<ReportDTO.ShiftReportEntryDTO> mapShiftsToReportEntries(List<Shift> shifts) {
        return shifts.stream()
                .map(shift -> ReportDTO.ShiftReportEntryDTO.builder()
                        .shiftId(shift.getId())
                        .employeeName(shift.getEmployee().getName().getFullName())
                        .shiftType(shift.getShiftType().getDisplayName())
                        .startTime(shift.getStartTime())
                        .endTime(shift.getEndTime())
                        .hours(shift.getDurationInHours())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Maps VacationRequest entities to VacationReportEntryDTO objects
     * @param vacations List of vacation requests to map
     * @return List of vacation report entries
     */
    private List<ReportDTO.VacationReportEntryDTO> mapVacationsToReportEntries(List<VacationRequest> vacations) {
        return vacations.stream()
                .map(vacation -> ReportDTO.VacationReportEntryDTO.builder()
                        .vacationId(vacation.getId())
                        .employeeName(vacation.getEmployee().getName().getFullName())
                        .startDate(vacation.getStartDate())
                        .endDate(vacation.getEndDate())
                        .days(vacation.getDaysCount())
                        .status(vacation.getStatus().toString())
                        .build())
                .collect(Collectors.toList());
    }
}
