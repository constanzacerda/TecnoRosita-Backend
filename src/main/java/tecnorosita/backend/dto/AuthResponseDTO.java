package tecnorosita.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private Long id;
    private String nombre;
    private String email;
    private String rol;
}