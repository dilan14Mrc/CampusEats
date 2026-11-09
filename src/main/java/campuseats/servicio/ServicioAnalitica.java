package campuseats.servicio;

import campuseats.fabrica.ConfigFuente;
import campuseats.fabrica.FabricaFuentes;
import campuseats.fabrica.LectorConfig;
import campuseats.limpieza.*;
import campuseats.metricas.*;
import campuseats.modelo.LineaPedido;
import campuseats.modelo.Pago;
import campuseats.modelo.Plato;
import campuseats.repositorio.RepositorioDatos;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ServicioAnalitica {
    
    private RepositorioDatos repositorio;

    public ServicioAnalitica() {
        this.repositorio = new RepositorioDatos();
    }

    public void ejecutarProcesoCompleto() {
        System.out.println("--- INICIANDO SISTEMA CAMPUSEATS ---");

        try {
            Path base = Paths.get("."); 
            
            // 1. LECTURA
            System.out.println("1. Leyendo configuracion y extrayendo datos de archivos...");
            List<ConfigFuente> configs = LectorConfig.leer(base.resolve("config/fuentes.json"));
            FabricaFuentes fabrica = new FabricaFuentes();

            List<LineaPedido> pedidosBrutos = fabrica.leerTodas(configs, base, LineaPedido.class);
            List<Pago> pagosBrutos = fabrica.leerTodas(configs, base, Pago.class);
            List<Plato> menuBruto = fabrica.leerTodas(configs, base, Plato.class);

            // 2. LIMPIEZA
            System.out.println("2. Ejecutando el Pipeline de limpieza de datos...");
            List<Filtro> filtros = Arrays.asList(
                new EliminarDuplicados(),
                new ValidarCantidades(),
                new SoloPagados(),
                new ConciliarConPagos(pagosBrutos),
                new UnirConMenu(menuBruto)
            );

            Pipeline pipeline = new Pipeline(filtros);
            List<LineaPedido> pedidosLimpios = pipeline.ejecutar(pedidosBrutos);

            System.out.println("\n--- BITACORA DEL PIPELINE ---");
            for (RegistroPaso paso : pipeline.bitacora()) {
                System.out.println("Filtro: " + paso.filtro() + 
                                   " | Entraron: " + paso.antes() + 
                                   " | Salieron: " + paso.despues() + 
                                   " | Descartadas: " + paso.descartadas());
            }
            System.out.println("--------------------------------\n");

            // 3. ALMACENAMIENTO
            System.out.println("3. Guardando datos limpios en el Repositorio Central...");
            repositorio.guardarPedidos(pedidosLimpios);
            repositorio.guardarPagos(pagosBrutos);
            repositorio.guardarMenu(menuBruto);

            System.out.println("   > Lineas guardadas en repositorio: " + repositorio.obtenerPedidos().size());
            System.out.println("   > Pedidos distintos: " + repositorio.obtenerPedidos().stream()
                    .map(LineaPedido::numeroPedido).distinct().count());

            // 4. METRICAS
            System.out.println("4. Calculando metricas estadisticas...");
            
            DatosAnalitica datosAnalitica = new DatosAnalitica(
                repositorio.obtenerPedidos(), 
                repositorio.obtenerPagos()
            );

            List<Metrica> metricas = Arrays.asList(
                new PlatosTop(),
                new PedidosPorHora(),
                new TicketPromedio()
            );

            for (Metrica metrica : metricas) {
                System.out.println("\n[Metrica] " + metrica.nombre() + ":");
                Object resultado = metrica.calcular(datosAnalitica);
                imprimirResultado(metrica, resultado);
            }

            System.out.println("\n--- PROCESO FINALIZADO CON EXITO ---");

        } catch (Exception e) {
            System.err.println("Error durante la ejecucion del servicio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void imprimirResultado(Metrica metrica, Object resultado) {
        if (metrica instanceof PlatosTop && resultado instanceof List<?> lista) {
            int pos = 1;
            for (Object o : lista) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                System.out.println("   " + pos++ + ". " + e.getKey() + ", " + e.getValue() + " unidades");
            }
        } else if (metrica instanceof PedidosPorHora && resultado instanceof Map<?, ?> mapa) {
            List<Object> horas = new ArrayList<>(mapa.keySet());
            List<Object> cantidades = new ArrayList<>(mapa.values());
            if (horas.isEmpty()) {
                System.out.println("   (Sin resultados para mostrar)");
                return;
            }
            System.out.println("   La hora pico es las " + horas.get(0) + ", con " + cantidades.get(0) + " pedidos.");
            if (horas.size() >= 3) {
                System.out.println("   Le siguen las " + horas.get(1) + " (" + cantidades.get(1)
                        + ") y las " + horas.get(2) + " (" + cantidades.get(2) + ").");
            }
        } else if (resultado instanceof BigDecimal valor) {
            System.out.println("   " + valor.toPlainString().replace('.', ',') + " dolares por pedido");
        } else {
            System.out.println("   " + resultado);
        }
    }
}