import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { loginReverseGuard } from './login-reverse.guard';

describe('loginReverseGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => loginReverseGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
