import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Cursus } from './cursus';

describe('Cursus', () => {
  let component: Cursus;
  let fixture: ComponentFixture<Cursus>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Cursus],
    }).compileComponents();

    fixture = TestBed.createComponent(Cursus);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
