import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
  constructor(private router: Router, private authService: AuthService) {}

  canActivate() {
    const isAdmin = this.authService.isAdmin();
    if (isAdmin) {
      // User is an admin
      return true;
    }

    // Not an admin, redirect to dashboard
    this.router.navigate(['/dashboard']);
    return false;
  }
}
