import {Routes} from '@angular/router';

export const authRoutes: Routes = [
  {
    path: "authentication",
    loadComponent: () =>
      import("./pages/authentication/authentication.component").then(c => c.AuthenticationComponent)
  },
];
