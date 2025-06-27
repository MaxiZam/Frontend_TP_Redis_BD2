package ar.unrn.tp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescuentoCompraDTO {
    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String marcaTarjeta;
    private Float porcentajeDescuento;
}

