package campuseats.limpieza;

import campuseats.modelo.LineaPedido;
import java.util.List;
import java.util.stream.Collectors;

public class ValidarCantidades implements Filtro {
    @Override
    public List<LineaPedido> apply(List<LineaPedido> lineas) {
        return lineas.stream()
                .filter(l -> l.cantidad() > 0)
                .collect(Collectors.toList());
    }
}