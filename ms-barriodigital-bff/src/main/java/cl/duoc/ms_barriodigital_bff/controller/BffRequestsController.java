package cl.duoc.ms_barriodigital_bff.controller;

import cl.duoc.ms_barriodigital_bff.service.RequestsClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bff/requests")
public class BffRequestsController {

    private final RequestsClient requestsClient;

    public BffRequestsController(RequestsClient requestsClient) {
        this.requestsClient = requestsClient;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VECINO', 'FUNCIONARIO')")
    public ResponseEntity<Object> crear(@RequestBody Map<String, Object> body) {
        return requestsClient.crear(body);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VECINO', 'FUNCIONARIO', 'ADMIN', 'AUDITOR')")
    public ResponseEntity<Object> obtener(@PathVariable Long id) {
        return requestsClient.obtenerPorId(id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('FUNCIONARIO', 'ADMIN')")
    public ResponseEntity<Object> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return requestsClient.cambiarEstado(id, body);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('FUNCIONARIO', 'ADMIN')")
    public ResponseEntity<Object> listar(@RequestParam(required = false) String status) {
        return requestsClient.listar(status);
    }
}