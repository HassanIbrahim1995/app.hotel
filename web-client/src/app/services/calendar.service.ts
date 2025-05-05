import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CalendarEntry } from '../models/calendar-entry.model';

@Injectable({
  providedIn: 'root'
})
export class CalendarService {
  private baseUrl = '/api/calendar';

  constructor(private http: HttpClient) { }

  getPersonalCalendar(startDate?: string, endDate?: string): Observable<CalendarEntry[]> {
    let params = new HttpParams();
    if (startDate) params = params.append('startDate', startDate);
    if (endDate) params = params.append('endDate', endDate);
    
    return this.http.get<CalendarEntry[]>(`${this.baseUrl}/personal`, { params });
  }

  getTeamCalendar(startDate?: string, endDate?: string): Observable<CalendarEntry[]> {
    let params = new HttpParams();
    if (startDate) params = params.append('startDate', startDate);
    if (endDate) params = params.append('endDate', endDate);
    
    return this.http.get<CalendarEntry[]>(`${this.baseUrl}/team`, { params });
  }

  getEmployeeCalendar(employeeId: number, startDate?: string, endDate?: string): Observable<CalendarEntry[]> {
    let params = new HttpParams();
    if (startDate) params = params.append('startDate', startDate);
    if (endDate) params = params.append('endDate', endDate);
    
    return this.http.get<CalendarEntry[]>(`${this.baseUrl}/employee/${employeeId}`, { params });
  }

  createCalendarEntry(entry: CalendarEntry): Observable<CalendarEntry> {
    return this.http.post<CalendarEntry>(this.baseUrl, entry);
  }

  updateCalendarEntry(entry: CalendarEntry): Observable<CalendarEntry> {
    return this.http.put<CalendarEntry>(`${this.baseUrl}/${entry.id}`, entry);
  }

  deleteCalendarEntry(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
