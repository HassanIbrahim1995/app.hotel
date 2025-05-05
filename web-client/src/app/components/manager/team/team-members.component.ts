import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Employee } from '../../../models/employee.model';
import { EmployeeService } from '../../../services/employee.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-team-members',
  templateUrl: './team-members.component.html',
  styleUrls: ['./team-members.component.scss']
})
export class TeamMembersComponent implements OnInit {
  teamMembers: Employee[] = [];
  loading = false;
  selectedEmployee: Employee | null = null;
  searchTerm = '';
  
  constructor(
    private employeeService: EmployeeService,
    private notificationService: NotificationService,
    private modalService: NgbModal
  ) { }

  ngOnInit(): void {
    this.loadTeamMembers();
  }

  loadTeamMembers(): void {
    this.loading = true;
    this.employeeService.getCurrentEmployee().subscribe({
      next: (manager) => {
        if (manager.id) {
          this.employeeService.getEmployeesByManager(manager.id).subscribe({
            next: (data) => {
              this.teamMembers = data;
              this.loading = false;
            },
            error: (error) => {
              console.error('Error fetching team members', error);
              this.notificationService.showError('Failed to load team members');
              this.loading = false;
            }
          });
        } else {
          this.loading = false;
          this.notificationService.showError('Cannot identify current manager');
        }
      },
      error: (error) => {
        console.error('Error fetching current manager', error);
        this.notificationService.showError('Failed to load manager data');
        this.loading = false;
      }
    });
  }

  openEmployeeDetailsModal(content: any, employee: Employee): void {
    this.selectedEmployee = employee;
    this.modalService.open(content, { size: 'lg', centered: true });
  }

  applyFilter(): void {
    if (this.searchTerm.trim() === '') {
      this.loadTeamMembers();
    } else {
      this.loading = true;
      this.employeeService.searchEmployees(this.searchTerm).subscribe({
        next: (data) => {
          // Filter search results to only include team members
          this.teamMembers = data;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error searching employees', error);
          this.notificationService.showError('Failed to search employees');
          this.loading = false;
        }
      });
    }
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.loadTeamMembers();
  }
}
