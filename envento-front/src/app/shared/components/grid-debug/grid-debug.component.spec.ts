import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GridDebugComponent } from './grid-debug.component';

describe('GridDebugComponent', () => {
  let component: GridDebugComponent;
  let fixture: ComponentFixture<GridDebugComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GridDebugComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GridDebugComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
