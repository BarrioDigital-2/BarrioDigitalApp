package cl.duoc.ms_barriodigital_bff.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Unico responsable de hablar HTTP con el ms-barriodigital-requests (el de tu compañero).
 * El controller no conoce URLs ni detalles de RestClient, solo llama a estos metodos.
 */
@Service
public class RequestsClient {

    private final RestClient restClient;

    public RequestsClient(RestClient requestsRestClient) {
        this.restClient = requestsRestClient;
    }

    public ResponseEntity<Object> crear(Map<String, Object> body) {
        return restClient.post()
                .uri("/api/requests")
                .body(body)
                .retrieve()
                .toEntity(Object.class);
    }

    public ResponseEntity<Object> obtenerPorId(Long id) {
        return restClient.get()
                .uri("/api/requests/{id}", id)
                .retrieve()
                .toEntity(Object.class);
    }

    public ResponseEntity<Object> cambiarEstado(Long id, Map<String, Object> body) {
        return restClient.put()
                .uri("/api/requests/{id}/status", id)
                .body(body)
                .retrieve()
                .toEntity(Object.class);
    }

    public ResponseEntity<Object> listar(String status) {
        String uri = (status != null) ? "/api/requests?status={status}" : "/api/requests";
        return restClient.get()
                .uri(uri, status)
                .retrieve()
                .toEntity(Object.class);
    }
}