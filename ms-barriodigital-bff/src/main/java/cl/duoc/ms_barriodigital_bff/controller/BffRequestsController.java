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
    @PreAuthorize("hasAnyRole('Vecino', 'Funcionario')")
    public ResponseEntity<Object> crear(@RequestBody Map<String, Object> body) {
        return requestsClient.crear(body);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('Vecino', 'Funcionario', 'Admin', 'Auditor')")
    public ResponseEntity<Object> obtener(@PathVariable Long id) {
        return requestsClient.obtenerPorId(id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('Funcionario', 'Admin')")
    public ResponseEntity<Object> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return requestsClient.cambiarEstado(id, body);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Funcionario', 'Admin')")
    public ResponseEntity<Object> listar(@RequestParam(required = false) String status) {
        return requestsClient.listar(status);
    }
}