package campuseats.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Un intento de pago de la pasarela. referencia = número de pedido (ej. P-00032). */
public record Pago(String referencia, BigDecimal monto, String estado, LocalDateTime fechaHora) {}
