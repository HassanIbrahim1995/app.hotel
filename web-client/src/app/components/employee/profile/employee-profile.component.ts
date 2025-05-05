import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Employee } from '../../../models/employee.model';
import { EmployeeService } from '../../../services/employee.service';
import { NotificationService } from '../../../services/notification.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-employee-profile',
  templateUrl: './employee-profile.component.html',
  styleUrls: ['./employee-profile.component.scss']
})
export class EmployeeProfileComponent implements OnInit {
  employee: Employee | null = null;
  profileForm: FormGroup;
  loading = false;
  isEditing = false;

  constructor(
    private employeeService: EmployeeService,
    private notificationService: NotificationService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.profileForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(100)]],
      phoneNumber: ['', [Validators.maxLength(20)]],
      address: this.fb.group({
        street: ['', [Validators.maxLength(100)]],
        city: ['', [Validators.maxLength(50)]],
        state: ['', [Validators.maxLength(50)]],
        zipCode: ['', [Validators.maxLength(20)]],
        country: ['', [Validators.maxLength(50)]]
      })
    });
  }

  ngOnInit(): void {
    this.loadEmployeeProfile();
  }

  loadEmployeeProfile(): void {
    this.loading = true;
    this.employeeService.getCurrentEmployee().subscribe({
      next: (data) => {
        this.employee = data;
        this.patchFormWithEmployeeData();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching employee profile', error);
        this.notificationService.showError('Failed to load your profile');
        this.loading = false;
      }
    });
  }

  patchFormWithEmployeeData(): void {
    if (this.employee) {
      this.profileForm.patchValue({
        firstName: this.employee.firstName,
        lastName: this.employee.lastName,
        email: this.employee.email,
        phoneNumber: this.employee.phoneNumber,
        address: this.employee.address || {}
      });
      this.profileForm.markAsPristine();
    }
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
    if (!this.isEditing) {
      this.patchFormWithEmployeeData();
    }
  }

  saveProfile(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      this.notificationService.showError('Please correct the errors in the form');
      return;
    }

    if (!this.employee) {
      this.notificationService.showError('Employee data not loaded');
      return;
    }

    const updatedEmployee: Employee = {
      ...this.employee,
      ...this.profileForm.value
    };

    this.loading = true;
    this.employeeService.updateEmployee(updatedEmployee).subscribe({
      next: (data) => {
        this.employee = data;
        this.isEditing = false;
        this.loading = false;
        this.notificationService.showSuccess('Profile updated successfully');
        
        // Update display name in the UI if name was changed
        if (this.authService.getCurrentUser()) {
          const currentUser = this.authService.getCurrentUser()!;
          const fullName = `${data.firstName} ${data.lastName}`;
          if (currentUser.fullName !== fullName) {
            currentUser.fullName = fullName;
            this.authService.setCurrentUser(currentUser);
          }
        }
      },
      error: (error) => {
        console.error('Error updating employee profile', error);
        this.notificationService.showError('Failed to update your profile');
        this.loading = false;
      }
    });
  }
}
