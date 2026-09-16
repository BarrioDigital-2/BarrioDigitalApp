package cl.duoc.ms_barriodigital_requests.service;

import cl.duoc.ms_barriodigital_requests.dto.EstadoUpdateDTO;
import cl.duoc.ms_barriodigital_requests.dto.SolicitudDTO;
import cl.duoc.ms_barriodigital_requests.model.EstadoSolicitud;
import cl.duoc.ms_barriodigital_requests.model.SolicitudModel;
import cl.duoc.ms_barriodigital_requests.repository.SolicitudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;

    public SolicitudServiceImpl(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Override
    public SolicitudDTO crearSolicitud(SolicitudDTO solicitudDTO) {
        SolicitudModel model = SolicitudModel.builder()
                .tipoTramite(solicitudDTO.getTipoTramite())
                .vecinoEmail(solicitudDTO.getVecinoEmail())
                .estado(EstadoSolicitud.INGRESADO) // Estado inicial por defecto
                .build();

        SolicitudModel saved = solicitudRepository.save(model);
        return mapToDTO(saved);
    }

    @Override
    public SolicitudDTO obtenerSolicitudPorId(Long id) {
        SolicitudModel model = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La solicitud no existe"));
        return mapToDTO(model);
    }

    @Override
    public List<SolicitudDTO> listarSolicitudes(EstadoSolicitud estado) {
        List<SolicitudModel> solicitudes;
        if (estado != null) {
            solicitudes = solicitudRepository.findByEstado(estado);
        } else {
            solicitudes = solicitudRepository.findAll();
        }
        return solicitudes.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public SolicitudDTO actualizarEstado(Long id, EstadoUpdateDTO estadoUpdateDTO) {
        SolicitudModel model = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La solicitud no existe"));

        // Regla de negocio: no se puede pasar a EN_TERRENO sin antes pasar por ADMITIDO
        if (estadoUpdateDTO.getStatus() == EstadoSolicitud.EN_TERRENO &&
            model.getEstado() != EstadoSolicitud.ADMITIDO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede pasar a EN_TERRENO sin antes estar ADMITIDO");
        }

        model.setEstado(estadoUpdateDTO.getStatus());
        SolicitudModel updated = solicitudRepository.save(model);
        return mapToDTO(updated);
    }

    private SolicitudDTO mapToDTO(SolicitudModel model) {
        return SolicitudDTO.builder()
                .id(model.getId())
                .tipoTramite(model.getTipoTramite())
                .vecinoEmail(model.getVecinoEmail())
                .estado(model.getEstado())
                .fechaCreacion(model.getFechaCreacion())
                .fechaActualizacion(model.getFechaActualizacion())
                .build();
    }
}
