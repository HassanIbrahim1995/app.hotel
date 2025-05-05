import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReportParams {
  startDate?: string;
  endDate?: string;
  employeeId?: number;
  locationId?: number;
  reportType?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private baseUrl = '/api/reports';

  constructor(private http: HttpClient) { }

  generateScheduleReport(params: ReportParams): Observable<Blob> {
    let httpParams = this.buildParams(params);
    
    return this.http.get(`${this.baseUrl}/schedule`, {
      params: httpParams,
      responseType: 'blob'
    });
  }

  generateHoursReport(params: ReportParams): Observable<Blob> {
    let httpParams = this.buildParams(params);
    
    return this.http.get(`${this.baseUrl}/hours`, {
      params: httpParams,
      responseType: 'blob'
    });
  }

  generateAttendanceReport(params: ReportParams): Observable<Blob> {
    let httpParams = this.buildParams(params);
    
    return this.http.get(`${this.baseUrl}/attendance`, {
      params: httpParams,
      responseType: 'blob'
    });
  }

  generateVacationReport(params: ReportParams): Observable<Blob> {
    let httpParams = this.buildParams(params);
    
    return this.http.get(`${this.baseUrl}/vacation`, {
      params: httpParams,
      responseType: 'blob'
    });
  }

  private buildParams(params: ReportParams): HttpParams {
    let httpParams = new HttpParams();
    
    if (params.startDate) httpParams = httpParams.append('startDate', params.startDate);
    if (params.endDate) httpParams = httpParams.append('endDate', params.endDate);
    if (params.employeeId) httpParams = httpParams.append('employeeId', params.employeeId.toString());
    if (params.locationId) httpParams = httpParams.append('locationId', params.locationId.toString());
    if (params.reportType) httpParams = httpParams.append('reportType', params.reportType);
    
    return httpParams;
  }
}
