import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Filiere } from './filiere';

describe('Filiere', () => {
  let component: Filiere;
  let fixture: ComponentFixture<Filiere>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Filiere]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Filiere);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
