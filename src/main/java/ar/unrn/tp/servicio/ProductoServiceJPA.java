package ar.unrn.tp.servicio;

import java.util.ArrayList;
import java.util.List;
import ar.unrn.tp.exception.OptimisticLockException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Producto;

@Service
@Transactional
public class ProductoServiceJPA implements ProductoService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void crearProducto(String codigo, String descripcion, String marca, float precio, Long idCategoria) {
        if (codigoYaExistente(codigo)) {
            throw new IllegalArgumentException("Ya existe un producto con ese código");
        }

        Categoria categoria = em.find(Categoria.class, idCategoria);
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría especificada no existe");
        }

        Producto producto = new Producto(codigo, descripcion, categoria, marca, precio);
        em.persist(producto);
    }

    @Override
    public void modificarProducto(Long id, String nombre, float precio, String marca, Long categoriaId, Long version) {
        Producto producto = em.find(Producto.class, id);

        if (producto == null) {
            throw new IllegalArgumentException("El producto no existe");
        }

        if (!producto.getVersion().equals(version)) {
            throw new OptimisticLockException("El producto ha sido modificado por otro usuario");
        }

        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setMarca(marca);

        Categoria categoria = em.find(Categoria.class, categoriaId);

        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no existe");
        }

        producto.setCategoria(categoria);
        em.flush();
    }

    @Override
    public List<Producto> listarProductos() {
        return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        Producto producto = em.find(Producto.class, id);
        if (producto == null) {
            throw new IllegalArgumentException("El producto con ID " + id + " no existe");
        }
        return producto;
    }

    @Override
    public Producto obtenerProductoPorCodigo(String codigo) {
        try {
            return em.createQuery("SELECT p FROM Producto p WHERE p.codigo = :codigo", Producto.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new IllegalArgumentException("El producto no existe");
        } catch (PersistenceException e) {
            throw new RuntimeException("Error al obtener el producto: " + e.getMessage(), e);
        }
    }


    @Override
    public void borrarProducto(Long id) {
        try {
            Producto producto = em.find(Producto.class, id);
            if (producto == null) {
                throw new IllegalArgumentException("El producto no existe");
            }
            em.remove(producto);
        } catch (PersistenceException e) {
            throw new RuntimeException("Error al eliminar el producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void crearCategoria(String nombre) {
        if(nombre.isEmpty()){
            throw new IllegalArgumentException("No se ingreso ningun nombre para la categoria");
        }
        Categoria nuevaCategoria = new Categoria(nombre);
        em.persist(nuevaCategoria);
    }

    private boolean codigoYaExistente(String codigo) {
        return !em.createQuery("SELECT p FROM Producto p WHERE p.codigo = :codigo", Producto.class)
                .setParameter("codigo", codigo)
                .getResultList()
                .isEmpty();
    }

    public List<Producto> obtenerProductosPorIds(List<Long> productosIds) {
        if (productosIds == null || productosIds.isEmpty()) {
            return new ArrayList<>(); // retorna lista vacia si no hay IDs
        }

        return em.createQuery("SELECT p FROM Producto p WHERE p.id IN :ids", Producto.class)
                .setParameter("ids", productosIds)
                .getResultList();
    }

    public List<Categoria> obtenerCategorias(){
        return em.createQuery("SELECT p FROM Categoria p", Categoria.class)
                .getResultList();
    }
}


