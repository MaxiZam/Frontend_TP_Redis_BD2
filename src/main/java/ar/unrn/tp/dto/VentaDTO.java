package ar.unrn.tp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaDTO {
    private Long id;
    private LocalDateTime fecha;
    private String numeroVenta;
    private Float montoTotal;
    private String nombreCliente;
    private String apellidoCliente;
    private List<ProductoVentaDTO> productos;
}