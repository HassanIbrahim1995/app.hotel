package com.shiftmanager.dashboard.model;

import com.shiftmanager.common.model.BaseEntity;
import com.shiftmanager.common.model.Location;
import com.shiftmanager.employeemanagement.model.Employee;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a dashboard
 */
@Entity
@Table(name = "dashboards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dashboard extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "default_dashboard")
    private boolean defaultDashboard = false;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Employee owner;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DashboardWidget> widgets = new HashSet<>();

    @Column(name = "layout_config", columnDefinition = "TEXT")
    private String layoutConfig;

    @Column(name = "shared")
    private boolean shared = false;

    @Column(name = "active")
    private boolean active = true;

    /**
     * Add a widget to this dashboard
     * @param widget the widget to add
     */
    public void addWidget(DashboardWidget widget) {
        widget.setDashboard(this);
        widgets.add(widget);
    }

    /**
     * Remove a widget from this dashboard
     * @param widget the widget to remove
     */
    public void removeWidget(DashboardWidget widget) {
        widgets.remove(widget);
        widget.setDashboard(null);
    }

    /**
     * Get the number of widgets on this dashboard
     * @return widget count
     */
    public int getWidgetCount() {
        return widgets.size();
    }
}
