package ar.unrn.tp.modelo;

import java.time.LocalDate;

public interface Descuento {
    boolean esValido(LocalDate fecha);
    Float aplicarDescuento(Float monto);
}