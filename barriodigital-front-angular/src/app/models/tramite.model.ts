export type EstadoTramite = 'INGRESADO' | 'ADMITIDO' | 'EN_GESTION' | 'EN_TERRENO' | 'RESUELTO' | 'RECHAZADO';

export interface Tramite {
  id: number;
  tipoTramite: string;
  vecinoEmail: string;
  estado: EstadoTramite;
  fechaCreacion: string;
  fechaActualizacion: string;
}

export interface CrearTramiteRequest {
  tipoTramite: string;
  vecinoEmail: string;
}