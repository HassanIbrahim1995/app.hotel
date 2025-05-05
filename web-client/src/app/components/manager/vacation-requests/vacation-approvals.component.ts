import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { VacationRequest } from '../../../models/vacation-request.model';
import { VacationRequestService } from '../../../services/vacation-request.service';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-vacation-approvals',
  templateUrl: './vacation-approvals.component.html',
  styleUrls: ['./vacation-approvals.component.scss']
})
export class VacationApprovalsComponent implements OnInit {
  vacationRequests: VacationRequest[] = [];
  filteredRequests: VacationRequest[] = [];
  loading = false;
  selectedRequest: VacationRequest | null = null;
  reviewNotes: string = '';
  filterStatus: string = 'PENDING';
  
  constructor(
    private vacationRequestService: VacationRequestService,
    private notificationService: NotificationService,
    private modalService: NgbModal
  ) { }

  ngOnInit(): void {
    this.loadVacationRequests();
  }

  loadVacationRequests(): void {
    this.loading = true;
    this.vacationRequestService.getTeamVacationRequests(this.filterStatus).subscribe({
      next: (data) => {
        this.vacationRequests = data;
        this.filteredRequests = [...data];
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching vacation requests', error);
        this.notificationService.showError('Failed to load vacation requests');
        this.loading = false;
      }
    });
  }

  openReviewModal(content: any, request: VacationRequest): void {
    this.selectedRequest = request;
    this.reviewNotes = '';
    this.modalService.open(content, { centered: true });
  }

  approveRequest(): void {
    if (!this.selectedRequest) return;
    
    this.vacationRequestService.approveVacationRequest(this.selectedRequest.id!, this.reviewNotes).subscribe({
      next: (data) => {
        this.loadVacationRequests();
        this.modalService.dismissAll();
        this.notificationService.showSuccess('Vacation request approved successfully');
      },
      error: (error) => {
        console.error('Error approving vacation request', error);
        this.notificationService.showError('Failed to approve vacation request');
      }
    });
  }

  rejectRequest(): void {
    if (!this.selectedRequest) return;
    
    this.vacationRequestService.rejectVacationRequest(this.selectedRequest.id!, this.reviewNotes).subscribe({
      next: (data) => {
        this.loadVacationRequests();
        this.modalService.dismissAll();
        this.notificationService.showSuccess('Vacation request rejected successfully');
      },
      error: (error) => {
        console.error('Error rejecting vacation request', error);
        this.notificationService.showError('Failed to reject vacation request');
      }
    });
  }

  calculateDays(startDate: string, endDate: string): number {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const diffTime = Math.abs(end.getTime() - start.getTime());
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1; // +1 to include both start and end days
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'PENDING': return 'bg-warning';
      case 'APPROVED': return 'bg-success';
      case 'REJECTED': return 'bg-danger';
      default: return 'bg-secondary';
    }
  }
}
