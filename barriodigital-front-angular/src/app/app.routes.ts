import { Routes } from '@angular/router';
import { MsalGuard } from '@azure/msal-angular';
import { roleGuard } from './core/guards/role.guard';
import { ROLES } from './models/rol.model';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./pages/dashboard/dashboard.component').then((m) => m.DashboardComponent),
    canActivate: [MsalGuard],
  },
  {
    path: 'requests',
    loadComponent: () => import('./pages/requests/requests-list.component').then((m) => m.RequestsListComponent),
    canActivate: [MsalGuard, roleGuard([ROLES.ADMIN, ROLES.FUNCIONARIO])],
  },
  {
    path: 'requests/nuevo',
    loadComponent: () => import('./pages/requests/requests-create.component').then((m) => m.RequestsCreateComponent),
    canActivate: [MsalGuard, roleGuard([ROLES.VECINO, ROLES.FUNCIONARIO])],
  },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: 'dashboard' },
];