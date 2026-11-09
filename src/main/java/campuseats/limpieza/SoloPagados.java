package campuseats.limpieza;

import campuseats.modelo.LineaPedido;
import java.util.List;
import java.util.stream.Collectors;

public class SoloPagados implements Filtro {
    @Override
    public List<LineaPedido> apply(List<LineaPedido> lineas) {
        return lineas.stream()
                .filter(l -> l.pagadoEn() != null)
                .collect(Collectors.toList());
    }
}