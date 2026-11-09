package campuseats.limpieza;

import campuseats.modelo.LineaPedido;
import campuseats.modelo.Plato;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UnirConMenu implements Filtro {
    private final Map<Integer, String> nombresPorId;

    public UnirConMenu(List<Plato> platos) {
        this.nombresPorId = platos.stream()
                .collect(Collectors.toMap(Plato::id, Plato::nombre));
    }

    @Override
    public List<LineaPedido> apply(List<LineaPedido> lineas) {
        return lineas.stream()
                .map(l -> {
                    String nombre = nombresPorId.get(l.platoId());
                    // Si no encuentra el nombre (plato retirado), deja el que tenía o pone "Desconocido"
                    return l.conNombre(nombre != null ? nombre : "Desconocido");
                })
                .collect(Collectors.toList());
    }
}
