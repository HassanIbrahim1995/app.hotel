import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'Shift Manager';
  isLoggedIn = false;
  isAdmin = false;
  isManager = false;
  userFullName = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.isLoggedIn = !!user;
      if (user) {
        this.userFullName = user.firstName + ' ' + user.lastName;
        this.isAdmin = user.roles.includes('ROLE_ADMIN');
        this.isManager = user.roles.includes('ROLE_MANAGER');
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
