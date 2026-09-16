package cl.duoc.ms_barriodigital_bff.service;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class RequestsClient {

    private final RestClient restClient;

    public RequestsClient(RestClient requestsRestClient) {
        this.restClient = requestsRestClient;
    }

    public ResponseEntity<Object> crear(Map<String, Object> body, String bearerToken) {
        ResponseEntity<Object> response = restClient.post()
                .uri("/api/requests")
                .header("Authorization", "Bearer " + bearerToken)
                .body(body)
                .retrieve()
                .toEntity(Object.class);
        return limpiar(response);
    }

    public ResponseEntity<Object> obtenerPorId(Long id, String bearerToken) {
        ResponseEntity<Object> response = restClient.get()
                .uri("/api/requests/{id}", id)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()
                .toEntity(Object.class);
        return limpiar(response);
    }

    public ResponseEntity<Object> cambiarEstado(Long id, Map<String, Object> body, String bearerToken) {
        ResponseEntity<Object> response = restClient.put()
                .uri("/api/requests/{id}/status", id)
                .header("Authorization", "Bearer " + bearerToken)
                .body(body)
                .retrieve()
                .toEntity(Object.class);
        return limpiar(response);
    }

    public ResponseEntity<Object> listar(String status, String bearerToken) {
        String uri = (status != null) ? "/api/requests?status={status}" : "/api/requests";
        ResponseEntity<Object> response = restClient.get()
                .uri(uri, status)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()
                .toEntity(Object.class);
        return limpiar(response);
    }

    /**
     * Reenvia solo status + body, sin los headers originales de la respuesta.
     * Evita que headers "hop-by-hop" (Transfer-Encoding, Connection, etc.) del
     * microservicio de dominio choquen con los que agrega Tomcat al construir
     * la respuesta hacia el frontend (causaba 502 en nginx por header duplicado).
     */
    private ResponseEntity<Object> limpiar(ResponseEntity<Object> response) {
        HttpStatusCode status = response.getStatusCode();
        return ResponseEntity.status(status).body(response.getBody());
    }
}