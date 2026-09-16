package cl.duoc.ms_barriodigital_requests.dto;

import cl.duoc.ms_barriodigital_requests.model.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoUpdateDTO {

    @NotNull(message = "El estado es obligatorio")
    private EstadoSolicitud status;
}
