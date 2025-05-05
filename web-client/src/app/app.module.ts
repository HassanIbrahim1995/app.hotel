import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// Third-party modules
import { FullCalendarModule } from '@fullcalendar/angular';
import { NgChartsModule } from 'ng2-charts';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule } from 'ngx-spinner';
import { ToastrModule } from 'ngx-toastr';

// App components
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

// Core components and services
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { ErrorInterceptor } from './core/interceptors/error.interceptor';
import { LoadingInterceptor } from './core/interceptors/loading.interceptor';

// Auth components
import { LoginComponent } from './components/auth/login/login.component';

// Employee components
import { EmployeeDashboardComponent } from './components/employee/dashboard/employee-dashboard.component';
import { EmployeeCalendarComponent } from './components/employee/calendar/employee-calendar.component';
import { VacationRequestsComponent } from './components/employee/vacation-requests/vacation-requests.component';

// Manager components
import { ManagerDashboardComponent } from './components/manager/dashboard/manager-dashboard.component';
import { ManagerScheduleComponent } from './components/manager/schedule/manager-schedule.component';
import { TeamMembersComponent } from './components/manager/team/team-members.component';
import { VacationApprovalsComponent } from './components/manager/vacation-requests/vacation-approvals.component';
import { ReportsComponent } from './components/manager/reports/reports.component';

// Admin components
import { EmployeeListComponent } from './components/admin/employees/employee-list.component';
import { LocationListComponent } from './components/admin/locations/location-list.component';
import { ShiftTypeListComponent } from './components/admin/shift-types/shift-type-list.component';
import { SystemSettingsComponent } from './components/admin/settings/system-settings.component';

// Employee components
import { EmployeeShiftsComponent } from './components/employee/shifts/employee-shifts.component';
import { EmployeeProfileComponent } from './components/employee/profile/employee-profile.component';

@NgModule({
  declarations: [
    AppComponent,
    
    // Auth components
    LoginComponent,
    
    // Employee components
    EmployeeDashboardComponent,
    EmployeeCalendarComponent,
    VacationRequestsComponent,
    
    // Manager components
    ManagerDashboardComponent,
    ManagerScheduleComponent,
    TeamMembersComponent,
    VacationApprovalsComponent,
    ReportsComponent,
    
    // Admin components
    EmployeeListComponent,
    LocationListComponent,
    ShiftTypeListComponent,
    SystemSettingsComponent,
    
    // Additional Employee components
    EmployeeShiftsComponent,
    EmployeeProfileComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,

    // Third-party modules
    FullCalendarModule,
    NgChartsModule,
    NgbModule,
    NgxSpinnerModule,
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
      timeOut: 3000
    })
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: LoadingInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
