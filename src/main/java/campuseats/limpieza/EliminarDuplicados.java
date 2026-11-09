package campuseats.limpieza;

import campuseats.modelo.LineaPedido;
import java.util.*;
import java.util.stream.Collectors;

public class EliminarDuplicados implements Filtro {
    @Override
    public List<LineaPedido> apply(List<LineaPedido> lineas) {
        Set<String> vistos = new HashSet<>();
        return lineas.stream()
                .filter(l -> vistos.add(l.numeroPedido() + "-" + l.platoId()))
                .collect(Collectors.toList());
    }
}