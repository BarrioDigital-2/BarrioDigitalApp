import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { appConfig } from '../core/config/app.config';
import { CrearTramiteRequest, EstadoTramite, Tramite } from '../models/tramite.model';

/**
 * Unico responsable de hablar HTTP con el BFF para el recurso "trámites".
 * El MsalInterceptor (configurado en msal.config.ts) adjunta el Bearer token
 * automaticamente porque la URL matchea el protectedResourceMap.
 */
@Injectable({ providedIn: 'root' })
export class TramitesService {
    private readonly http = inject(HttpClient);
    private readonly baseUrl = `${appConfig.bff.baseUrl}/bff/requests`;

    crear(request: CrearTramiteRequest): Observable<Tramite> {
        return this.http.post<Tramite>(this.baseUrl, request);
    }

    listar(estado?: EstadoTramite): Observable<Tramite[]> {
        const params: Record<string, string> = {};
        if (estado) {
            params['status'] = estado;
        }
        return this.http.get<Tramite[]>(this.baseUrl, { params });
    }

    cambiarEstado(id: number, estado: EstadoTramite): Observable<Tramite> {
        return this.http.put<Tramite>(`${this.baseUrl}/${id}/status`, { status: estado });
    }
}