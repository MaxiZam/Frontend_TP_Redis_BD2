package ar.unrn.tp.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "tarjeta_id", nullable = false)
    private TarjetaCredito tarjeta;

    @ManyToMany
    @JoinTable(
            name = "venta_producto",
            joinColumns = @JoinColumn(name = "venta_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> productos = new ArrayList<>();

    private Float montoTotal;

    @Column(unique = true, nullable = false)
    private String numeroVenta;

    public Venta(Cliente cliente, TarjetaCredito tarjeta, List<Producto> productos, Float montoTotal, String numeroVenta) {
        this.fecha = LocalDateTime.now();
        this.cliente = cliente;
        this.tarjeta = tarjeta;
        this.productos = productos != null ? productos : new ArrayList<>();
        this.montoTotal = montoTotal;
        this.numeroVenta = numeroVenta;
    }
}

