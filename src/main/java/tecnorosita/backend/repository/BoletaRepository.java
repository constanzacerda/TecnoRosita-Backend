package tecnorosita.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tecnorosita.backend.model.Boleta;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
}
