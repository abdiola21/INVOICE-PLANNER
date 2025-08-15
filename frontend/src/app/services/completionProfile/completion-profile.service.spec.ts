import { TestBed } from '@angular/core/testing';

import { CompletionProfileService } from './completion-profile.service';

describe('CompletionProfileService', () => {
  let service: CompletionProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompletionProfileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

