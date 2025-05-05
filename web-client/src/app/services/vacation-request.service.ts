import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VacationRequest } from '../models/vacation-request.model';

@Injectable({
  providedIn: 'root'
})
export class VacationRequestService {
  private baseUrl = '/api/vacation-requests';

  constructor(private http: HttpClient) { }

  getMyVacationRequests(status?: string): Observable<VacationRequest[]> {
    let params = new HttpParams();
    if (status) params = params.append('status', status);
    
    return this.http.get<VacationRequest[]>(`${this.baseUrl}/my`, { params });
  }

  getTeamVacationRequests(status?: string): Observable<VacationRequest[]> {
    let params = new HttpParams();
    if (status) params = params.append('status', status);
    
    return this.http.get<VacationRequest[]>(`${this.baseUrl}/team`, { params });
  }

  getVacationRequestsByEmployee(employeeId: number, status?: string): Observable<VacationRequest[]> {
    let params = new HttpParams();
    if (status) params = params.append('status', status);
    
    return this.http.get<VacationRequest[]>(`${this.baseUrl}/employee/${employeeId}`, { params });
  }

  getVacationRequestById(id: number): Observable<VacationRequest> {
    return this.http.get<VacationRequest>(`${this.baseUrl}/${id}`);
  }

  createVacationRequest(request: VacationRequest): Observable<VacationRequest> {
    return this.http.post<VacationRequest>(this.baseUrl, request);
  }

  approveVacationRequest(id: number, notes?: string): Observable<VacationRequest> {
    return this.http.post<VacationRequest>(`${this.baseUrl}/${id}/approve`, { notes });
  }

  rejectVacationRequest(id: number, notes?: string): Observable<VacationRequest> {
    return this.http.post<VacationRequest>(`${this.baseUrl}/${id}/reject`, { notes });
  }

  cancelVacationRequest(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
