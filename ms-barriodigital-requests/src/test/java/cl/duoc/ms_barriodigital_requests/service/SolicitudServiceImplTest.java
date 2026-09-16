package cl.duoc.ms_barriodigital_requests.service;

import cl.duoc.ms_barriodigital_requests.dto.EstadoUpdateDTO;
import cl.duoc.ms_barriodigital_requests.dto.SolicitudDTO;
import cl.duoc.ms_barriodigital_requests.model.EstadoSolicitud;
import cl.duoc.ms_barriodigital_requests.model.SolicitudModel;
import cl.duoc.ms_barriodigital_requests.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceImplTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    private SolicitudServiceImpl solicitudService;

    @BeforeEach
    void setUp() {
        solicitudService = new SolicitudServiceImpl(solicitudRepository);
    }

    @Test
    void crearSolicitudDebeGuardarEnEstadoIngresado() {
        SolicitudDTO request = SolicitudDTO.builder()
                .tipoTramite("CERTIFICADO_RESIDENCIA")
                .vecinoEmail("vecino@duocuc.cl")
                .build();
        SolicitudModel saved = solicitud(1L, "CERTIFICADO_RESIDENCIA", "vecino@duocuc.cl", EstadoSolicitud.INGRESADO);
        when(solicitudRepository.save(any(SolicitudModel.class))).thenReturn(saved);

        SolicitudDTO result = solicitudService.crearSolicitud(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTipoTramite()).isEqualTo("CERTIFICADO_RESIDENCIA");
        assertThat(result.getEstado()).isEqualTo(EstadoSolicitud.INGRESADO);
        verify(solicitudRepository).save(any(SolicitudModel.class));
    }

    @Test
    void obtenerSolicitudPorIdDebeRetornarSolicitudExistente() {
        SolicitudModel model = solicitud(1L, "LICENCIA", "vecino@duocuc.cl", EstadoSolicitud.INGRESADO);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(model));

        SolicitudDTO result = solicitudService.obtenerSolicitudPorId(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTipoTramite()).isEqualTo("LICENCIA");
        verify(solicitudRepository).findById(1L);
    }

    @Test
    void obtenerSolicitudPorIdDebeRetornar404SiNoExiste() {
        when(solicitudRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitudService.obtenerSolicitudPorId(99L))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(exception -> ((ResponseStatusException) exception).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void listarSolicitudesDebeFiltrarPorEstadoCuandoSeIndica() {
        SolicitudModel model = solicitud(1L, "LICENCIA", "vecino@duocuc.cl", EstadoSolicitud.ADMITIDO);
        when(solicitudRepository.findByEstado(EstadoSolicitud.ADMITIDO)).thenReturn(List.of(model));

        List<SolicitudDTO> result = solicitudService.listarSolicitudes(EstadoSolicitud.ADMITIDO);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEstado()).isEqualTo(EstadoSolicitud.ADMITIDO);
        verify(solicitudRepository).findByEstado(EstadoSolicitud.ADMITIDO);
    }

    @Test
    void listarSolicitudesDebeRetornarTodasCuandoNoSeIndicaEstado() {
        SolicitudModel model = solicitud(1L, "LICENCIA", "vecino@duocuc.cl", EstadoSolicitud.INGRESADO);
        when(solicitudRepository.findAll()).thenReturn(List.of(model));

        List<SolicitudDTO> result = solicitudService.listarSolicitudes(null);

        assertThat(result).hasSize(1);
        verify(solicitudRepository).findAll();
    }

    @Test
    void actualizarEstadoDebeCambiarEstadoYGuardar() {
        SolicitudModel model = solicitud(1L, "LICENCIA", "vecino@duocuc.cl", EstadoSolicitud.INGRESADO);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(model));
        when(solicitudRepository.save(model)).thenReturn(model);

        SolicitudDTO result = solicitudService.actualizarEstado(
                1L, new EstadoUpdateDTO(EstadoSolicitud.ADMITIDO));

        assertThat(result.getEstado()).isEqualTo(EstadoSolicitud.ADMITIDO);
        assertThat(model.getEstado()).isEqualTo(EstadoSolicitud.ADMITIDO);
        verify(solicitudRepository).save(model);
    }

    @Test
    void actualizarEstadoNoDebePermitirEnTerrenoSiNoEstaAdmitida() {
        SolicitudModel model = solicitud(1L, "LICENCIA", "vecino@duocuc.cl", EstadoSolicitud.INGRESADO);
        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(model));

        assertThatThrownBy(() -> solicitudService.actualizarEstado(
                1L, new EstadoUpdateDTO(EstadoSolicitud.EN_TERRENO)))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(exception -> ((ResponseStatusException) exception).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private SolicitudModel solicitud(Long id, String tipoTramite, String vecinoEmail, EstadoSolicitud estado) {
        return SolicitudModel.builder()
                .id(id)
                .tipoTramite(tipoTramite)
                .vecinoEmail(vecinoEmail)
                .estado(estado)
                .build();
    }
}
