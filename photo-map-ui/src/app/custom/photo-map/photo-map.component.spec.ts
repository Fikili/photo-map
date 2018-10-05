import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PhotoMapComponent } from './photo-map.component';

describe('PhotoMapComponent', () => {
  let component: PhotoMapComponent;
  let fixture: ComponentFixture<PhotoMapComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PhotoMapComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PhotoMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
