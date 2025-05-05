import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Notification {
  id: number;
  userId: number;
  message: string;
  type: string;
  read: boolean;
  createdAt: string;
  relatedEntityId?: number;
  relatedEntityType?: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private baseUrl = '/api/notifications';

  constructor(private http: HttpClient) { }

  getMyNotifications(unreadOnly = false): Observable<Notification[]> {
    const params = unreadOnly ? { unreadOnly: 'true' } : {};
    return this.http.get<Notification[]>(`${this.baseUrl}`, { params });
  }

  markAsRead(id: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${id}/read`, {});
  }

  markAllAsRead(): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/read-all`, {});
  }

  deleteNotification(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  clearAllNotifications(): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/clear-all`);
  }
}
