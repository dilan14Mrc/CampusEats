package campuseats.fuentes;

import campuseats.modelo.LineaPedido;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON de la app: un objeto por pedido. Aquí se "aplana": por cada plato del pedido
 * se crea UNA LineaPedido (un pedido con 3 platos = 3 líneas).
 * Este adaptador solo TRADUCE; no filtra nada (la limpieza es del Pipeline).
 */
public class AppPedidosJsonAdapter extends FuenteDatos<LineaPedido> {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public AppPedidosJsonAdapter(Path ruta) { super(ruta, LineaPedido.class); }

    @Override
    protected List<LineaPedido> convertir(String texto) {
        try {
            List<LineaPedido> resultado = new ArrayList<>();
            for (JsonNode pedido : MAPPER.readTree(texto)) {
                String numero = pedido.get("numeroPedido").asText();
                LocalDateTime registrado = LocalDateTime.parse(pedido.get("registradoEn").asText());
                String estado = pedido.get("estado").asText();
                BigDecimal total = new BigDecimal(pedido.get("total").asText());

                // Fecha de cada estado por el que pasó el pedido (null si nunca pasó)
                Map<String, LocalDateTime> fechas = new HashMap<>();
                for (JsonNode h : pedido.path("historialEstados")) {
                    fechas.putIfAbsent(h.get("estado").asText(),
                            LocalDateTime.parse(h.get("fechaHora").asText()));
                }

                for (JsonNode l : pedido.path("lineas")) {
                    resultado.add(new LineaPedido(
                            numero, registrado, estado,
                            l.get("platoId").asInt(),
                            l.get("cantidad").asInt(),
                            new BigDecimal(l.get("precioUnitario").asText()),
                            total,
                            fechas.get("PAGADO"),
                            fechas.get("EN_PREPARACION"),
                            fechas.get("LISTO_RETIRO"),
                            null));                        // el nombre lo completa el filtro UnirConMenu
                }
            }
            return resultado;
        } catch (Exception e) {
            throw new IllegalStateException("JSON de pedidos inválido: " + e.getMessage(), e);
        }
    }
}
