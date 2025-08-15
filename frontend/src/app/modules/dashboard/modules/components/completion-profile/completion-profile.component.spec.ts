import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompletionProfileComponent } from './completion-profile.component';

describe('CompletionProfileComponent', () => {
  let component: CompletionProfileComponent;
  let fixture: ComponentFixture<CompletionProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CompletionProfileComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompletionProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

