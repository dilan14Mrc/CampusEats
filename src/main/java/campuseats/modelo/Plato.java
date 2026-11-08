package campuseats.modelo;

import java.math.BigDecimal;

public record Plato(int id, String nombre, String categoria, BigDecimal precioActual, String estado) {}
