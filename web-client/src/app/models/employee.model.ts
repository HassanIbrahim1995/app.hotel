import { Person } from './person.model';

export interface Employee extends Person {
  employeeNumber: string;
  position: string;
  department: string;
  hireDate: string;
  managerId?: number;
  managerName?: string;
  hourlyRate?: number;
  fullTime: boolean;
  maxHoursPerWeek: number;
  note?: string;
}
