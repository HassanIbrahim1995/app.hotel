import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-system-settings',
  templateUrl: './system-settings.component.html',
  styleUrls: ['./system-settings.component.scss']
})
export class SystemSettingsComponent implements OnInit {
  generalSettingsForm: FormGroup;
  notificationSettingsForm: FormGroup;
  scheduleSettingsForm: FormGroup;
  loading = false;
  activeTab = 'general';
  
  constructor(
    private fb: FormBuilder,
    private notificationService: NotificationService
  ) {
    this.generalSettingsForm = this.fb.group({
      companyName: ['Shift Manager', [Validators.required]],
      timeZone: ['UTC', [Validators.required]],
      dateFormat: ['MM/dd/yyyy', [Validators.required]],
      timeFormat: ['12h', [Validators.required]],
      defaultLanguage: ['en', [Validators.required]],
      weekStartsOn: ['0', [Validators.required]] // 0 = Sunday, 1 = Monday
    });
    
    this.notificationSettingsForm = this.fb.group({
      enableEmailNotifications: [true],
      emailShiftAssignments: [true],
      emailShiftReminders: [true],
      emailVacationApprovals: [true],
      emailVacationReminders: [true],
      reminderHours: [24, [Validators.required, Validators.min(1), Validators.max(72)]],
      senderEmail: ['noreply@shiftmanager.com', [Validators.required, Validators.email]],
      emailFooter: ['This is an automated message from the Shift Manager system. Please do not reply to this email.']
    });
    
    this.scheduleSettingsForm = this.fb.group({
      minHoursBetweenShifts: [8, [Validators.required, Validators.min(0), Validators.max(24)]],
      maxConsecutiveWorkDays: [7, [Validators.required, Validators.min(1), Validators.max(14)]],
      defaultShiftDuration: [8, [Validators.required, Validators.min(1), Validators.max(24)]],
      enableAutomaticScheduling: [true],
      considerEmployeePreferences: [true],
      enforceBreakRules: [true],
      minBreakDuration: [30, [Validators.required, Validators.min(0), Validators.max(120)]],
      requireVacationApproval: [true]
    });
  }

  ngOnInit(): void {
    // In a real app, you would load settings from the backend here
    this.loadSettings();
  }

  loadSettings(): void {
    // Simulating a backend call to get settings
    this.loading = true;
    setTimeout(() => {
      // In a real app, here you would make a service call to get settings
      // and patch the form values with the response
      this.loading = false;
    }, 500);
  }

  saveGeneralSettings(): void {
    if (this.generalSettingsForm.invalid) {
      this.generalSettingsForm.markAllAsTouched();
      this.notificationService.showError('Please correct the errors in the form');
      return;
    }
    
    this.loading = true;
    // In a real app, here you would make a service call to save settings
    setTimeout(() => {
      this.loading = false;
      this.notificationService.showSuccess('General settings saved successfully');
    }, 500);
  }

  saveNotificationSettings(): void {
    if (this.notificationSettingsForm.invalid) {
      this.notificationSettingsForm.markAllAsTouched();
      this.notificationService.showError('Please correct the errors in the form');
      return;
    }
    
    this.loading = true;
    // In a real app, here you would make a service call to save settings
    setTimeout(() => {
      this.loading = false;
      this.notificationService.showSuccess('Notification settings saved successfully');
    }, 500);
  }

  saveScheduleSettings(): void {
    if (this.scheduleSettingsForm.invalid) {
      this.scheduleSettingsForm.markAllAsTouched();
      this.notificationService.showError('Please correct the errors in the form');
      return;
    }
    
    this.loading = true;
    // In a real app, here you would make a service call to save settings
    setTimeout(() => {
      this.loading = false;
      this.notificationService.showSuccess('Schedule settings saved successfully');
    }, 500);
  }
  
  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }
}
