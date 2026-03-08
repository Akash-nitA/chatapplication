import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  router = inject(Router);
logout() {
  localStorage.removeItem('token');
  this.router.navigate(['/login']);
}

}
