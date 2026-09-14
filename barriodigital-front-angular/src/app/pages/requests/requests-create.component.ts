import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TramitesService } from '../../services/tramites.service';

@Component({
  selector: 'app-requests-create',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './requests-create.component.html',
  styleUrl: './requests-create.component.css',
})
export class RequestsCreateComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly tramitesService = inject(TramitesService);
  private readonly router = inject(Router);

  readonly enviando = signal(false);
  readonly error = signal<string | null>(null);

  readonly form = this.formBuilder.nonNullable.group({
    tipoTramite: ['', Validators.required],
    vecinoEmail: ['', [Validators.required, Validators.email]],
  });

  enviar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.enviando.set(true);
    this.error.set(null);

    this.tramitesService.crear(this.form.getRawValue()).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => {
        this.error.set('No se pudo ingresar el trámite. Intenta nuevamente.');
        this.enviando.set(false);
      },
    });
  }
}