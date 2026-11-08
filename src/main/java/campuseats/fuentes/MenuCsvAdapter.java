package campuseats.fuentes;

import campuseats.modelo.Plato;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

/** CSV separado por coma: id_plato,nombre_plato,categoria,precio_actual,estado */
public class MenuCsvAdapter extends FuenteDatos<Plato> {

    public MenuCsvAdapter(Path ruta) { super(ruta, Plato.class); }

    @Override
    protected List<Plato> convertir(String texto) {
        return texto.lines()
                .skip(1)                                   // cabecera
                .filter(l -> !l.isBlank())
                .map(l -> l.split(",", -1))
                .map(c -> new Plato(
                        Integer.parseInt(c[0].trim()),
                        c[1].trim(),
                        c[2].trim(),
                        new BigDecimal(c[3].trim()),       // precio: posición 3 (la 2 es la categoría)
                        c[4].trim()))
                .toList();
    }
}
