package tecnorosita.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tecnorosita.backend.dto.UsuarioResponseDTO;
import tecnorosita.backend.repository.UsuarioRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios (exclusivo ADMINISTRADOR)")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Listar usuarios", description = "Retorna todos los usuarios registrados sin exponer contraseñas.", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponseDTO(
                        u.getId(),
                        u.getNombre(),
                        u.getEmail(),
                        u.getRol()
                ))
                .toList();

        return ResponseEntity.ok(usuarios);
    }
}