import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {GridDebugComponent} from './shared/components/grid-debug/grid-debug.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, GridDebugComponent],
  template: `
    <app-grid-debug/>
    <router-outlet/>
  `
})
export class AppComponent implements OnInit {
  title = 'front';

  ngOnInit() {
    console.log(this.title)
  }
}
