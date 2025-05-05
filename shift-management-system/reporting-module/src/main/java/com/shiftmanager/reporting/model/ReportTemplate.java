package com.shiftmanager.reporting.model;

import com.shiftmanager.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a report template that can be used to generate reports
 */
@Entity
@Table(name = "report_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportTemplate extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;

    @Column(name = "template_content", columnDefinition = "TEXT")
    private String templateContent;

    @Column(name = "default_parameters", columnDefinition = "TEXT")
    private String defaultParameters;

    @Size(max = 50)
    @Column(name = "file_format")
    private String fileFormat = "PDF";

    @Column(name = "system_template")
    private boolean systemTemplate = false;

    @Column(name = "active")
    private boolean active = true;

    /**
     * Check if this is a PDF template
     * @return true if PDF format
     */
    public boolean isPdfTemplate() {
        return "PDF".equalsIgnoreCase(fileFormat);
    }

    /**
     * Check if this is an Excel template
     * @return true if Excel format
     */
    public boolean isExcelTemplate() {
        return "EXCEL".equalsIgnoreCase(fileFormat) || "XLS".equalsIgnoreCase(fileFormat) || "XLSX".equalsIgnoreCase(fileFormat);
    }
}
