import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee } from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private baseUrl = '/api/employees';

  constructor(private http: HttpClient) { }

  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.baseUrl);
  }

  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.baseUrl}/${id}`);
  }

  getCurrentEmployee(): Observable<Employee> {
    return this.http.get<Employee>(`${this.baseUrl}/me`);
  }

  createEmployee(employee: Employee): Observable<Employee> {
    return this.http.post<Employee>(this.baseUrl, employee);
  }

  updateEmployee(employee: Employee): Observable<Employee> {
    return this.http.put<Employee>(`${this.baseUrl}/${employee.id}`, employee);
  }

  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getEmployeesByManager(managerId: number): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.baseUrl}/manager/${managerId}`);
  }

  searchEmployees(query: string): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.baseUrl}/search?query=${query}`);
  }
}
