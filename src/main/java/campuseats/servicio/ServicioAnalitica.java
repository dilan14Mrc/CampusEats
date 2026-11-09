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

import java.nio.file.Path;
import java.nio.file.Paths;
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

            // 2. LIMPIEZA (Comentamos temporalmente ConciliarConPagos para que no borre todo)
            System.out.println("2. Ejecutando el Pipeline de limpieza de datos...");
            List<Filtro> filtros = Arrays.asList(
                new EliminarDuplicados(),
                new ValidarCantidades(),
                new SoloPagados(),
                // new ConciliarConPagos(pagosBrutos), -> Comentado temporalmente por incompatibilidad de datos
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

            System.out.println("   > Pedidos guardados en repositorio: " + repositorio.obtenerPedidos().size());

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
                
                if (resultado instanceof Map) {
                    ((Map<?, ?>) resultado).forEach((k, v) -> System.out.println("   " + k + " -> " + v));
                } else if (resultado instanceof List) {
                    List<?> lista = (List<?>) resultado;
                    if (lista.isEmpty()) {
                        System.out.println("   (Sin resultados para mostrar)");
                    } else {
                        lista.forEach(item -> System.out.println("   " + item));
                    }
                } else {
                    System.out.println("   " + resultado);
                }
            }

            System.out.println("\n--- PROCESO FINALIZADO CON EXITO ---");

        } catch (Exception e) {
            System.err.println("Error durante la ejecución del servicio: " + e.getMessage());
            e.printStackTrace();
        }
    }
}