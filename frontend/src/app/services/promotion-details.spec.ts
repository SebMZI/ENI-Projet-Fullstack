import { TestBed } from '@angular/core/testing';

import { PromotionDetails } from './promotion-details';

describe('PromotionDetails', () => {
    let service: PromotionDetails;

    beforeEach(() => {
        TestBed.configureTestingModule({});
        service = TestBed.inject(PromotionDetails);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
});
