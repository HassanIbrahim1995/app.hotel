import { Component, OnInit } from '@angular/core';
import { NgbCalendar, NgbDate, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { Employee } from '../../../models/employee.model';
import { Location } from '../../../models/location.model';
import { EmployeeService } from '../../../services/employee.service';
import { LocationService } from '../../../services/location.service';
import { ReportService, ReportParams } from '../../../services/report.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {
  employees: Employee[] = [];
  teamMembers: Employee[] = [];
  locations: Location[] = [];
  loading = false;
  generatingReport = false;
  
  // Report parameters
  reportType = 'SCHEDULE';
  selectedEmployeeId?: number;
  selectedLocationId?: number;
  fromDate: NgbDate;
  toDate: NgbDate;
  hoveredDate: NgbDate | null = null;
  
  constructor(
    private employeeService: EmployeeService,
    private locationService: LocationService,
    private reportService: ReportService,
    private notificationService: NotificationService,
    private calendar: NgbCalendar,
    private dateFormatter: NgbDateParserFormatter
  ) {
    // Default date range is current month
    const today = calendar.getToday();
    this.fromDate = new NgbDate(today.year, today.month, 1);
    this.toDate = calendar.getNext(this.fromDate, 'm', 1);
    this.toDate = new NgbDate(this.toDate.year, this.toDate.month, 0); // Last day of current month
  }

  ngOnInit(): void {
    this.loadLocations();
    this.loadTeamMembers();
  }

  loadTeamMembers(): void {
    this.loading = true;
    this.employeeService.getCurrentEmployee().subscribe({
      next: (manager) => {
        if (manager.id) {
          this.employeeService.getEmployeesByManager(manager.id).subscribe({
            next: (data) => {
              this.teamMembers = data;
              this.loading = false;
            },
            error: (error) => {
              console.error('Error fetching team members', error);
              this.notificationService.showError('Failed to load team members');
              this.loading = false;
            }
          });
        } else {
          this.loading = false;
          this.notificationService.showError('Cannot identify current manager');
        }
      },
      error: (error) => {
        console.error('Error fetching current manager', error);
        this.notificationService.showError('Failed to load manager data');
        this.loading = false;
      }
    });
  }

  loadLocations(): void {
    this.locationService.getActiveLocations().subscribe({
      next: (data) => {
        this.locations = data;
      },
      error: (error) => {
        console.error('Error fetching locations', error);
        this.notificationService.showError('Failed to load locations');
      }
    });
  }

  generateReport(): void {
    this.generatingReport = true;
    
    const params: ReportParams = {
      startDate: this.dateFormatter.format(this.fromDate),
      endDate: this.dateFormatter.format(this.toDate),
      employeeId: this.selectedEmployeeId,
      locationId: this.selectedLocationId,
      reportType: this.reportType
    };
    
    let reportObservable;
    
    switch (this.reportType) {
      case 'SCHEDULE':
        reportObservable = this.reportService.generateScheduleReport(params);
        break;
      case 'HOURS':
        reportObservable = this.reportService.generateHoursReport(params);
        break;
      case 'ATTENDANCE':
        reportObservable = this.reportService.generateAttendanceReport(params);
        break;
      case 'VACATION':
        reportObservable = this.reportService.generateVacationReport(params);
        break;
      default:
        reportObservable = this.reportService.generateScheduleReport(params);
    }
    
    reportObservable.subscribe({
      next: (blob) => {
        this.generatingReport = false;
        this.downloadReport(blob);
      },
      error: (error) => {
        console.error('Error generating report', error);
        this.notificationService.showError('Failed to generate report');
        this.generatingReport = false;
      }
    });
  }

  downloadReport(blob: Blob): void {
    const url = window.URL.createObjectURL(blob);
    const reportName = this.getReportFileName();
    
    const a = document.createElement('a');
    a.href = url;
    a.download = reportName;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
    
    this.notificationService.showSuccess('Report generated successfully');
  }

  getReportFileName(): string {
    const reportTypes = {
      'SCHEDULE': 'schedule',
      'HOURS': 'hours',
      'ATTENDANCE': 'attendance',
      'VACATION': 'vacation'
    };
    
    const reportTypeName = reportTypes[this.reportType as keyof typeof reportTypes] || 'report';
    const startDate = this.dateFormatter.format(this.fromDate);
    const endDate = this.dateFormatter.format(this.toDate);
    
    return `${reportTypeName}_${startDate}_${endDate}.pdf`;
  }

  resetForm(): void {
    this.reportType = 'SCHEDULE';
    this.selectedEmployeeId = undefined;
    this.selectedLocationId = undefined;
    
    // Reset to current month
    const today = this.calendar.getToday();
    this.fromDate = new NgbDate(today.year, today.month, 1);
    this.toDate = this.calendar.getNext(this.fromDate, 'm', 1);
    this.toDate = new NgbDate(this.toDate.year, this.toDate.month, 0);
  }

  // Date range picker methods
  onDateSelection(date: NgbDate): void {
    if (!this.fromDate && !this.toDate) {
      this.fromDate = date;
    } else if (this.fromDate && !this.toDate && date.after(this.fromDate)) {
      this.toDate = date;
    } else {
      this.toDate = null;
      this.fromDate = date;
    }
  }

  isHovered(date: NgbDate): boolean {
    return this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate);
  }

  isInside(date: NgbDate): boolean {
    return this.toDate && date.after(this.fromDate) && date.before(this.toDate);
  }

  isRange(date: NgbDate): boolean {
    return date.equals(this.fromDate) || (this.toDate && date.equals(this.toDate)) || this.isInside(date) || this.isHovered(date);
  }

  validateInput(currentValue: NgbDate | null, input: string): NgbDate | null {
    const parsed = this.dateFormatter.parse(input);
    return parsed && this.calendar.isValid(NgbDate.from(parsed)) ? NgbDate.from(parsed) : currentValue;
  }
}
