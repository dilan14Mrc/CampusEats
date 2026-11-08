package campuseats.fuentes;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * PATRÓN ADAPTER (el "Objetivo"): todas las fuentes se leen igual, con leer().
 * Cada subclase (adaptador) solo sabe traducir SU formato al modelo en convertir().
 */
public abstract class FuenteDatos<T> {
    private final Path ruta;
    private final Class<T> tipoElemento;

    protected FuenteDatos(Path ruta, Class<T> tipoElemento) {
        this.ruta = ruta;
        this.tipoElemento = tipoElemento;
    }

    /** Qué clase del modelo entrega esta fuente (LineaPedido, Pago o Plato). */
    public Class<T> tipoElemento() { return tipoElemento; }

    public final List<T> leer() {
        try {
            String texto = Files.readString(ruta);          // UTF-8 por defecto
            if (texto.startsWith("\uFEFF")) texto = texto.substring(1); // quita BOM de Excel
            return convertir(texto);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer el archivo: " + ruta.toAbsolutePath(), e);
        }
    }

    /** Traduce el texto del archivo (en su formato propio) a objetos del modelo. */
    protected abstract List<T> convertir(String texto);
}
