package ar.unrn.tp.web;

import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.dto.CrearVentaRequest;
import ar.unrn.tp.dto.VentaDTO;
import ar.unrn.tp.mapper.VentaMapper;
import ar.unrn.tp.servicio.VentaServiceJPA;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ventas")
@AllArgsConstructor
public class VentaController {

    private VentaService ventaService;
    private VentaMapper ventaMapper;

    @PostMapping
    public ResponseEntity<?> realizarVenta(@RequestBody CrearVentaRequest request) {
        if (isInvalidCrearVentaRequest(request)) {
            return ResponseEntity.badRequest().body("Datos de venta incompletos o inválidos.");
        }

        try {
            ventaService.realizarVenta(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (VentaServiceJPA.ClienteNoEncontradoException | VentaServiceJPA.TarjetaNoValidaException | VentaServiceJPA.ProductosNoEncontradosException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PutMapping("/calcular-monto")
    public ResponseEntity<?> calcularMonto(@RequestBody List<Long> productosId, @RequestParam Long tarjetaId) {
        try {
            float monto = ventaService.calcularMonto(productosId, tarjetaId);
            return ResponseEntity.ok(monto);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> listarVentas() {
        try {
            List<VentaDTO> ventas = ventaService.listarVentas().stream()
                    .map(ventaMapper::toVentaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarVentasCliente(@PathVariable Long id){
        try {
            List<VentaDTO> ventas = ventaService.listarVentasCliente(id).stream()
                    .map(ventaMapper::toVentaDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarVenta(@PathVariable Long id) {
        try {
            ventaService.borrarVenta(id);
            return ResponseEntity.ok("Venta eliminada exitosamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: La venta con ID " + id + " no existe.");
        } catch (Exception e) {
            return handleException(e, "Error al eliminar la venta.");
        }
    }

    @GetMapping("/ultimas/{clienteId}")
    public ResponseEntity<?> ultimasVentas(@PathVariable Long clienteId) {
        if (clienteId == null || clienteId <= 0) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        try {
            List<VentaDTO> ultimasVentasDTO = ventaService.obtenerUltimasVentasCliente(clienteId);
            return ResponseEntity.ok(ultimasVentasDTO);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private boolean isInvalidCrearVentaRequest(CrearVentaRequest request) {
        return request == null || request.getIdsProductos() == null || request.getIdsProductos().isEmpty() ||
                request.getIdCliente() == null || request.getIdTarjeta() == null;
    }

    private ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error interno: " + e.getMessage()); // 500 Internal Server Error
    }

    private ResponseEntity<String> handleException(Exception e, String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message + " Detalle: " + e.getMessage());
    }
}

