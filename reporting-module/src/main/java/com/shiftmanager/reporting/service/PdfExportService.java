package com.shiftmanager.reporting.service;

import com.shiftmanager.reporting.dto.ReportDTO;

/**
 * Service interface for PDF export operations
 */
public interface PdfExportService {

    /**
     * Generate a PDF report for the specified employee's shifts
     * @param report The employee shift report data
     * @return The PDF report as a byte array
     */
    byte[] generateEmployeeShiftReportPdf(ReportDTO report);

    /**
     * Generate a PDF report for the specified manager's team
     * @param report The manager team report data
     * @return The PDF report as a byte array
     */
    byte[] generateManagerTeamReportPdf(ReportDTO report);
}
