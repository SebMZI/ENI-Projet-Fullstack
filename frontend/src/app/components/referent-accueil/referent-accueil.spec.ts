import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReferentAccueil } from './referent-accueil';

describe('ReferentAccueil', () => {
  let component: ReferentAccueil;
  let fixture: ComponentFixture<ReferentAccueil>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReferentAccueil],
    }).compileComponents();

    fixture = TestBed.createComponent(ReferentAccueil);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
