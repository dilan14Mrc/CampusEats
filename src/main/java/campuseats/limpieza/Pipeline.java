package campuseats.limpieza;

import campuseats.modelo.LineaPedido;
import java.util.ArrayList;
import java.util.List;

public class Pipeline {
    private final List<Filtro> filtros;
    private final List<RegistroPaso> bitacora;

    public Pipeline(List<Filtro> filtros) {
        this.filtros = filtros;
        this.bitacora = new ArrayList<>();
    }

    public List<LineaPedido> ejecutar(List<LineaPedido> lineas) {
        List<LineaPedido> actuales = lineas;
        for (Filtro filtro : filtros) {
            int antes = actuales.size();
            actuales = filtro.apply(actuales);
            int despues = actuales.size();
            
            bitacora.add(new RegistroPaso(filtro.getClass().getSimpleName(), antes, despues));
        }
        return actuales;
    }

    public List<RegistroPaso> bitacora() {
        return bitacora;
    }
}