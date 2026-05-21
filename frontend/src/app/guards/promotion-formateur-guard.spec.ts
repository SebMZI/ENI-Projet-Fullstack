import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { promotionFormateurGuard } from './promotion-formateur-guard';

describe('promotionFormateurGuard', () => {
    const executeGuard: CanActivateFn = (...guardParameters) =>
        TestBed.runInInjectionContext(() => promotionFormateurGuard(...guardParameters));

    beforeEach(() => {
        TestBed.configureTestingModule({});
    });

    it('should be created', () => {
        expect(executeGuard).toBeTruthy();
    });
});
