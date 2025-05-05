import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Location } from '../models/location.model';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private baseUrl = '/api/locations';

  constructor(private http: HttpClient) { }

  getAllLocations(): Observable<Location[]> {
    return this.http.get<Location[]>(this.baseUrl);
  }

  getLocationById(id: number): Observable<Location> {
    return this.http.get<Location>(`${this.baseUrl}/${id}`);
  }

  createLocation(location: Location): Observable<Location> {
    return this.http.post<Location>(this.baseUrl, location);
  }

  updateLocation(location: Location): Observable<Location> {
    return this.http.put<Location>(`${this.baseUrl}/${location.id}`, location);
  }

  deleteLocation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getActiveLocations(): Observable<Location[]> {
    return this.http.get<Location[]>(`${this.baseUrl}/active`);
  }
}
