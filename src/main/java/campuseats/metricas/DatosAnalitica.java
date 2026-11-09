package campuseats.metricas;

import campuseats.modelo.LineaPedido;
import campuseats.modelo.Pago;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatosAnalitica {

    private final List<LineaPedido> lineas;
    private final List<Pago> pagos;

    public DatosAnalitica(List<LineaPedido> lineas, List<Pago> pagos) {
        this.lineas = List.copyOf(lineas);
        this.pagos = List.copyOf(pagos);
    }

    public List<LineaPedido> lineas() {
        return lineas;
    }

    public List<Pago> pagos() {
        return pagos;
    }

    public List<LineaPedido> unaLineaPorPedido() {
        Map<String, LineaPedido> pedidos = new LinkedHashMap<>();

        for (LineaPedido linea : lineas) {
            pedidos.putIfAbsent(linea.numeroPedido(), linea);
        }

        return List.copyOf(pedidos.values());
    }
}