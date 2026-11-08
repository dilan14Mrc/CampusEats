package campuseats.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Una línea = un plato dentro de un pedido. Los datos del pedido se repiten en cada línea. */
public record LineaPedido(
        String numeroPedido,
        LocalDateTime registradoEn,
        String estado,
        int platoId,
        int cantidad,
        BigDecimal precioUnitario,   // precio con el que se hizo el pedido (NO el del menú)
        BigDecimal totalPedido,
        LocalDateTime pagadoEn,          // null si nunca pasó por PAGADO
        LocalDateTime enPreparacionEn,   // null si no existe
        LocalDateTime listoEn,           // null si no existe
        String nombrePlato) {            // null hasta que lo completa UnirConMenu

    public LineaPedido conNombre(String nombre) {
        return new LineaPedido(numeroPedido, registradoEn, estado, platoId, cantidad,
                precioUnitario, totalPedido, pagadoEn, enPreparacionEn, listoEn, nombre);
    }
}
