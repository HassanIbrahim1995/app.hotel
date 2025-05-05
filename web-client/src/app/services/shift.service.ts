import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Shift } from '../models/shift.model';
import { EmployeeShift } from '../models/employee-shift.model';
import { ShiftType } from '../models/shift-type.model';

@Injectable({
  providedIn: 'root'
})
export class ShiftService {
  private baseUrl = '/api/shifts';

  constructor(private http: HttpClient) { }

  getAllShifts(startDate?: string, endDate?: string): Observable<Shift[]> {
    let params = new HttpParams();
    if (startDate) params = params.append('startDate', startDate);
    if (endDate) params = params.append('endDate', endDate);
    
    return this.http.get<Shift[]>(this.baseUrl, { params });
  }

  getShiftById(id: number): Observable<Shift> {
    return this.http.get<Shift>(`${this.baseUrl}/${id}`);
  }

  createShift(shift: Shift): Observable<Shift> {
    return this.http.post<Shift>(this.baseUrl, shift);
  }

  updateShift(shift: Shift): Observable<Shift> {
    return this.http.put<Shift>(`${this.baseUrl}/${shift.id}`, shift);
  }

  deleteShift(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // Methods for shifts by location and shift type
  getShiftsByLocation(locationId: number, startDate?: string, endDate?: string): Observable<Shift[]> {
    let params = new HttpParams();
    if (startDate) params = params.append('startDate', startDate);
    if (endDate) params = params.append('endDate', endDate);
    
    return this.http.get<Shift[]>(`${this.baseUrl}/location/${locationId}`, { params });
  }

  getShiftsByShiftType(shiftTypeId: number, startDate?: string, endDate?: string): Observable<Shift[]> {
    let params = new HttpParams();
    if (startDate) params = params.append('startDate', startDate);
    if (endDate) params = params.append('endDate', endDate);
    
    return this.http.get<Shift[]>(`${this.baseUrl}/shift-type/${shiftTypeId}`, { params });
  }

  // Employee Shifts
  getEmployeeShifts(employeeId: number, startDate?: string, endDate?: string): Observable<EmployeeShift[]> {
    let params = new HttpParams();
    if (startDate) params = params.append('startDate', startDate);
    if (endDate) params = params.append('endDate', endDate);
    
    return this.http.get<EmployeeShift[]>(`${this.baseUrl}/employee/${employeeId}`, { params });
  }

  getMyShifts(startDate?: string, endDate?: string): Observable<EmployeeShift[]> {
    let params = new HttpParams();
    if (startDate) params = params.append('startDate', startDate);
    if (endDate) params = params.append('endDate', endDate);
    
    return this.http.get<EmployeeShift[]>(`${this.baseUrl}/employee/current`, { params });
  }

  assignShift(shiftId: number, employeeId: number): Observable<boolean> {
    return this.http.post<boolean>(`${this.baseUrl}/${shiftId}/assign/${employeeId}`, {});
  }

  confirmShift(employeeShiftId: number): Observable<EmployeeShift> {
    return this.http.post<EmployeeShift>(`${this.baseUrl}/confirm/${employeeShiftId}`, {});
  }

  declineShift(employeeShiftId: number, reason?: string): Observable<EmployeeShift> {
    return this.http.post<EmployeeShift>(`${this.baseUrl}/decline/${employeeShiftId}`, { reason });
  }

  clockIn(employeeShiftId: number): Observable<EmployeeShift> {
    return this.http.post<EmployeeShift>(`${this.baseUrl}/clock-in/${employeeShiftId}`, {});
  }

  clockOut(employeeShiftId: number): Observable<EmployeeShift> {
    return this.http.post<EmployeeShift>(`${this.baseUrl}/clock-out/${employeeShiftId}`, {});
  }
}
