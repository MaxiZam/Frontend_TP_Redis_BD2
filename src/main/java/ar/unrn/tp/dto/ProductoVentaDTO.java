package ar.unrn.tp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoVentaDTO {
    private Long id;
    private String nombre;
    private String codigo;
    private String marca;
    private Float precio;
    private String nombreCategoria;
}