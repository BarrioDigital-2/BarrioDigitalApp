package cl.duoc.ms_barriodigital_requests.service;

import cl.duoc.ms_barriodigital_requests.dto.EstadoUpdateDTO;
import cl.duoc.ms_barriodigital_requests.dto.SolicitudDTO;
import cl.duoc.ms_barriodigital_requests.model.EstadoSolicitud;

import java.util.List;

public interface SolicitudService {
    SolicitudDTO crearSolicitud(SolicitudDTO solicitudDTO);
    SolicitudDTO obtenerSolicitudPorId(Long id);
    List<SolicitudDTO> listarSolicitudes(EstadoSolicitud estado);
    SolicitudDTO actualizarEstado(Long id, EstadoUpdateDTO estadoUpdateDTO);
}
