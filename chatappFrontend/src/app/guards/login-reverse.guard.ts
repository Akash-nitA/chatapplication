import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginServiceService } from '../service/login-service.service';

export const loginReverseGuard: CanActivateFn = (route, state) => {
  const authService = inject(LoginServiceService);
  const router = inject(Router);
  return authService.isAuthenticated() ? router.parseUrl('/dashboard') : true;
};
