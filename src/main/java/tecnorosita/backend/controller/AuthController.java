package tecnorosita.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tecnorosita.backend.dto.AuthResponseDTO;
import tecnorosita.backend.dto.LoginRequestDTO;
import tecnorosita.backend.dto.RegistroRequestDTO;
import tecnorosita.backend.model.Usuario;
import tecnorosita.backend.repository.UsuarioRepository;
import tecnorosita.backend.security.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints para Login, Registro y generación de JWT")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Iniciar Sesión", description = "Valida credenciales y devuelve un token JWT con los datos del usuario.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        String jwt = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol());

        return ResponseEntity.ok(new AuthResponseDTO(
                jwt,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        ));
    }

    @Operation(summary = "Registrar Cliente", description = "Crea una nueva cuenta de usuario con rol CLIENTE y retorna su token JWT.")
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequestDTO request) {
        if (request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()
                || request.getNombre() == null || request.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body("Todos los campos son obligatorios");
        }

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }

        Usuario nuevoUsuario = new Usuario(
                null,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNombre(),
                "CLIENTE"
        );

        Usuario guardado = usuarioRepository.save(nuevoUsuario);
        String jwt = jwtUtil.generateToken(guardado.getEmail(), guardado.getRol());

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDTO(
                jwt,
                guardado.getId(),
                guardado.getNombre(),
                guardado.getEmail(),
                guardado.getRol()
        ));
    }
}