import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomMultipleSelect } from './custom-multiple-select';

describe('CustomMultipleSelect', () => {
  let component: CustomMultipleSelect;
  let fixture: ComponentFixture<CustomMultipleSelect>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomMultipleSelect],
    }).compileComponents();

    fixture = TestBed.createComponent(CustomMultipleSelect);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
