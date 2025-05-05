import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Injectable({ providedIn: 'root' })
export class ManagerGuard implements CanActivate {
  constructor(private router: Router, private authService: AuthService) {}

  canActivate() {
    const isManager = this.authService.isManager() || this.authService.isAdmin();
    if (isManager) {
      // User is a manager or admin
      return true;
    }

    // Not a manager, redirect to dashboard
    this.router.navigate(['/dashboard']);
    return false;
  }
}
