package campuseats.metricas;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PedidosPorHora implements Metrica {

    @Override
    public String nombre() {
        return "Pedidos por hora";
    }

    @Override
    public Object calcular(DatosAnalitica datos) {

        Map<String, Long> conteo = datos.unaLineaPorPedido()
            .stream()
            .filter(linea -> linea.registradoEn() != null)
            .collect(Collectors.groupingBy(
                linea -> linea.registradoEn().format(
                    DateTimeFormatter.ofPattern("HH:00")
                ),
                Collectors.counting()
            ));

        return conteo.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue()
                .reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> a,
                LinkedHashMap::new
            ));
    }
}
