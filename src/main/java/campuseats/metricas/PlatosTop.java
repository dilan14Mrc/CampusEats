package campuseats.metricas;

import campuseats.modelo.LineaPedido;
import java.util.Map;
import java.util.stream.Collectors;

public class PlatosTop implements Metrica {

    @Override
    public String nombre() {
        return "Platos más vendidos";
    }

    @Override
    public Object calcular(DatosAnalitica datos) {

        Map<String, Integer> cantidades =
            datos.lineas().stream()
                .collect(Collectors.groupingBy(
                    LineaPedido::nombrePlato,
                    Collectors.summingInt(LineaPedido::cantidad)
                ));

        return cantidades.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue()
                .reversed())
            .limit(5)
            .collect(Collectors.toList());
    }
}