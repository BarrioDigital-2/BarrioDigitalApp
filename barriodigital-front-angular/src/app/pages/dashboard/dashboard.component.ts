import { Component, inject } from '@angular/core';
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
  readonly roles = this.authService.getRoles();

  readonly esAdmin = this.roles.includes(ROLES.ADMIN);
  readonly esFuncionario = this.roles.includes(ROLES.FUNCIONARIO);
  readonly esVecino = this.roles.includes(ROLES.VECINO);
  readonly esAuditor = this.roles.includes(ROLES.AUDITOR);

  logout(): void {
    this.authService.logout();
  }
}