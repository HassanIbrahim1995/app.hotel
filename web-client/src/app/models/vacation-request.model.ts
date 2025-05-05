import { Employee } from './employee.model';

export interface VacationRequest {
  id?: number;
  employee: Employee;
  startDate: string;
  endDate: string;
  requestDate: string;
  reason?: string;
  status: string; // PENDING, APPROVED, REJECTED
  reviewedBy?: number;
  reviewDate?: string;
  reviewNotes?: string;
}
