import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { logginGuardGuard } from './loggin-guard.guard';

describe('logginGuardGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => logginGuardGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
