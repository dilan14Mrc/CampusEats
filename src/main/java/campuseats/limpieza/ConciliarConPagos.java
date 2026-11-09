package campuseats.limpieza;

import campuseats.modelo.LineaPedido;
import campuseats.modelo.Pago;
import java.util.List;
import java.util.stream.Collectors;

public class ConciliarConPagos implements Filtro {
    private final List<Pago> pagos;

    public ConciliarConPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    @Override
    public List<LineaPedido> apply(List<LineaPedido> lineas) {
        return lineas.stream().filter(linea -> {
            // Buscamos un pago aprobado 
            return pagos.stream().anyMatch(pago -> 
                pago.referencia().equals(linea.numeroPedido()) &&
                pago.estado().equals("APROBADO") && 
                pago.monto().compareTo(linea.totalPedido()) == 0 // compareTo porque BigDecimal usa escala
            );
        }).collect(Collectors.toList());
    }
}