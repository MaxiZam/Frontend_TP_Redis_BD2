package ar.unrn.tp.api;

import ar.unrn.tp.dto.CrearVentaRequest;
import ar.unrn.tp.dto.VentaDTO;
import ar.unrn.tp.modelo.Venta;
import java.util.List;

public interface VentaService {
    void realizarVenta(CrearVentaRequest request);
    List<VentaDTO> obtenerUltimasVentasCliente(Long clienteId);
    float calcularMonto(List<Long> productos, Long idTarjeta);
    List<Venta> listarVentas();
    List<Venta> listarVentasCliente(Long idCliente);
    void borrarVenta(Long id);
}
