import { Component, OnInit } from '@angular/core';
import { NgbDate, NgbCalendar, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { EmployeeShift } from '../../../models/employee-shift.model';
import { ShiftService } from '../../../services/shift.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-employee-shifts',
  templateUrl: './employee-shifts.component.html',
  styleUrls: ['./employee-shifts.component.scss']
})
export class EmployeeShiftsComponent implements OnInit {
  shifts: EmployeeShift[] = [];
  filteredShifts: EmployeeShift[] = [];
  loading = false;
  
  // Filter variables
  fromDate: NgbDate;
  toDate: NgbDate;
  hoveredDate: NgbDate | null = null;
  filterStatus: string = 'ALL';
  
  constructor(
    private shiftService: ShiftService,
    private notificationService: NotificationService,
    private calendar: NgbCalendar,
    private dateFormatter: NgbDateParserFormatter
  ) { 
    // Default date range is current week
    const today = calendar.getToday();
    this.fromDate = calendar.getPrev(today, 'd', today.day);
    this.toDate = calendar.getNext(this.fromDate, 'd', 6);
  }

  ngOnInit(): void {
    this.loadShifts();
  }

  loadShifts(): void {
    this.loading = true;
    const fromDateStr = this.dateFormatter.format(this.fromDate);
    const toDateStr = this.dateFormatter.format(this.toDate);
    
    this.shiftService.getMyShifts(fromDateStr, toDateStr).subscribe({
      next: (data) => {
        this.shifts = data;
        this.applyFilters();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching shifts', error);
        this.notificationService.showError('Failed to load shifts');
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    this.filteredShifts = this.shifts.filter(shift => {
      if (this.filterStatus !== 'ALL') {
        return shift.status === this.filterStatus;
      }
      return true;
    });
  }

  confirmShift(shiftId: number): void {
    this.shiftService.confirmShift(shiftId).subscribe({
      next: (data) => {
        const index = this.shifts.findIndex(s => s.id === shiftId);
        if (index !== -1) {
          this.shifts[index] = data;
          this.applyFilters();
        }
        this.notificationService.showSuccess('Shift confirmed successfully');
      },
      error: (error) => {
        console.error('Error confirming shift', error);
        this.notificationService.showError('Failed to confirm shift');
      }
    });
  }

  declineShift(shiftId: number, reason: string = ''): void {
    this.shiftService.declineShift(shiftId, reason).subscribe({
      next: (data) => {
        const index = this.shifts.findIndex(s => s.id === shiftId);
        if (index !== -1) {
          this.shifts[index] = data;
          this.applyFilters();
        }
        this.notificationService.showSuccess('Shift declined successfully');
      },
      error: (error) => {
        console.error('Error declining shift', error);
        this.notificationService.showError('Failed to decline shift');
      }
    });
  }

  clockIn(shiftId: number): void {
    this.shiftService.clockIn(shiftId).subscribe({
      next: (data) => {
        const index = this.shifts.findIndex(s => s.id === shiftId);
        if (index !== -1) {
          this.shifts[index] = data;
          this.applyFilters();
        }
        this.notificationService.showSuccess('Clocked in successfully');
      },
      error: (error) => {
        console.error('Error clocking in', error);
        this.notificationService.showError('Failed to clock in');
      }
    });
  }

  clockOut(shiftId: number): void {
    this.shiftService.clockOut(shiftId).subscribe({
      next: (data) => {
        const index = this.shifts.findIndex(s => s.id === shiftId);
        if (index !== -1) {
          this.shifts[index] = data;
          this.applyFilters();
        }
        this.notificationService.showSuccess('Clocked out successfully');
      },
      error: (error) => {
        console.error('Error clocking out', error);
        this.notificationService.showError('Failed to clock out');
      }
    });
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'ASSIGNED': return 'bg-info';
      case 'CONFIRMED': return 'bg-success';
      case 'DECLINED': return 'bg-danger';
      case 'COMPLETED': return 'bg-secondary';
      default: return 'bg-light text-dark';
    }
  }

  // For date range picker
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
