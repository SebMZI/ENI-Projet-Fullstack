import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PromotionDetails } from './promotion-details';

describe('PromotionDetails', () => {
    let component: PromotionDetails;
    let fixture: ComponentFixture<PromotionDetails>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [PromotionDetails],
        }).compileComponents();

        fixture = TestBed.createComponent(PromotionDetails);
        component = fixture.componentInstance;
        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
