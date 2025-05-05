import { Location } from './location.model';
import { ShiftType } from './shift-type.model';

export interface Shift {
  id?: number;
  shiftDate: string;
  startTime: string;
  endTime: string;
  location: Location;
  shiftType: ShiftType;
  note?: string;
  createdById: number;
  createdAt?: string;
  updatedAt?: string;
}
