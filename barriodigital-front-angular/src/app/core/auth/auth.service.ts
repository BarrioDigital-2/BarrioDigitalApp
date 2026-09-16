import { Injectable, computed, inject, signal } from '@angular/core';
import { MsalBroadcastService, MsalService } from '@azure/msal-angular';
import { AccountInfo, InteractionStatus } from '@azure/msal-browser';
import { filter } from 'rxjs';
import { appConfig } from '../config/app.config';
import { Rol } from '../../models/rol.model';

/**
 * Unico punto de contacto entre la app y MSAL.
 * Los componentes y guards consultan este servicio, nunca MsalService directamente,
 * asi si algun dia cambia la libreria de auth, solo se toca este archivo.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly msalService = inject(MsalService);
  private readonly broadcastService = inject(MsalBroadcastService);

  private readonly activeAccount = signal<AccountInfo | null>(this.msalService.instance.getActiveAccount());

  readonly isLoggedIn = computed(() => this.activeAccount() !== null);
  readonly displayName = computed(() => this.activeAccount()?.name ?? '');
  readonly roles = computed((): Rol[] => {
    const claims = this.activeAccount()?.idTokenClaims as { roles?: Rol[] } | undefined;
    return claims?.roles ?? [];
  });

  constructor() {
    this.broadcastService.inProgress$
      .pipe(filter((status) => status === InteractionStatus.None))
      .subscribe(() => {
        this.activeAccount.set(this.msalService.instance.getActiveAccount());
      });
  }

  login(): void {
    this.msalService.loginRedirect({ scopes: [appConfig.bff.scope] });
  }

  logout(): void {
    this.msalService.logoutRedirect();
  }

  /** Roles asignados al usuario, extraidos del claim "roles" del ID token. */
  getRoles(): Rol[] {
    return this.roles();
  }

  hasAnyRole(allowed: Rol[]): boolean {
    return this.roles().some((rol) => allowed.includes(rol));
  }
}