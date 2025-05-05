import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VacationRequest } from '../../../models/vacation-request.model';
import { VacationRequestService } from '../../../services/vacation-request.service';

// Declare Bootstrap Modal for event details
declare var bootstrap: any;

@Component({
  selector: 'app-vacation-requests',
  templateUrl: './vacation-requests.component.html',
  styleUrls: ['./vacation-requests.component.scss']
})
export class VacationRequestsComponent implements OnInit {
  allRequests: VacationRequest[] = [];
  filteredRequests: VacationRequest[] = [];
  selectedRequest: VacationRequest | null = null;
  activeTab = 'all';
  
  vacationForm: FormGroup;
  submitted = false;
  loading = false;
  
  constructor(
    private vacationRequestService: VacationRequestService,
    private formBuilder: FormBuilder
  ) {
    this.vacationForm = this.formBuilder.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      reason: [''],
    }, { validators: this.dateRangeValidator });
  }

  ngOnInit(): void {
    this.loadVacationRequests();
  }

  loadVacationRequests(): void {
    this.vacationRequestService.getMyVacationRequests().subscribe(requests => {
      this.allRequests = requests;
      this.filterRequests(this.activeTab);
    });
  }

  filterRequests(status: string): void {
    this.activeTab = status;
    
    if (status === 'all') {
      this.filteredRequests = this.allRequests;
    } else {
      const statusMap: any = {
        'pending': 'PENDING',
        'approved': 'APPROVED',
        'rejected': 'REJECTED'
      };
      
      this.filteredRequests = this.allRequests.filter(request => 
        request.status === statusMap[status]
      );
    }
    
    // Sort by start date (most recent first)
    this.filteredRequests.sort((a, b) => 
      new Date(b.startDate).getTime() - new Date(a.startDate).getTime()
    );
  }

  getPendingCount(): number {
    return this.allRequests.filter(r => r.status === 'PENDING').length;
  }

  getApprovedCount(): number {
    return this.allRequests.filter(r => r.status === 'APPROVED').length;
  }

  getRejectedCount(): number {
    return this.allRequests.filter(r => r.status === 'REJECTED').length;
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'PENDING': return 'bg-warning text-dark';
      case 'APPROVED': return 'bg-success';
      case 'REJECTED': return 'bg-danger';
      default: return 'bg-secondary';
    }
  }

  getStatusHeaderClass(status: string): string {
    switch (status) {
      case 'PENDING': return 'bg-warning text-dark';
      case 'APPROVED': return 'bg-success text-white';
      case 'REJECTED': return 'bg-danger text-white';
      default: return 'bg-secondary text-white';
    }
  }

  calculateDays(startDate: string, endDate: string): number {
    const start = new Date(startDate);
    start.setHours(0, 0, 0, 0);
    
    const end = new Date(endDate);
    end.setHours(0, 0, 0, 0);
    
    // Calculate the difference in days
    const diffTime = Math.abs(end.getTime() - start.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1; // Include both start and end days
    
    return diffDays;
  }

  openNewRequestModal(): void {
    // Reset form before opening modal
    this.vacationForm.reset();
    this.submitted = false;
    
    // Set default dates (today and tomorrow)
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1);
    
    this.vacationForm.patchValue({
      startDate: this.formatDateForInput(today),
      endDate: this.formatDateForInput(tomorrow)
    });
    
    // Open the modal
    const modalElement = document.getElementById('newRequestModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  viewRequestDetails(request: VacationRequest): void {
    this.selectedRequest = request;
    
    // Open the modal
    const modalElement = document.getElementById('requestDetailsModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  cancelRequest(id: number | undefined): void {
    if (!id) return;
    
    if (confirm('Are you sure you want to cancel this vacation request?')) {
      this.vacationRequestService.cancelVacationRequest(id).subscribe(() => {
        // Close any open modals
        const detailsModalElement = document.getElementById('requestDetailsModal');
        if (detailsModalElement) {
          const detailsModal = bootstrap.Modal.getInstance(detailsModalElement);
          if (detailsModal) {
            detailsModal.hide();
          }
        }
        
        // Reload vacation requests
        this.loadVacationRequests();
      });
    }
  }

  submitVacationRequest(): void {
    this.submitted = true;
    
    // stop here if form is invalid
    if (this.vacationForm.invalid) {
      return;
    }
    
    this.loading = true;
    
    const request = {
      startDate: this.f.startDate.value,
      endDate: this.f.endDate.value,
      reason: this.f.reason.value,
      // These fields would typically be handled by the backend
      requestDate: new Date().toISOString(),
      status: 'PENDING'
    };
    
    this.vacationRequestService.createVacationRequest(request as any).subscribe({
      next: () => {
        // Close the modal
        const modalElement = document.getElementById('newRequestModal');
        if (modalElement) {
          const modal = bootstrap.Modal.getInstance(modalElement);
          if (modal) {
            modal.hide();
          }
        }
        
        // Reset form
        this.vacationForm.reset();
        this.submitted = false;
        this.loading = false;
        
        // Reload vacation requests
        this.loadVacationRequests();
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  // Convenience getter for easy access to form fields
  get f() { return this.vacationForm.controls; }

  dateRangeValidator(group: FormGroup): { [key: string]: boolean } | null {
    const startDate = group.get('startDate')?.value;
    const endDate = group.get('endDate')?.value;
    
    if (startDate && endDate) {
      const start = new Date(startDate);
      const end = new Date(endDate);
      
      if (end < start) {
        return { 'dateRange': true };
      }
    }
    
    return null;
  }

  private formatDateForInput(date: Date): string {
    // Format date as YYYY-MM-DD for input[type=date]
    return date.toISOString().split('T')[0];
  }
}
