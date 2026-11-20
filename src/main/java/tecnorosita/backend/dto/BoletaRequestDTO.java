package tecnorosita.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletaRequestDTO {
    private String nombreCliente;
    private String emailCliente;
    private String direccion;
    private String metodoPago;
    private Integer total;
    private List<DetalleItemDTO> detalles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleItemDTO {
        private Long productoId;
        private Integer cantidad;
        private Integer precioUnitario;
    }
}
