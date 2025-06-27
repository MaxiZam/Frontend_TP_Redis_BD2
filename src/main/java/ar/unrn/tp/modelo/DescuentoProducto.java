package ar.unrn.tp.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class DescuentoProducto implements Descuento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String marcaProducto;
    private Float porcentajeDescuento;

    public DescuentoProducto(LocalDate fechaInicio, LocalDate fechaFin, String marcaProducto, Float porcentajeDescuento) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.marcaProducto = marcaProducto;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public boolean esValido(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    @Override
    public Float aplicarDescuento(Float monto) {
        return monto - (monto*(porcentajeDescuento/100));
    }

    public boolean aplicaA(Producto producto){
        return (this.marcaProducto.equals(producto.getMarca()));
    }

}
