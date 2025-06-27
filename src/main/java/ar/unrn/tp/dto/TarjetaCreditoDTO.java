package ar.unrn.tp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarjetaCreditoDTO {
    private Long id;
    private String numero;
    private String marca;
    private LocalDate fechaVencimiento;
    private Long clienteId;
}

