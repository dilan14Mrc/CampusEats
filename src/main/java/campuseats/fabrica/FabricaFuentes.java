package campuseats.fabrica;

import campuseats.fuentes.AppPedidosJsonAdapter;
import campuseats.fuentes.FuenteDatos;
import campuseats.fuentes.MenuCsvAdapter;
import campuseats.fuentes.PasarelaCsvAdapter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * PATRÓN FACTORY: un solo lugar decide qué adaptador crear según el "tipo" de fuentes.json.
 * No hay if/else: un Map asocia cada tipo con el constructor de su adaptador.
 * Para agregar una fuente nueva basta con registrar(...); el resto del programa no cambia.
 */
public class FabricaFuentes {
    private final Map<String, Function<Path, FuenteDatos<?>>> tipos = new HashMap<>();

    public FabricaFuentes() {
        // Las claves deben ser IGUALES al campo "tipo" de config/fuentes.json
        registrar("app_json", AppPedidosJsonAdapter::new);
        registrar("pasarela_csv", PasarelaCsvAdapter::new);
        registrar("menu_csv", MenuCsvAdapter::new);
    }

    public void registrar(String tipo, Function<Path, FuenteDatos<?>> constructor) {
        tipos.put(tipo, constructor);
    }

    public FuenteDatos<?> crear(ConfigFuente cfg, Path base) {
        Function<Path, FuenteDatos<?>> constructor = tipos.get(cfg.tipo());
        if (constructor == null) {
            throw new IllegalArgumentException("Tipo de fuente desconocido: " + cfg.tipo());
        }
        return constructor.apply(base.resolve(cfg.ruta()));
    }

    /**
     * Ayuda para ServicioAnalitica: crea todas las fuentes del config y devuelve
     * los datos de las que entregan objetos de la clase pedida (LineaPedido, Pago o Plato).
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> leerTodas(List<ConfigFuente> configs, Path base, Class<T> tipo) {
        List<T> resultado = new ArrayList<>();
        for (ConfigFuente cfg : configs) {
            FuenteDatos<?> fuente = crear(cfg, base);
            if (fuente.tipoElemento() == tipo) {
                resultado.addAll((List<T>) fuente.leer());
            }
        }
        return resultado;
    }
}
