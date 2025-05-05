package com.shiftmanager.reporting.service.impl;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.shiftmanager.reporting.dto.ReportDTO;
import com.shiftmanager.reporting.service.PdfExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementation of the PdfExportService for PDF generation
 */
@Service
@RequiredArgsConstructor
public class PdfExportServiceImpl implements PdfExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public byte[] generateEmployeeShiftReportPdf(ReportDTO report) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf, PageSize.A4)) {
            
            // Add report title
            String title = "Employee Shift Report: " + report.getEmployeeName();
            document.add(new Paragraph(title).setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            
            // Add report metadata
            document.add(new Paragraph("Department: " + report.getDepartment())
                    .setFontSize(12).setMarginTop(10));
            
            if (report.getLocationName() != null) {
                document.add(new Paragraph("Location: " + report.getLocationName())
                        .setFontSize(12));
            }
            
            document.add(new Paragraph("Period: " + report.getStartDate().format(DATE_FORMATTER) + 
                    " to " + report.getEndDate().format(DATE_FORMATTER))
                    .setFontSize(12));
            
            document.add(new Paragraph("Generated: " + report.getGeneratedAt().format(DATE_TIME_FORMATTER))
                    .setFontSize(12).setMarginBottom(20));
            
            // Add summary
            document.add(new Paragraph("Summary").setBold().setFontSize(14).setMarginBottom(10));
            
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
            summaryTable.setWidth(UnitValue.createPercentValue(50));
            
            summaryTable.addCell(createHeaderCell("Total Hours"));
            summaryTable.addCell(createCell(String.format("%.2f", report.getTotalHours())));
            
            summaryTable.addCell(createHeaderCell("Total Shifts"));
            summaryTable.addCell(createCell(report.getTotalShifts().toString()));
            
            document.add(summaryTable);
            document.add(new Paragraph("").setMarginBottom(20));
            
            // Add shift details
            if (report.getShifts() != null && !report.getShifts().isEmpty()) {
                document.add(new Paragraph("Shift Details").setBold().setFontSize(14).setMarginBottom(10));
                
                Table shiftsTable = new Table(UnitValue.createPercentArray(new float[]{2, 3, 3, 2}));
                shiftsTable.setWidth(UnitValue.createPercentValue(100));
                
                shiftsTable.addHeaderCell(createHeaderCell("Date"));
                shiftsTable.addHeaderCell(createHeaderCell("Shift Type"));
                shiftsTable.addHeaderCell(createHeaderCell("Time"));
                shiftsTable.addHeaderCell(createHeaderCell("Hours"));
                
                for (ReportDTO.ShiftReportEntryDTO shift : report.getShifts()) {
                    shiftsTable.addCell(createCell(shift.getStartTime().toLocalDate().format(DATE_FORMATTER)));
                    shiftsTable.addCell(createCell(shift.getShiftType()));
                    shiftsTable.addCell(createCell(shift.getStartTime().format(TIME_FORMATTER) + 
                            " - " + shift.getEndTime().format(TIME_FORMATTER)));
                    shiftsTable.addCell(createCell(String.format("%.2f", shift.getHours())));
                }
                
                document.add(shiftsTable);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate employee shift report PDF", e);
        }
        
        return outputStream.toByteArray();
    }

    @Override
    public byte[] generateManagerTeamReportPdf(ReportDTO report) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf, PageSize.A4)) {
            
            // Add report title
            String title = "Manager Team Report: " + report.getManagerName();
            document.add(new Paragraph(title).setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            
            // Add report metadata
            document.add(new Paragraph("Department: " + report.getDepartment())
                    .setFontSize(12).setMarginTop(10));
            
            if (report.getLocationName() != null) {
                document.add(new Paragraph("Location: " + report.getLocationName())
                        .setFontSize(12));
            }
            
            document.add(new Paragraph("Period: " + report.getStartDate().format(DATE_FORMATTER) + 
                    " to " + report.getEndDate().format(DATE_FORMATTER))
                    .setFontSize(12));
            
            document.add(new Paragraph("Generated: " + report.getGeneratedAt().format(DATE_TIME_FORMATTER))
                    .setFontSize(12).setMarginBottom(20));
            
            // Add team summary
            document.add(new Paragraph("Team Summary").setBold().setFontSize(14).setMarginBottom(10));
            
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
            summaryTable.setWidth(UnitValue.createPercentValue(50));
            
            summaryTable.addCell(createHeaderCell("Total Team Hours"));
            summaryTable.addCell(createCell(String.format("%.2f", report.getTotalHours())));
            
            summaryTable.addCell(createHeaderCell("Total Team Shifts"));
            summaryTable.addCell(createCell(report.getTotalShifts().toString()));
            
            document.add(summaryTable);
            document.add(new Paragraph("").setMarginBottom(20));
            
            // Add employee summaries
            if (report.getEmployees() != null && !report.getEmployees().isEmpty()) {
                document.add(new Paragraph("Employee Summaries").setBold().setFontSize(14).setMarginBottom(10));
                
                Table employeesTable = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2, 2}));
                employeesTable.setWidth(UnitValue.createPercentValue(100));
                
                employeesTable.addHeaderCell(createHeaderCell("Employee"));
                employeesTable.addHeaderCell(createHeaderCell("Position"));
                employeesTable.addHeaderCell(createHeaderCell("Total Hours"));
                employeesTable.addHeaderCell(createHeaderCell("Total Shifts"));
                employeesTable.addHeaderCell(createHeaderCell("Vacation Days"));
                
                for (ReportDTO.EmployeeReportSummaryDTO employee : report.getEmployees()) {
                    employeesTable.addCell(createCell(employee.getEmployeeName()));
                    employeesTable.addCell(createCell(employee.getPosition() != null ? employee.getPosition() : ""));
                    employeesTable.addCell(createCell(String.format("%.2f", employee.getTotalHours())));
                    employeesTable.addCell(createCell(employee.getTotalShifts() != null ? 
                            employee.getTotalShifts().toString() : "0"));
                    employeesTable.addCell(createCell(employee.getVacationDays() != null ? 
                            employee.getVacationDays().toString() : "0"));
                }
                
                document.add(employeesTable);
                document.add(new Paragraph("").setMarginBottom(20));
            }
            
            // Add department summaries if available
            if (report.getDepartments() != null && !report.getDepartments().isEmpty()) {
                document.add(new Paragraph("Department Summaries").setBold().setFontSize(14).setMarginBottom(10));
                
                Table departmentsTable = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 2, 2}));
                departmentsTable.setWidth(UnitValue.createPercentValue(100));
                
                departmentsTable.addHeaderCell(createHeaderCell("Department"));
                departmentsTable.addHeaderCell(createHeaderCell("Employee Count"));
                departmentsTable.addHeaderCell(createHeaderCell("Total Hours"));
                departmentsTable.addHeaderCell(createHeaderCell("Total Shifts"));
                departmentsTable.addHeaderCell(createHeaderCell("Avg Hours/Employee"));
                
                for (ReportDTO.DepartmentReportDTO dept : report.getDepartments()) {
                    departmentsTable.addCell(createCell(dept.getDepartment()));
                    departmentsTable.addCell(createCell(dept.getEmployeeCount().toString()));
                    departmentsTable.addCell(createCell(String.format("%.2f", dept.getTotalHours())));
                    departmentsTable.addCell(createCell(dept.getTotalShifts().toString()));
                    departmentsTable.addCell(createCell(String.format("%.2f", dept.getAverageHoursPerEmployee())));
                }
                
                document.add(departmentsTable);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate manager team report PDF", e);
        }
        
        return outputStream.toByteArray();
    }

    /**
     * Create a header cell for a table
     * @param text Cell text
     * @return The created cell
     */
    private Cell createHeaderCell(String text) {
        Cell cell = new Cell();
        cell.add(new Paragraph(text).setBold());
        cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        cell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
        cell.setPadding(5);
        return cell;
    }

    /**
     * Create a regular cell for a table
     * @param text Cell text
     * @return The created cell
     */
    private Cell createCell(String text) {
        Cell cell = new Cell();
        cell.add(new Paragraph(text));
        cell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
        cell.setPadding(5);
        return cell;
    }
}
