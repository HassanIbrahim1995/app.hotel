import { Employee } from './employee.model';
import { Shift } from './shift.model';

export interface CalendarEntry {
  id?: number;
  title: string;
  startDate: string;
  endDate: string;
  description?: string;
  entryType: string; // SHIFT, VACATION, HOLIDAY, etc.
  employee?: Employee;
  shift?: Shift;
  color?: string;
  allDay: boolean;
}
