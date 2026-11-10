package tecnorosita.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DETALLE_BOLETA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Integer subtotal;

    @ManyToOne
    @JoinColumn(name = "boleta_id", nullable = false)
    private Boleta boleta;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
}