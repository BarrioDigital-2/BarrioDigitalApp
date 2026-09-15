package cl.duoc.ms_barriodigital_requests.controller;

import cl.duoc.ms_barriodigital_requests.dto.EstadoUpdateDTO;
import cl.duoc.ms_barriodigital_requests.dto.SolicitudDTO;
import cl.duoc.ms_barriodigital_requests.model.EstadoSolicitud;
import cl.duoc.ms_barriodigital_requests.service.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<SolicitudDTO> crearSolicitud(@Valid @RequestBody SolicitudDTO solicitudDTO) {
        SolicitudDTO response = solicitudService.crearSolicitud(solicitudDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> listarSolicitudes(@RequestParam(required = false) EstadoSolicitud status) {
        List<SolicitudDTO> response = solicitudService.listarSolicitudes(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> obtenerSolicitudPorId(@PathVariable Long id) {
        SolicitudDTO response = solicitudService.obtenerSolicitudPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SolicitudDTO> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoUpdateDTO estadoUpdateDTO) {
        SolicitudDTO response = solicitudService.actualizarEstado(id, estadoUpdateDTO);
        return ResponseEntity.ok(response);
    }
}
