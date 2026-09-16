import { Component, computed, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { ROLES } from '../../models/rol.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {
  private readonly authService = inject(AuthService);

  readonly displayName = this.authService.displayName;
  readonly roles = this.authService.roles;

  readonly esAdmin = computed(() => this.roles().includes(ROLES.ADMIN));
  readonly esFuncionario = computed(() => this.roles().includes(ROLES.FUNCIONARIO));
  readonly esVecino = computed(() => this.roles().includes(ROLES.VECINO));
  readonly esAuditor = computed(() => this.roles().includes(ROLES.AUDITOR));

  logout(): void {
    this.authService.logout();
  }
}