package cl.duoc.ms_barriodigital_bff.controller;

import cl.duoc.ms_barriodigital_bff.service.RequestsClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bff/requests")
public class BffRequestsController {

    private final RequestsClient requestsClient;

    public BffRequestsController(RequestsClient requestsClient) {
        this.requestsClient = requestsClient;
    }

    private String extraerToken(JwtAuthenticationToken auth) {
        Jwt jwt = auth.getToken();
        return jwt.getTokenValue();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VECINO', 'FUNCIONARIO')")
    public ResponseEntity<Object> crear(@RequestBody Map<String, Object> body, JwtAuthenticationToken auth) {
        return requestsClient.crear(body, extraerToken(auth));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VECINO', 'FUNCIONARIO', 'ADMIN', 'AUDITOR')")
    public ResponseEntity<Object> obtener(@PathVariable Long id, JwtAuthenticationToken auth) {
        return requestsClient.obtenerPorId(id, extraerToken(auth));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('FUNCIONARIO', 'ADMIN')")
    public ResponseEntity<Object> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, Object> body, JwtAuthenticationToken auth) {
        return requestsClient.cambiarEstado(id, body, extraerToken(auth));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('FUNCIONARIO', 'ADMIN')")
    public ResponseEntity<Object> listar(@RequestParam(required = false) String status, JwtAuthenticationToken auth) {
        return requestsClient.listar(status, extraerToken(auth));
    }
}