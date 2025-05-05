import { Employee } from './employee.model';
import { Shift } from './shift.model';

export interface EmployeeShift {
  id?: number;
  employee: Employee;
  shift: Shift;
  assignedBy?: number;
  assignedAt?: string;
  status: string; // ASSIGNED, CONFIRMED, DECLINED, COMPLETED
  clockInTime?: string;
  clockOutTime?: string;
  note?: string;
}
