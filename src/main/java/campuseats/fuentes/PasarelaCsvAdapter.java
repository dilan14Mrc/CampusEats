package campuseats.fuentes;

import campuseats.modelo.Pago;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * CSV separado por punto y coma, monto con coma decimal y fecha dd/MM/yyyy:
 * REFERENCIA;MONTO;ESTADO_TX;COD_AUTORIZACION;MOTIVO_RECHAZO;FECHA_HORA
 */
public class PasarelaCsvAdapter extends FuenteDatos<Pago> {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public PasarelaCsvAdapter(Path ruta) { super(ruta, Pago.class); }

    @Override
    protected List<Pago> convertir(String texto) {
        return texto.lines()
                .skip(1)
                .filter(l -> !l.isBlank())
                .map(l -> l.split(";", -1))                // -1: no pierde campos vacíos al final
                .map(c -> new Pago(
                        c[0].trim(),
                        new BigDecimal(c[1].trim().replace(',', '.')),
                        c[2].trim(),
                        LocalDateTime.parse(c[5].trim(), FECHA)))
                .toList();
    }
}
