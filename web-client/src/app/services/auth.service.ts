import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { map, tap, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { LoginRequest, LoginResponse } from '../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = '/api/auth';
  private currentUserSource = new BehaviorSubject<LoginResponse | null>(null);
  currentUser$ = this.currentUserSource.asObservable();
  private tokenExpirationTimer: any;

  constructor(private http: HttpClient, private router: Router) {
    this.loadStoredUser();
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, loginRequest).pipe(
      tap(response => {
        this.setCurrentUser(response);
        this.startTokenExpirationTimer(response);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('user');
    this.currentUserSource.next(null);
    this.router.navigate(['/login']);
    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
      this.tokenExpirationTimer = null;
    }
  }

  refreshToken(): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/refresh-token`, {}).pipe(
      tap(response => {
        this.setCurrentUser(response);
        this.startTokenExpirationTimer(response);
      }),
      catchError(error => {
        this.logout();
        return of(null as any);
      })
    );
  }

  setCurrentUser(user: LoginResponse): void {
    if (user) {
      user.roles = this.getDecodedRoles(user.token);
      localStorage.setItem('user', JSON.stringify(user));
      this.currentUserSource.next(user);
    }
  }

  getToken(): string {
    const user = this.getCurrentUser();
    return user ? user.token : '';
  }

  getCurrentUser(): LoginResponse | null {
    return this.currentUserSource.value;
  }

  isLoggedIn(): boolean {
    return !!this.currentUserSource.value;
  }

  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return user ? user.roles.includes(role) : false;
  }

  isAdmin(): boolean {
    return this.hasRole('ROLE_ADMIN');
  }

  isManager(): boolean {
    return this.hasRole('ROLE_MANAGER');
  }

  private loadStoredUser(): void {
    const userString = localStorage.getItem('user');
    if (userString) {
      const user: LoginResponse = JSON.parse(userString);
      this.setCurrentUser(user);
      this.startTokenExpirationTimer(user);
    }
  }

  private startTokenExpirationTimer(user: LoginResponse): void {
    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }

    const expiresInMs = user.expiresIn * 1000;
    this.tokenExpirationTimer = setTimeout(() => {
      this.refreshToken().subscribe();
    }, expiresInMs - 60000); // Refresh 1 minute before expiration
  }

  private getDecodedRoles(token: string): string[] {
    if (!token) return [];
    
    const tokenParts = token.split('.');
    if (tokenParts.length !== 3) return [];
    
    try {
      const payloadBase64 = tokenParts[1];
      const normalizedPayload = payloadBase64
        .replace(/-/g, '+')
        .replace(/_/g, '/');
      const payload = JSON.parse(atob(normalizedPayload));
      
      return payload.roles || [];
    } catch (e) {
      console.error('Error parsing JWT token', e);
      return [];
    }
  }
}
