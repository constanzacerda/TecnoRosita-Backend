package tecnorosita.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tecnorosita.backend.dto.BoletaRequestDTO;
import tecnorosita.backend.dto.BoletaResponseDTO;
import tecnorosita.backend.model.Boleta;
import tecnorosita.backend.model.DetalleBoleta;
import tecnorosita.backend.model.Producto;
import tecnorosita.backend.model.Usuario;
import tecnorosita.backend.repository.BoletaRepository;
import tecnorosita.backend.repository.DetalleBoletaRepository;
import tecnorosita.backend.repository.ProductoRepository;
import tecnorosita.backend.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/boletas")
@Tag(name = "Boletas y Órdenes", description = "API REST para generar y consultar órdenes de compra")
public class BoletaController {

    private final BoletaRepository boletaRepository;
    private final DetalleBoletaRepository detalleBoletaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public BoletaController(BoletaRepository boletaRepository,
                            DetalleBoletaRepository detalleBoletaRepository,
                            ProductoRepository productoRepository,
                            UsuarioRepository usuarioRepository) {
        this.boletaRepository = boletaRepository;
        this.detalleBoletaRepository = detalleBoletaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Listar boletas", description = "ADMINISTRADOR y VENDEDOR ven todas las órdenes; CLIENTE solo ve sus propias compras.", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR', 'CLIENTE')")
    @GetMapping
    public ResponseEntity<List<BoletaResponseDTO>> listarBoletas(Authentication authentication) {
        String emailAutenticado = authentication.getName();
        boolean esStaff = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR") || a.getAuthority().equals("ROLE_VENDEDOR"));

        List<Boleta> boletas = boletaRepository.findAll();
        List<BoletaResponseDTO> respuesta = new ArrayList<>();

        for (Boleta b : boletas) {
            if (esStaff || (b.getUsuario() != null && emailAutenticado.equals(b.getUsuario().getEmail()))) {
                String nombre = b.getUsuario() != null ? b.getUsuario().getNombre() : null;
                String email = b.getUsuario() != null ? b.getUsuario().getEmail() : null;
                respuesta.add(new BoletaResponseDTO(
                        b.getId(),
                        b.getFecha(),
                        b.getTotal(),
                        nombre,
                        email
                ));
            }
        }
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Crear boleta (Checkout)", description = "Registra una compra, guarda sus detalles y descuenta el stock real en MySQL.", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENDEDOR', 'CLIENTE')")
    @PostMapping
    @Transactional
    public ResponseEntity<?> crearBoleta(@RequestBody BoletaRequestDTO request, Authentication authentication) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            return ResponseEntity.badRequest().body("La orden debe contener al menos un producto");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(authentication.getName());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario autenticado no encontrado");
        }

        int totalCalculado = 0;
        List<DetalleBoleta> detallesPreparados = new ArrayList<>();

        for (BoletaRequestDTO.DetalleItemDTO item : request.getDetalles()) {
            Optional<Producto> productoOpt = productoRepository.findById(item.getProductoId());
            if (productoOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Producto no encontrado ID: " + item.getProductoId());
            }

            Producto producto = productoOpt.get();
            int cantidad = (item.getCantidad() != null && item.getCantidad() > 0) ? item.getCantidad() : 1;
            int stockDisponible = producto.getStock() != null ? producto.getStock() : 0;

            if (stockDisponible < cantidad) {
                return ResponseEntity.badRequest().body("Stock insuficiente para: " + producto.getNombre());
            }

            producto.setStock(stockDisponible - cantidad);
            productoRepository.save(producto);

            int subtotal = producto.getPrecio() * cantidad;
            totalCalculado += subtotal;

            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setSubtotal(subtotal);
            detallesPreparados.add(detalle);
        }

        Boleta boleta = new Boleta();
        boleta.setFecha(new Date());
        boleta.setTotal(totalCalculado);
        boleta.setUsuario(usuarioOpt.get());

        Boleta boletaGuardada = boletaRepository.save(boleta);

        for (DetalleBoleta detalle : detallesPreparados) {
            detalle.setBoleta(boletaGuardada);
        }
        detalleBoletaRepository.saveAll(detallesPreparados);

        BoletaResponseDTO respuesta = new BoletaResponseDTO(
                boletaGuardada.getId(),
                boletaGuardada.getFecha(),
                boletaGuardada.getTotal(),
                boletaGuardada.getUsuario().getNombre(),
                boletaGuardada.getUsuario().getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}