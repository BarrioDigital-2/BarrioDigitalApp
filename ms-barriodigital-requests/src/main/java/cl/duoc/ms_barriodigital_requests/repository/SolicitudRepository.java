package cl.duoc.ms_barriodigital_requests.repository;

import cl.duoc.ms_barriodigital_requests.model.EstadoSolicitud;
import cl.duoc.ms_barriodigital_requests.model.SolicitudModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudModel, Long> {
    List<SolicitudModel> findByEstado(EstadoSolicitud estado);
}
