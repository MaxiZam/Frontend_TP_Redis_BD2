package ar.unrn.tp.servicio;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime; // Importar LocalDateTime
import java.util.ArrayList;
import java.util.Comparator; // Importar Comparator
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ar.unrn.tp.dto.CrearVentaRequest;
import ar.unrn.tp.modelo.*;
import ar.unrn.tp.dto.VentaDTO; // Importar VentaDTO
import ar.unrn.tp.mapper.VentaMapper; // Importar VentaMapper
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import ar.unrn.tp.api.VentaService;

@Service
@Transactional
public class VentaServiceJPA implements VentaService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RedisTemplate<String, List<VentaDTO>> redisTemplate;

    @Autowired
    private VentaMapper ventaMapper;

    @Autowired
    private ProductoServiceJPA productoServiceJPA;

    private static final String VENTAS_CACHE_KEY_PREFIX = "ultimas_compras_cliente:";
    private static final int MAX_ULTIMAS_VENTAS = 3; // Límite de ventas a cachear

    @Override
    public void realizarVenta(CrearVentaRequest request) {
        if (request.getIdsProductos() == null || request.getIdsProductos().isEmpty()) {
            throw new ProductosNoEncontradosException();
        }

        // Validar que los IDs no sean nulos
        if (request.getIdCliente() == null) {
            throw new IllegalArgumentException("El ID del cliente no puede ser nulo");
        }
        if (request.getIdTarjeta() == null) {
            throw new IllegalArgumentException("El ID de la tarjeta no puede ser nulo");
        }

        Cliente cliente = getCliente(request.getIdCliente());
        TarjetaCredito tarjeta = getTarjeta(request.getIdTarjeta(), cliente);

        List<Producto> productosDeVenta = obtenerProductosPorIds(request.getIdsProductos());

        if (productosDeVenta.isEmpty()) {
            throw new ProductosNoEncontradosException();
        }

        Float montoTotal = calcularMonto(request.getIdsProductos(), request.getIdTarjeta());

        Venta venta = new Venta(cliente, tarjeta, productosDeVenta, montoTotal, generarNumeroVenta());
        em.persist(venta);
        em.flush();

        actualizarCacheUltimasVentas(cliente.getId());
    }

    @Override
    public List<VentaDTO> obtenerUltimasVentasCliente(Long clienteId) { // Retorna List<VentaDTO>
        String cacheKey = VENTAS_CACHE_KEY_PREFIX + clienteId;

        List<VentaDTO> ventasCacheadas = redisTemplate.opsForValue().get(cacheKey);

        if (ventasCacheadas != null && !ventasCacheadas.isEmpty()) {
            return ventasCacheadas;
        }

        List<Venta> ultimasVentasDB = obtenerUltimasVentasDesdeDB(clienteId);

        List<VentaDTO> ultimasVentasDTO = ventaMapper.toVentaDTOList(ultimasVentasDB); // Usar el nuevo método

        if (!ultimasVentasDTO.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, ultimasVentasDTO, Duration.ofMinutes(30));
        } else {
            redisTemplate.delete(cacheKey);
        }

        return ultimasVentasDTO;
    }

    @Override
    public float calcularMonto(List<Long> productosIds, Long idTarjeta) {
        if (productosIds == null || productosIds.isEmpty()) {
            return 0F;
        }

        List<Producto> productos = productoServiceJPA.obtenerProductosPorIds(productosIds);

        if (productos.isEmpty()) {
            throw new ProductosNoEncontradosException();
        }

        // Paso 1: Calcular subtotal y aplicar descuentos por producto
        float subtotalConDescuentosProducto = 0F;
        List<DescuentoProducto> descuentosProductoActivos = em.createQuery(
                        "SELECT dp FROM DescuentoProducto dp WHERE :fechaActual BETWEEN dp.fechaInicio AND dp.fechaFin",
                        DescuentoProducto.class)
                .setParameter("fechaActual", LocalDate.now())
                .getResultList();

        for (Producto producto : productos) {
            float precioProducto = producto.getPrecio();
            float precioDescontadoProducto = precioProducto;

            // Buscar el mejor descuento por producto aplicable
            for (DescuentoProducto descuento : descuentosProductoActivos) {
                if (descuento.aplicaA(producto)) {
                    // Si hay múltiples descuentos de producto, se podría aplicar el mejor
                    // Aquí asumimos que solo se aplica un descuento por producto,
                    // o el último que coincida si hay solapamiento.
                    // Para mayor precisión, se podría ordenar o aplicar el que dé el mayor descuento.
                    precioDescontadoProducto = descuento.aplicarDescuento(precioProducto);
                    break; // Aplicar solo un descuento por producto por simplicidad
                }
            }
            subtotalConDescuentosProducto += precioDescontadoProducto;
        }

        // Paso 2: Aplicar descuentos por compra (sobre el total)
        float montoFinal = subtotalConDescuentosProducto;
        if (idTarjeta != null) {
            TarjetaCredito tarjeta = em.find(TarjetaCredito.class, idTarjeta);
            if (tarjeta != null) {
                List<DescuentoCompra> descuentosCompraActivos = em.createQuery(
                                "SELECT dc FROM DescuentoCompra dc WHERE :fechaActual BETWEEN dc.fechaInicio AND dc.fechaFin AND dc.marcaTarjeta = :marcaTarjeta",
                                DescuentoCompra.class)
                        .setParameter("fechaActual", LocalDate.now())
                        .setParameter("marcaTarjeta", tarjeta.getMarca())
                        .getResultList();

                if (!descuentosCompraActivos.isEmpty()) {
                    // Aplicar el descuento de compra. Si hay varios, se puede aplicar el que dé el mayor descuento.
                    // Aquí, por simplicidad, aplicaremos el primero o se podría buscar el máximo porcentaje.
                    DescuentoCompra mejorDescuentoCompra = descuentosCompraActivos.stream()
                            .max(Comparator.comparing(DescuentoCompra::getPorcentajeDescuento))
                            .orElse(null);

                    if (mejorDescuentoCompra != null) {
                        montoFinal = mejorDescuentoCompra.aplicarDescuento(montoFinal);
                    }
                }
            }
        }
        return montoFinal;
    }

    @Override
    public List<Venta> listarVentas() {
        return em.createQuery("SELECT v FROM Venta v JOIN FETCH v.cliente JOIN FETCH v.tarjeta LEFT JOIN FETCH v.productos", Venta.class)
                .getResultList();
    }

    @Override
    public List<Venta> listarVentasCliente(Long idCliente) {
        if (idCliente == null) {
            throw new IllegalArgumentException("El ID del cliente no puede ser nulo.");
        }
        try {
            return em.createQuery("SELECT v FROM Venta v JOIN FETCH v.cliente c JOIN FETCH v.tarjeta LEFT JOIN FETCH v.productos p WHERE c.id = :idCliente", Venta.class)
                    .setParameter("idCliente", idCliente)
                    .getResultList();
        } catch (NoResultException e) {
            return List.of(); // Retorna una lista vacía si no se encuentran ventas para el cliente
        }
    }

    @Override
    public void borrarVenta(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la venta no puede ser nulo.");
        }
        Venta venta = em.find(Venta.class, id);
        if (venta == null) {
            throw new EntityNotFoundException("No se encontró la venta con ID: " + id);
        }
        em.remove(venta);
    }

    // Exceptions //

    public class ClienteNoEncontradoException extends RuntimeException {
        public ClienteNoEncontradoException(Long id) {
            super("El cliente con ID " + id + " no existe.");
        }
    }

    public class TarjetaNoValidaException extends RuntimeException {
        public TarjetaNoValidaException(Long id) {
            super("La tarjeta con ID " + id + " no existe o no pertenece al cliente.");
        }
    }

    public class ProductosNoEncontradosException extends RuntimeException {
        public ProductosNoEncontradosException() {
            super("No se encontraron productos en el carrito.");
        }
    }

    // Funciones Privadas //

    private void actualizarCacheUltimasVentas(Long clienteId) {
        String cacheKey = VENTAS_CACHE_KEY_PREFIX + clienteId;
        List<Venta> ultimasVentasDB = obtenerUltimasVentasDesdeDB(clienteId);
        List<VentaDTO> ultimasVentasDTO = ventaMapper.toVentaDTOList(ultimasVentasDB);

        if (!ultimasVentasDTO.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, ultimasVentasDTO, Duration.ofMinutes(30));
        } else {
            redisTemplate.delete(cacheKey);
        }
    }

    private Cliente getCliente(Long idCliente) {
        Cliente cliente = em.find(Cliente.class, idCliente);
        if (cliente == null) throw new ClienteNoEncontradoException(idCliente);
        return cliente;
    }

    private TarjetaCredito getTarjeta(Long idTarjeta, Cliente cliente) {
        TarjetaCredito tarjeta = em.find(TarjetaCredito.class, idTarjeta);
        // Asegurarse de que la tarjeta exista y pertenezca al cliente.
        // La comparación de objetos de TarjetaCredito requiere que su método equals() y hashCode()
        // estén correctamente implementados si no son la misma instancia referenciada.
        // Si Lombok @Data está en TarjetaCredito, esto debería manejarse automáticamente si todos los campos son relevantes,
        // pero para relaciones ManyToOne, a menudo es mejor comparar por ID.
        if (tarjeta == null || !tarjeta.getCliente().getId().equals(cliente.getId())) {
            throw new TarjetaNoValidaException(idTarjeta);
        }
        return tarjeta;
    }

    private List<Venta> obtenerUltimasVentasDesdeDB(Long clienteId) {
        return em.createQuery("SELECT v FROM Venta v WHERE v.cliente.id = :clienteId ORDER BY v.fecha DESC", Venta.class)
                .setParameter("clienteId", clienteId)
                .setMaxResults(3) // Obtener solo las últimas 3
                .getResultList();
    }

    private List<Producto> obtenerProductosPorIds(List<Long> productosIds) {
        // Mejorar esta consulta para evitar N+1 si se puede hacer con una sola consulta
        // o si ya tienes un repositorio de productos.
        if (productosIds == null || productosIds.isEmpty()) {
            return new ArrayList<>();
        }
        return em.createQuery("SELECT p FROM Producto p WHERE p.id IN :ids", Producto.class)
                .setParameter("ids", productosIds)
                .getResultList();
    }

    private String generarNumeroVenta() {
        int year = LocalDateTime.now().getYear(); // Usar LocalDateTime
        String numeroVentaPrefijo = em.createQuery(
                        "SELECT v.numeroVenta FROM Venta v WHERE FUNCTION('date_part', 'year', v.fecha) = :year ORDER BY v.fecha DESC", // Usar FUNCTION('YEAR', v.fecha) para compatibilidad con JPA/Hibernate
                        String.class)
                .setParameter("year", year)
                .setMaxResults(1)
                .getResultStream() // Usar stream para manejar Optional y evitar NoResultException
                .findFirst()
                .orElse("0-" + year); // Si no hay ventas, empieza desde 0

        String numeroSinAnio = numeroVentaPrefijo.split("-")[0];
        return (Integer.parseInt(numeroSinAnio) + 1) + "-" + year;
    }
}




