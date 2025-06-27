package ar.unrn.tp.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String codigo;
    private String nombre;
    private String marca;
    private Float precio;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Version
    private Long version;

    public Producto(String codigo, String nombre, Categoria categoria, String marca, Float precio) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del producto no puede ser nulo o vacío");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del producto no puede ser nula o vacía");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría del producto no puede ser nula");
        }
        if (marca == null|| nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca del producto no puede ser nula o vacía");
        }
        if (precio == null || precio.compareTo(0F) <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser un valor positivo");
        }
        
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.marca = marca;
        this.precio = precio;
    }
}
