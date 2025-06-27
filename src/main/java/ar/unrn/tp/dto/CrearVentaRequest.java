package ar.unrn.tp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearVentaRequest {
    private Long idCliente;
    private List<Long> idsProductos;
    private Long idTarjeta;
}
