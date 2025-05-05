import { Component, OnInit } from '@angular/core';
import { Employee } from '../../../models/employee.model';
import { EmployeeShift } from '../../../models/employee-shift.model';
import { VacationRequest } from '../../../models/vacation-request.model';
import { ShiftService } from '../../../services/shift.service';
import { EmployeeService } from '../../../services/employee.service';
import { VacationRequestService } from '../../../services/vacation-request.service';
import { forkJoin } from 'rxjs';

interface TeamStats {
  employeeCount: number;
  activeEmployeeCount: number;
  todayShiftCount: number;
  completedShiftCount: number;
  pendingVacationRequestCount: number;
  todayVacationCount: number;
  weeklyHours: number;
  weeklyOvertime: number;
}

@Component({
  selector: 'app-manager-dashboard',
  templateUrl: './manager-dashboard.component.html',
  styleUrls: ['./manager-dashboard.component.scss']
})
export class ManagerDashboardComponent implements OnInit {
  teamMembers: Employee[] = [];
  teamShifts: EmployeeShift[] = [];
  pendingVacationRequests: VacationRequest[] = [];
  approvedVacationRequests: VacationRequest[] = [];
  weekDays: { date: Date }[] = [];
  currentWeekStart: Date = new Date();
  currentWeekEnd: Date = new Date();
  
  teamStats: TeamStats = {
    employeeCount: 0,
    activeEmployeeCount: 0,
    todayShiftCount: 0,
    completedShiftCount: 0,
    pendingVacationRequestCount: 0,
    todayVacationCount: 0,
    weeklyHours: 0,
    weeklyOvertime: 0
  };

  constructor(
    private employeeService: EmployeeService,
    private shiftService: ShiftService,
    private vacationRequestService: VacationRequestService
  ) { }

  ngOnInit(): void {
    this.setCurrentWeek();
    this.generateWeekDays();
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    // Format dates for API calls
    const startDateStr = this.formatDateForApi(this.currentWeekStart);
    const endDateStr = this.formatDateForApi(this.currentWeekEnd);
    
    // Get all data in parallel using forkJoin
    forkJoin({
      employees: this.employeeService.getEmployeesByManager(0), // We'll get current manager's ID in the real implementation
      shifts: this.shiftService.getAllShifts(startDateStr, endDateStr),
      pendingVacations: this.vacationRequestService.getTeamVacationRequests('PENDING'),
      approvedVacations: this.vacationRequestService.getTeamVacationRequests('APPROVED')
    }).subscribe(results => {
      this.teamMembers = results.employees;
      this.teamShifts = this.mapEmployeeShifts(results.shifts);
      this.pendingVacationRequests = results.pendingVacations;
      this.approvedVacationRequests = results.approvedVacations;
      
      this.calculateTeamStats();
    });
  }

  private mapEmployeeShifts(shifts: any[]): EmployeeShift[] {
    // In a real implementation, this would convert the raw API data to EmployeeShift objects
    // For now, we'll just return the shifts as is
    return shifts as EmployeeShift[];
  }

  private calculateTeamStats(): void {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    // Calculate basic stats
    this.teamStats.employeeCount = this.teamMembers.length;
    
    // Count employees with shifts today
    this.teamStats.activeEmployeeCount = this.countActiveEmployeesToday();
    
    // Count shifts for today
    this.teamStats.todayShiftCount = this.countShiftsForDate(today);
    
    // Count completed shifts
    this.teamStats.completedShiftCount = this.teamShifts.filter(s => s.status === 'COMPLETED').length;
    
    // Count pending vacation requests
    this.teamStats.pendingVacationRequestCount = this.pendingVacationRequests.length;
    
    // Count employees on vacation today
    this.teamStats.todayVacationCount = this.countEmployeesOnVacationToday();
    
    // Calculate weekly hours
    this.teamStats.weeklyHours = this.calculateWeeklyHours();
    
    // Calculate overtime
    this.teamStats.weeklyOvertime = this.calculateWeeklyOvertime();
  }

  private countActiveEmployeesToday(): number {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const employeeIdsWithShiftsToday = new Set<number>();
    
    this.teamShifts.forEach(shift => {
      const shiftDate = new Date(shift.shift.shiftDate);
      shiftDate.setHours(0, 0, 0, 0);
      if (shiftDate.getTime() === today.getTime()) {
        employeeIdsWithShiftsToday.add(shift.employee.id);
      }
    });
    
    return employeeIdsWithShiftsToday.size;
  }

  private countShiftsForDate(date: Date): number {
    return this.teamShifts.filter(shift => {
      const shiftDate = new Date(shift.shift.shiftDate);
      shiftDate.setHours(0, 0, 0, 0);
      return shiftDate.getTime() === date.getTime();
    }).length;
  }

  private countEmployeesOnVacationToday(): number {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const employeeIdsOnVacation = new Set<number>();
    
    this.approvedVacationRequests.forEach(vacation => {
      const startDate = new Date(vacation.startDate);
      const endDate = new Date(vacation.endDate);
      startDate.setHours(0, 0, 0, 0);
      endDate.setHours(0, 0, 0, 0);
      
      if (startDate.getTime() <= today.getTime() && today.getTime() <= endDate.getTime()) {
        employeeIdsOnVacation.add(vacation.employee.id);
      }
    });
    
    return employeeIdsOnVacation.size;
  }

  private calculateWeeklyHours(): number {
    // In a real implementation, this would calculate actual hours from shifts
    // For now, let's return a dummy value
    return 160;
  }

  private calculateWeeklyOvertime(): number {
    // In a real implementation, this would calculate actual overtime from shifts
    // For now, let's return a dummy value
    return 12;
  }

  getShiftForEmployeeAndDay(employee: Employee, date: Date): EmployeeShift | null {
    const dateToFind = new Date(date);
    dateToFind.setHours(0, 0, 0, 0);
    
    return this.teamShifts.find(shift => {
      const shiftDate = new Date(shift.shift.shiftDate);
      shiftDate.setHours(0, 0, 0, 0);
      return shift.employee.id === employee.id && shiftDate.getTime() === dateToFind.getTime();
    }) || null;
  }

  isEmployeeOnVacation(employee: Employee, date: Date): boolean {
    const dateToCheck = new Date(date);
    dateToCheck.setHours(0, 0, 0, 0);
    
    return this.approvedVacationRequests.some(vacation => {
      const startDate = new Date(vacation.startDate);
      const endDate = new Date(vacation.endDate);
      startDate.setHours(0, 0, 0, 0);
      endDate.setHours(0, 0, 0, 0);
      
      return vacation.employee.id === employee.id && 
             startDate.getTime() <= dateToCheck.getTime() && 
             dateToCheck.getTime() <= endDate.getTime();
    });
  }

  private setCurrentWeek(): void {
    // Set current week (Sunday to Saturday)
    const today = new Date();
    const dayOfWeek = today.getDay(); // 0 = Sunday, 1 = Monday, etc.
    
    this.currentWeekStart = new Date(today);
    this.currentWeekStart.setDate(today.getDate() - dayOfWeek);
    this.currentWeekStart.setHours(0, 0, 0, 0);
    
    this.currentWeekEnd = new Date(this.currentWeekStart);
    this.currentWeekEnd.setDate(this.currentWeekStart.getDate() + 6);
    this.currentWeekEnd.setHours(23, 59, 59, 999);
  }

  private generateWeekDays(): void {
    // Generate 7 days starting from current week start
    this.weekDays = Array.from({ length: 7 }, (_, i) => {
      const date = new Date(this.currentWeekStart);
      date.setDate(this.currentWeekStart.getDate() + i);
      return { date };
    });
  }

  previousWeek(): void {
    // Go to previous week
    this.currentWeekStart.setDate(this.currentWeekStart.getDate() - 7);
    this.currentWeekEnd.setDate(this.currentWeekEnd.getDate() - 7);
    this.generateWeekDays();
    this.loadDashboardData();
  }

  nextWeek(): void {
    // Go to next week
    this.currentWeekStart.setDate(this.currentWeekStart.getDate() + 7);
    this.currentWeekEnd.setDate(this.currentWeekEnd.getDate() + 7);
    this.generateWeekDays();
    this.loadDashboardData();
  }

  private formatDateForApi(date: Date): string {
    // Format date as YYYY-MM-DD
    return date.toISOString().split('T')[0];
  }

  openShiftAssignmentModal(employee: Employee, date: Date): void {
    // This would open a modal to assign a shift
    console.log('Open shift assignment modal for employee', employee.id, 'on date', date);
  }

  openCreateShiftModal(): void {
    // This would open a modal to create a new shift
    console.log('Open create shift modal');
  }

  approveVacationRequest(id: number): void {
    this.vacationRequestService.approveVacationRequest(id).subscribe(updatedRequest => {
      // Remove from pending and add to approved
      this.pendingVacationRequests = this.pendingVacationRequests.filter(r => r.id !== id);
      this.approvedVacationRequests.push(updatedRequest);
      this.calculateTeamStats();
    });
  }

  rejectVacationRequest(id: number): void {
    this.vacationRequestService.rejectVacationRequest(id).subscribe(() => {
      // Remove from pending
      this.pendingVacationRequests = this.pendingVacationRequests.filter(r => r.id !== id);
      this.calculateTeamStats();
    });
  }

  viewVacationDetails(id: number): void {
    // This would navigate to the vacation request details page
    console.log('View vacation details for ID', id);
  }

  openEmailTeamModal(): void {
    // This would open a modal to send an email to the team
    console.log('Open email team modal');
  }
}
