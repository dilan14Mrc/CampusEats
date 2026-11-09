package campuseats.limpieza;

import campuseats.modelo.LineaPedido;
import java.util.List;
import java.util.function.UnaryOperator;

public interface Filtro extends UnaryOperator<List<LineaPedido>> {
    
}