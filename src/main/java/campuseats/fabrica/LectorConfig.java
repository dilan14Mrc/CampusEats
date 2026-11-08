package campuseats.fabrica;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Lee config/fuentes.json y devuelve la lista de fuentes a cargar. */
public final class LectorConfig {
    private LectorConfig() {}

    public static List<ConfigFuente> leer(Path archivo) {
        try {
            JsonNode raiz = new ObjectMapper().readTree(archivo.toFile());
            List<ConfigFuente> fuentes = new ArrayList<>();
            for (JsonNode f : raiz.path("fuentes")) {
                fuentes.add(new ConfigFuente(f.get("tipo").asText(), f.get("ruta").asText()));
            }
            return fuentes;
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer la configuración: " + archivo.toAbsolutePath(), e);
        }
    }
}
