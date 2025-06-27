package ar.unrn.tp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescuentoProductoDTO {
    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String marcaProducto;
    private Float porcentajeDescuento;
}

