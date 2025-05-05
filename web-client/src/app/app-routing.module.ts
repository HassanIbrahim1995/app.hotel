import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Guards
import { AuthGuard } from './core/guards/auth.guard';
import { ManagerGuard } from './core/guards/manager.guard';
import { AdminGuard } from './core/guards/admin.guard';

// Auth components
import { LoginComponent } from './components/auth/login/login.component';

// Employee components
import { EmployeeDashboardComponent } from './components/employee/dashboard/employee-dashboard.component';
import { EmployeeCalendarComponent } from './components/employee/calendar/employee-calendar.component';
import { VacationRequestsComponent } from './components/employee/vacation-requests/vacation-requests.component';

// Manager components
import { ManagerDashboardComponent } from './components/manager/dashboard/manager-dashboard.component';
import { ManagerScheduleComponent } from './components/manager/schedule/manager-schedule.component';

// Admin components
import { EmployeeListComponent } from './components/admin/employees/employee-list.component';
import { LocationListComponent } from './components/admin/locations/location-list.component';
import { ShiftTypeListComponent } from './components/admin/shift-types/shift-type-list.component';

const routes: Routes = [
  // Auth routes
  { path: 'login', component: LoginComponent },
  //{ path: 'register', component: RegisterComponent },
  //{ path: 'forgot-password', component: ForgotPasswordComponent },
  
  // Employee routes
  { path: 'dashboard', component: EmployeeDashboardComponent, canActivate: [AuthGuard] },
  { path: 'calendar', component: EmployeeCalendarComponent, canActivate: [AuthGuard] },
  { path: 'shifts', component: EmployeeShiftsComponent, canActivate: [AuthGuard] },
  { path: 'vacation-requests', component: VacationRequestsComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: EmployeeProfileComponent, canActivate: [AuthGuard] },
  
  // Manager routes
  {
    path: 'manager',
    canActivate: [AuthGuard, ManagerGuard],
    children: [
      { path: 'dashboard', component: ManagerDashboardComponent },
      { path: 'schedule', component: ManagerScheduleComponent },
      { path: 'team', component: TeamMembersComponent },
      { path: 'vacation-requests', component: VacationApprovalsComponent },
      { path: 'reports', component: ReportsComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  
  // Admin routes
  {
    path: 'admin',
    canActivate: [AuthGuard, AdminGuard],
    children: [
      { path: 'employees', component: EmployeeListComponent },
      { path: 'locations', component: LocationListComponent },
      { path: 'shift-types', component: ShiftTypeListComponent },
      { path: 'settings', component: SystemSettingsComponent },
      { path: '', redirectTo: 'employees', pathMatch: 'full' }
    ]
  },
  
  // Default route
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: '/dashboard' } // Handle wild card routes
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
