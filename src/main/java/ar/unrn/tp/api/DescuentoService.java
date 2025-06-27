package ar.unrn.tp.api;

import ar.unrn.tp.modelo.DescuentoCompra;
import ar.unrn.tp.modelo.DescuentoProducto;

import java.time.LocalDate;
import java.util.List;

public interface DescuentoService {
    void crearDescuentoSobreTotal(String marcaTarjeta, LocalDate fechaDesde, LocalDate fechaHasta, Float porcentaje);
    void crearDescuentoSobreProducto(String marcaProducto, LocalDate fechaDesde, LocalDate fechaHasta, Float porcentaje);
    List<DescuentoCompra> listarDescuentosCompra();
    List<DescuentoProducto> listarDescuentosProducto();
}
