package tecnorosita.backend.service;

import org.springframework.stereotype.Service;
import tecnorosita.backend.model.Categoria;
import tecnorosita.backend.model.Producto;
import tecnorosita.backend.repository.CategoriaRepository;
import tecnorosita.backend.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardar(Producto producto) {
        if (producto.getStock() == null) {
            producto.setStock(15);
        }
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria cat = categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            producto.setCategoria(cat);
        }
        return productoRepository.save(producto);
    }

    public Optional<Producto> actualizar(Long id, Producto productoActualizado) {
        return productoRepository.findById(id).map(producto -> {
            producto.setNombre(productoActualizado.getNombre());
            producto.setPrecio(productoActualizado.getPrecio());
            if (productoActualizado.getStock() != null) {
                producto.setStock(productoActualizado.getStock());
            }
            producto.setImagenUrl(productoActualizado.getImagenUrl());
            if (productoActualizado.getCategoria() != null && productoActualizado.getCategoria().getId() != null) {
                Categoria cat = categoriaRepository.findById(productoActualizado.getCategoria().getId())
                        .orElse(producto.getCategoria());
                producto.setCategoria(cat);
            }
            return productoRepository.save(producto);
        });
    }

    public boolean eliminar(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}