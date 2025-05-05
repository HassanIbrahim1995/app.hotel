import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ShiftType } from '../models/shift-type.model';

@Injectable({
  providedIn: 'root'
})
export class ShiftTypeService {
  private baseUrl = '/api/shift-types';

  constructor(private http: HttpClient) { }

  getAllShiftTypes(): Observable<ShiftType[]> {
    return this.http.get<ShiftType[]>(this.baseUrl);
  }

  getShiftTypeById(id: number): Observable<ShiftType> {
    return this.http.get<ShiftType>(`${this.baseUrl}/${id}`);
  }

  createShiftType(shiftType: ShiftType): Observable<ShiftType> {
    return this.http.post<ShiftType>(this.baseUrl, shiftType);
  }

  updateShiftType(shiftType: ShiftType): Observable<ShiftType> {
    return this.http.put<ShiftType>(`${this.baseUrl}/${shiftType.id}`, shiftType);
  }

  deleteShiftType(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getActiveShiftTypes(): Observable<ShiftType[]> {
    return this.http.get<ShiftType[]>(`${this.baseUrl}/active`);
  }
}
