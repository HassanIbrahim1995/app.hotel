import { Component, OnInit } from '@angular/core';
import { EmployeeShift } from '../../../models/employee-shift.model';
import { VacationRequest } from '../../../models/vacation-request.model';
import { ShiftService } from '../../../services/shift.service';
import { VacationRequestService } from '../../../services/vacation-request.service';
import { NotificationService, Notification } from '../../../services/notification.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-employee-dashboard',
  templateUrl: './employee-dashboard.component.html',
  styleUrls: ['./employee-dashboard.component.scss']
})
export class EmployeeDashboardComponent implements OnInit {
  upcomingShifts: EmployeeShift[] = [];
  vacationRequests: VacationRequest[] = [];
  notifications: Notification[] = [];
  weekDays: { date: Date }[] = [];
  unreadNotificationsCount = 0;

  constructor(
    private shiftService: ShiftService,
    private vacationRequestService: VacationRequestService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.generateWeekDays();
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    // Get all data in parallel using forkJoin
    forkJoin({
      shifts: this.shiftService.getMyShifts(),
      vacations: this.vacationRequestService.getMyVacationRequests('PENDING'),
      notifications: this.notificationService.getMyNotifications()
    }).subscribe(results => {
      this.upcomingShifts = this.filterUpcomingShifts(results.shifts);
      this.vacationRequests = results.vacations;
      this.notifications = results.notifications;
      this.unreadNotificationsCount = this.notifications.filter(n => !n.read).length;
    });
  }

  private filterUpcomingShifts(shifts: EmployeeShift[]): EmployeeShift[] {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    return shifts
      .filter(shift => {
        const shiftDate = new Date(shift.shift.shiftDate);
        return shiftDate >= today;
      })
      .sort((a, b) => {
        return new Date(a.shift.shiftDate).getTime() - new Date(b.shift.shiftDate).getTime();
      })
      .slice(0, 5); // Show only the next 5 shifts
  }

  private generateWeekDays(): void {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    // Find the start of the week (Sunday)
    const startOfWeek = new Date(today);
    const dayOfWeek = today.getDay();
    startOfWeek.setDate(today.getDate() - dayOfWeek);
    
    // Generate 7 days starting from the start of the week
    this.weekDays = Array.from({ length: 7 }, (_, i) => {
      const date = new Date(startOfWeek);
      date.setDate(startOfWeek.getDate() + i);
      return { date };
    });
  }

  getShiftForDay(date: Date): EmployeeShift | null {
    const dateToFind = new Date(date);
    dateToFind.setHours(0, 0, 0, 0);
    
    return this.upcomingShifts.find(shift => {
      const shiftDate = new Date(shift.shift.shiftDate);
      shiftDate.setHours(0, 0, 0, 0);
      return shiftDate.getTime() === dateToFind.getTime();
    }) || null;
  }

  getStatusClass(status: string): string {
    switch (status.toUpperCase()) {
      case 'ASSIGNED': return 'bg-warning text-dark';
      case 'CONFIRMED': return 'bg-success';
      case 'DECLINED': return 'bg-danger';
      case 'COMPLETED': return 'bg-info';
      default: return 'bg-secondary';
    }
  }

  getVacationStatusClass(status: string): string {
    switch (status.toUpperCase()) {
      case 'PENDING': return 'bg-warning text-dark';
      case 'APPROVED': return 'bg-success';
      case 'REJECTED': return 'bg-danger';
      default: return 'bg-secondary';
    }
  }

  getNotificationTypeClass(type: string): string {
    switch (type.toUpperCase()) {
      case 'SHIFT': return 'bg-primary';
      case 'VACATION': return 'bg-info';
      case 'SYSTEM': return 'bg-dark';
      case 'WARNING': return 'bg-warning text-dark';
      default: return 'bg-secondary';
    }
  }

  getNotificationTypeIcon(type: string): string {
    switch (type.toUpperCase()) {
      case 'SHIFT': return 'bi bi-calendar';
      case 'VACATION': return 'bi bi-airplane';
      case 'SYSTEM': return 'bi bi-gear';
      case 'WARNING': return 'bi bi-exclamation-triangle';
      default: return 'bi bi-bell';
    }
  }

  markAsRead(id: number): void {
    this.notificationService.markAsRead(id).subscribe(() => {
      const notification = this.notifications.find(n => n.id === id);
      if (notification) {
        notification.read = true;
        this.unreadNotificationsCount = this.notifications.filter(n => !n.read).length;
      }
    });
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead().subscribe(() => {
      this.notifications.forEach(n => n.read = true);
      this.unreadNotificationsCount = 0;
    });
  }
}
