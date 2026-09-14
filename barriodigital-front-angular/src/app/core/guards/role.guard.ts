import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { Rol } from '../../models/rol.model';

/**
 * Fabrica de guards: roleGuard(['Admin']) protege una ruta solo para ese rol.
 * Se usa junto a MsalGuard en las rutas (MsalGuard exige sesion, este exige rol).
 */
export function roleGuard(allowedRoles: Rol[]): CanActivateFn {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (authService.hasAnyRole(allowedRoles)) {
      return true;
    }

    router.navigate(['/dashboard']);
    return false;
  };
}