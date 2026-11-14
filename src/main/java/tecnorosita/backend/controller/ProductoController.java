package tecnorosita.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tecnorosita.backend.model.Producto;
import tecnorosita.backend.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Catálogo de Productos", description = "API REST para la gestión de inventario")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Listar todos los productos", description = "Obtiene la lista completa del catálogo para la vitrina del frontend.")
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @Operation(summary = "Obtener un producto por ID", description = "Busca el detalle de un producto específico mediante su identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un producto", description = "Agrega un nuevo producto a la base de datos.", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @Operation(summary = "Actualizar un producto", description = "Modifica los datos de un producto existente.", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        return productoService.actualizar(id, productoActualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un producto", description = "Borra físicamente un producto del sistema.", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        return productoService.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

