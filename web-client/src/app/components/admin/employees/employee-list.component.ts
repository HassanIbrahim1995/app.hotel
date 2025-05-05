import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Employee } from '../../../models/employee.model';
import { EmployeeService } from '../../../services/employee.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  employees: Employee[] = [];
  loading = false;
  searchTerm = '';

  constructor(
    private employeeService: EmployeeService,
    private notificationService: NotificationService,
    private modalService: NgbModal,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.loading = true;
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        this.employees = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching employees', error);
        this.notificationService.showError('Failed to load employees');
        this.loading = false;
      }
    });
  }

  openAddEmployeeModal(content: any): void {
    this.modalService.open(content, { size: 'lg', centered: true });
  }

  editEmployee(employeeId: number): void {
    this.router.navigate(['/admin/employees/edit', employeeId]);
  }

  deleteEmployee(employeeId: number): void {
    if (confirm('Are you sure you want to delete this employee?')) {
      this.employeeService.deleteEmployee(employeeId).subscribe({
        next: () => {
          this.loadEmployees();
          this.notificationService.showSuccess('Employee deleted successfully');
        },
        error: (error) => {
          console.error('Error deleting employee', error);
          this.notificationService.showError('Failed to delete employee');
        }
      });
    }
  }

  applyFilter(): void {
    if (this.searchTerm.trim() === '') {
      this.loadEmployees();
    } else {
      this.employeeService.searchEmployees(this.searchTerm).subscribe({
        next: (data) => {
          this.employees = data;
        },
        error: (error) => {
          console.error('Error searching employees', error);
          this.notificationService.showError('Failed to search employees');
        }
      });
    }
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.loadEmployees();
  }
}
