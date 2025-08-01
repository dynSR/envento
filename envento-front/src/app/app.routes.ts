import {Routes} from '@angular/router';
import {authRoutes} from './features/auth/auth.routes';
import {eventsRoutes} from './features/events/events.routes';
import {usersRoutes} from './features/users/users.routes';

export const routes: Routes = [
  ...authRoutes,
  {
    path: "",
    loadComponent: () =>
      import('./features/home/home.component').then(c => c.HomeComponent)
  },
  {
    path: "home", redirectTo: ""
  },
  ...usersRoutes,
  ...eventsRoutes,
];
