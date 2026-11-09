package campuseats.metricas;

import campuseats.modelo.LineaPedido;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TicketPromedio implements Metrica {

    @Override
    public String nombre() {
        return "Ticket promedio";
    }

    @Override
    public Object calcular(DatosAnalitica datos) {

        var pedidos = datos.unaLineaPorPedido();

        if (pedidos.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal suma = BigDecimal.ZERO;

        for (LineaPedido linea : pedidos) {
            suma = suma.add(linea.totalPedido());
        }

        return suma.divide(
            BigDecimal.valueOf(pedidos.size()),
            2,
            RoundingMode.HALF_UP
        );
    }
}