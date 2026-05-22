import { TestBed } from '@angular/core/testing';

import { Filiere } from './filiere';

describe('Filiere', () => {
  let service: Filiere;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Filiere);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
