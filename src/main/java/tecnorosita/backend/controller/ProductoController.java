package tecnorosita.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tecnorosita.backend.model.Producto;
import tecnorosita.backend.repository.ProductoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Catálogo de Productos", description = "API REST para la gestión de inventario")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }


    @Operation(summary = "Listar todos los productos", description = "Obtiene la lista completa del catálogo para la vitrina del frontend.")
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoRepository.findAll());
    }

    @Operation(summary = "Obtener un producto por ID", description = "Busca el detalle de un producto específico mediante su identificador.")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Crear un producto", description = "Agrega un nuevo producto a la base de datos.", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoRepository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }


    @Operation(summary = "Actualizar un producto", description = "Modifica los datos de un producto existente.", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        return productoRepository.findById(id).map(producto -> {
            producto.setNombre(productoActualizado.getNombre());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setImagenUrl(productoActualizado.getImagenUrl());
            producto.setCategoria(productoActualizado.getCategoria());
            return ResponseEntity.ok(productoRepository.save(producto));
        }).orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Eliminar un producto", description = "Borra físicamente un producto del sistema.", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}