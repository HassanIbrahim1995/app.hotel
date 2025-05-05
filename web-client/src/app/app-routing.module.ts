import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// We'll add route components as they are created
const routes: Routes = [
  // Auth routes - when we create these components
  //{ path: 'login', component: LoginComponent },
  //{ path: 'register', component: RegisterComponent },
  //{ path: 'forgot-password', component: ForgotPasswordComponent },
  
  // Employee routes
  //{ path: 'dashboard', component: EmployeeDashboardComponent, canActivate: [AuthGuard] },
  //{ path: 'calendar', component: EmployeeCalendarComponent, canActivate: [AuthGuard] },
  //{ path: 'shifts', component: EmployeeShiftsComponent, canActivate: [AuthGuard] },
  //{ path: 'vacation-requests', component: VacationRequestsComponent, canActivate: [AuthGuard] },
  //{ path: 'profile', component: EmployeeProfileComponent, canActivate: [AuthGuard] },
  
  // Manager routes
  //{
  //  path: 'manager',
  //  canActivate: [AuthGuard, ManagerGuard],
  //  children: [
  //    { path: 'dashboard', component: ManagerDashboardComponent },
  //    { path: 'team', component: TeamMembersComponent },
  //    { path: 'schedule', component: TeamScheduleComponent },
  //    { path: 'vacation-requests', component: VacationApprovalsComponent },
  //    { path: 'reports', component: ReportsComponent }
  //  ]
  //},
  
  // Admin routes
  //{
  //  path: 'admin',
  //  canActivate: [AuthGuard, AdminGuard],
  //  children: [
  //    { path: 'employees', component: EmployeeManagementComponent },
  //    { path: 'locations', component: LocationManagementComponent },
  //    { path: 'shift-types', component: ShiftTypeManagementComponent },
  //    { path: 'settings', component: SystemSettingsComponent }
  //  ]
  //},
  
  // Default route
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: '/dashboard' } // Handle wild card routes
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
