import {
  Component, ElementRef, OnInit,
  signal, ViewChild,
} from '@angular/core';


/**
 * Goal of this component is to provide a way to set and display a grid used for layout.
 * As in figma, is would great to be able to :
 * - Set a horizontal grid, with :
 *    - Dynamic column amount
 *    - Dynamic column width
 *    - Dynamic alignment
 *    - Dynamic gutter/gap value
 * - Set a vertical grid with the same requirements
 * - Each grid would have their specific settings and adjustable color
 */

@Component({
  selector: 'app-grid-debug',
  standalone: true,
  imports: [],
  templateUrl: './grid-debug.component.html',
  styleUrl: 'grid-debug.component.css'
})
export class GridDebugComponent implements OnInit {
  @ViewChild('gridContainer') readonly gridContainer!: ElementRef<HTMLDivElement>;
  readonly isDisplayed = signal(true);
  readonly isSideMarginApplied = signal(false);
  readonly columnAmount = signal(12);

  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  public toggleIsDisplayValue(): void {
    this.isDisplayed.set(!this.isDisplayed());
  }

  public toggleSideMarginApplication(): void {
    const grid = this.gridContainer.nativeElement;

    this.isSideMarginApplied.set(!this.isSideMarginApplied());
    if (this.isSideMarginApplied()) grid.classList.add('side-margin');
    else grid.classList.remove('side-margin');
  }
}
