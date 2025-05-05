package com.shiftmanager.dashboard.model;

import com.shiftmanager.common.model.BaseEntity;
import com.shiftmanager.common.model.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
 * Entity representing a widget on a dashboard
 */
@Entity
@Table(name = "dashboard_widgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardWidget extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "dashboard_id", nullable = false)
    private Dashboard dashboard;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "widget_type", nullable = false)
    private WidgetType widgetType;

    @Column(name = "config_json", columnDefinition = "TEXT")
    private String configJson;

    @Column(name = "position_x")
    private Integer positionX = 0;

    @Column(name = "position_y")
    private Integer positionY = 0;

    @Column(name = "width")
    private Integer width = 1;

    @Column(name = "height")
    private Integer height = 1;

    @Column(name = "refresh_interval_seconds")
    private Integer refreshIntervalSeconds;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "active")
    private boolean active = true;
}
