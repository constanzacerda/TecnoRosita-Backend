package tecnorosita.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletaResponseDTO {
    private Long id;
    private Date fecha;
    private Integer total;
    private String clienteNombre;
    private String clienteEmail;
}