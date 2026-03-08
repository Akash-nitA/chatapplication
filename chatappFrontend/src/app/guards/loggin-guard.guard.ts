import { inject } from '@angular/core';
import { CanActivateFn , ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import { LoginServiceService } from '../service/login-service.service';

export const logginGuardGuard: CanActivateFn = (route, state) => {
  const router =inject(Router);
  const authService=inject(LoginServiceService);
  console.log("login guard value ", authService.isAuthenticated());
  return authService.isAuthenticated()? true: router.parseUrl('/login');
};
