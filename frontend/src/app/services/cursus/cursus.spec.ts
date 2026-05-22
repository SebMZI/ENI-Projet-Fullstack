import { TestBed } from '@angular/core/testing';

import { Cursus } from './cursus';

describe('Cursus', () => {
  let service: Cursus;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Cursus);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
