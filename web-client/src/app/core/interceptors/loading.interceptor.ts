import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, finalize } from 'rxjs';
import { NgxSpinnerService } from 'ngx-spinner';

@Injectable()
export class LoadingInterceptor implements HttpInterceptor {
  private activeRequests = 0;
  private excludedUrls: string[] = [
    '/api/notifications'
  ];

  constructor(private spinnerService: NgxSpinnerService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    // Skip spinner for certain URLs
    if (this.shouldShowSpinner(request.url)) {
      this.activeRequests++;
      this.spinnerService.show();
    }

    return next.handle(request).pipe(
      finalize(() => {
        if (this.shouldShowSpinner(request.url)) {
          this.activeRequests--;
          if (this.activeRequests === 0) {
            this.spinnerService.hide();
          }
        }
      })
    );
  }

  private shouldShowSpinner(url: string): boolean {
    return !this.excludedUrls.some(excludedUrl => url.includes(excludedUrl));
  }
}
