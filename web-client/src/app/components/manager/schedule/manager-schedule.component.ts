import { Component, OnInit } from '@angular/core';
import { NgbDate, NgbCalendar, NgbDateParserFormatter, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Shift } from '../../../models/shift.model';
import { Employee } from '../../../models/employee.model';
import { Location } from '../../../models/location.model';
import { ShiftType } from '../../../models/shift-type.model';
import { ShiftService } from '../../../services/shift.service';
import { EmployeeService } from '../../../services/employee.service';
import { LocationService } from '../../../services/location.service';
import { ShiftTypeService } from '../../../services/shift-type.service';
import { NotificationService } from '../../../services/notification.service';
import { ReportService, ReportParams } from '../../../services/report.service';

@Component({
  selector: 'app-manager-schedule',
  templateUrl: './manager-schedule.component.html',
  styleUrls: ['./manager-schedule.component.scss']
})
export class ManagerScheduleComponent implements OnInit {
  shifts: Shift[] = [];
  employees: Employee[] = [];
  locations: Location[] = [];
  shiftTypes: ShiftType[] = [];
  loading = false;
  
  fromDate: NgbDate;
  toDate: NgbDate;
  
  // For filtering
  selectedLocationId?: number;
  selectedShiftTypeId?: number;
  
  // For new shift
  newShift: Partial<Shift> = {
    shiftDate: '',
    startTime: '',
    endTime: '',
  };
  
  // For assigning shifts
  selectedShift?: Shift;
  selectedEmployeeId?: number;
  availableEmployees: Employee[] = [];
  
  hoveredDate: NgbDate | null = null;

  constructor(
    private calendar: NgbCalendar,
    private dateFormatter: NgbDateParserFormatter,
    private shiftService: ShiftService,
    private employeeService: EmployeeService,
    private locationService: LocationService,
    private shiftTypeService: ShiftTypeService,
    private notificationService: NotificationService,
    private reportService: ReportService,
    private modalService: NgbModal
  ) {
    // Default date range is current week
    const today = calendar.getToday();
    this.fromDate = calendar.getPrev(today, 'd', today.day);
    this.toDate = calendar.getNext(this.fromDate, 'd', 6);
  }

  ngOnInit(): void {
    this.loadEmployees();
    this.loadLocations();
    this.loadShiftTypes();
    this.loadShifts();
  }

  loadShifts(): void {
    this.loading = true;
    const fromDateStr = this.dateFormatter.format(this.fromDate);
    const toDateStr = this.dateFormatter.format(this.toDate);
    
    this.shiftService.getAllShifts(fromDateStr, toDateStr).subscribe({
      next: (data) => {
        this.shifts = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching shifts', error);
        this.notificationService.showError('Failed to load shifts');
        this.loading = false;
      }
    });
  }

  loadEmployees(): void {
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        this.employees = data;
      },
      error: (error) => {
        console.error('Error fetching employees', error);
        this.notificationService.showError('Failed to load employees');
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

  loadShiftTypes(): void {
    this.shiftTypeService.getActiveShiftTypes().subscribe({
      next: (data) => {
        this.shiftTypes = data;
      },
      error: (error) => {
        console.error('Error fetching shift types', error);
        this.notificationService.showError('Failed to load shift types');
      }
    });
  }

  applyFilter(): void {
    this.loadShifts();
  }

  resetFilter(): void {
    this.selectedLocationId = undefined;
    this.selectedShiftTypeId = undefined;
    this.loadShifts();
  }

  openShiftModal(content: any, shift?: Shift): void {
    if (shift) {
      this.selectedShift = {...shift};
    } else {
      this.newShift = {
        shiftDate: this.dateFormatter.format(this.calendar.getToday()),
        startTime: '09:00',
        endTime: '17:00',
      };
    }
    
    this.modalService.open(content, { centered: true });
  }

  openAssignModal(content: any, shift: Shift): void {
    this.selectedShift = shift;
    this.selectedEmployeeId = undefined;
    this.loadAvailableEmployees(shift);
    
    this.modalService.open(content, { centered: true });
  }

  loadAvailableEmployees(shift: Shift): void {
    // In a real application, this would filter employees based on availability
    // For simplicity, we're just using all employees
    this.availableEmployees = [...this.employees];
  }

  assignShift(): void {
    if (!this.selectedShift || !this.selectedEmployeeId) {
      this.notificationService.showError('Please select an employee');
      return;
    }
    
    this.shiftService.assignShift(this.selectedShift.id!, this.selectedEmployeeId).subscribe({
      next: (result) => {
        if (result) {
          this.loadShifts();
          this.modalService.dismissAll();
          this.notificationService.showSuccess('Shift assigned successfully');
        } else {
          this.notificationService.showError('Failed to assign shift');
        }
      },
      error: (error) => {
        console.error('Error assigning shift', error);
        this.notificationService.showError('Failed to assign shift');
      }
    });
  }

  createShift(): void {
    if (!this.newShift.shiftDate || !this.newShift.startTime || !this.newShift.endTime ||
        !this.newShift.location || !this.newShift.shiftType) {
      this.notificationService.showError('Please fill all required fields');
      return;
    }
    
    // Complete the shift object before saving
    const completeShift = this.newShift as Shift;
    
    this.shiftService.createShift(completeShift).subscribe({
      next: (result) => {
        this.loadShifts();
        this.modalService.dismissAll();
        this.notificationService.showSuccess('Shift created successfully');
      },
      error: (error) => {
        console.error('Error creating shift', error);
        this.notificationService.showError('Failed to create shift');
      }
    });
  }

  deleteShift(shiftId: number): void {
    if (confirm('Are you sure you want to delete this shift?')) {
      this.shiftService.deleteShift(shiftId).subscribe({
        next: () => {
          this.loadShifts();
          this.notificationService.showSuccess('Shift deleted successfully');
        },
        error: (error) => {
          console.error('Error deleting shift', error);
          this.notificationService.showError('Failed to delete shift');
        }
      });
    }
  }

  exportScheduleAsPdf(): void {
    const params: ReportParams = {
      startDate: this.dateFormatter.format(this.fromDate),
      endDate: this.dateFormatter.format(this.toDate),
      locationId: this.selectedLocationId,
      reportType: 'SCHEDULE'
    };
    
    this.reportService.generateScheduleReport(params).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `schedule_${params.startDate}_${params.endDate}.pdf`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      },
      error: (error) => {
        console.error('Error exporting schedule', error);
        this.notificationService.showError('Failed to export schedule');
      }
    });
  }

  // Date range picker methods
  onDateSelection(date: NgbDate): void {
    if (!this.fromDate && !this.toDate) {
      this.fromDate = date;
    } else if (this.fromDate && !this.toDate && date.after(this.fromDate)) {
      this.toDate = date;
      this.loadShifts();
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
