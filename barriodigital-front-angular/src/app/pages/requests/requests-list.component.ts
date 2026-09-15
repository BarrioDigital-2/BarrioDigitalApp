import { Component, OnInit, inject, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { TramitesService } from '../../services/tramites.service';
import { EstadoTramite, Tramite } from '../../models/tramite.model';

@Component({
  selector: 'app-requests-list',
  standalone: true,
  imports: [NgClass],
  templateUrl: './requests-list.component.html',
  styleUrl: './requests-list.component.css',
})
export class RequestsListComponent implements OnInit {
  private readonly tramitesService = inject(TramitesService);

  readonly tramites = signal<Tramite[]>([]);
  readonly cargando = signal(true);
  readonly error = signal<string | null>(null);

  ngOnInit(): void {
    this.cargarTramites();
  }

  cargarTramites(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.tramitesService.listar().subscribe({
      next: (tramites) => {
        this.tramites.set(tramites);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar los trámites. Verifica que el BFF esté corriendo.');
        this.cargando.set(false);
      },
    });
  }

  admitir(tramite: Tramite): void {
    this.cambiarEstado(tramite, 'ADMITIDO');
  }

  resolver(tramite: Tramite): void {
    this.cambiarEstado(tramite, 'RESUELTO');
  }

  private cambiarEstado(tramite: Tramite, nuevoEstado: EstadoTramite): void {
    this.tramitesService.cambiarEstado(tramite.id, nuevoEstado).subscribe({
      next: () => this.cargarTramites(),
      error: () => this.error.set('No se pudo cambiar el estado del trámite.'),
    });
  }

  estadoClass(estado: EstadoTramite): string {
    const clases: Record<EstadoTramite, string> = {
      INGRESADO: 'badge-info',
      ADMITIDO: 'badge-info',
      EN_GESTION: 'badge-warning',
      EN_TERRENO: 'badge-warning',
      RESUELTO: 'badge-success',
      RECHAZADO: 'badge-danger',
    };
    return clases[estado];
  }
}