package cl.duoc.ms_barriodigital_requests.dto;

import cl.duoc.ms_barriodigital_requests.model.EstadoSolicitud;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudDTO {

    private Long id;

    @NotBlank(message = "El tipo de trámite es obligatorio")
    private String tipoTramite;

    @NotBlank(message = "El email del vecino es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String vecinoEmail;

    private EstadoSolicitud estado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;
}
