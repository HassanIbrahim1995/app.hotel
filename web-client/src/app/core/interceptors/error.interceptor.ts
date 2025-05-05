import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private router: Router) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Redirect to login page on unauthorized
          this.router.navigate(['/login']);
        }
        
        // Get custom error message or use default
        let errorMessage = 'An unexpected error occurred';
        
        if (error.error instanceof ErrorEvent) {
          // Client-side error
          errorMessage = `Error: ${error.error.message}`;
        } else if (typeof error.error === 'object' && error.error !== null) {
          // Server-side error with message
          errorMessage = error.error.message || error.error.error || errorMessage;
        } else if (typeof error.error === 'string' && error.error) {
          // Plain text error
          errorMessage = error.error;
        } else if (error.status) {
          // HTTP status code based error
          switch (error.status) {
            case 400: errorMessage = 'Bad Request'; break;
            case 403: errorMessage = 'Access Forbidden'; break;
            case 404: errorMessage = 'Resource Not Found'; break;
            case 500: errorMessage = 'Internal Server Error'; break;
            default: errorMessage = `Error Code: ${error.status}`;
          }
        }
        
        // We can add a toast notification here if needed
        console.error('API Error:', errorMessage, error);
        
        return throwError(() => new Error(errorMessage));
      })
    );
  }
}
