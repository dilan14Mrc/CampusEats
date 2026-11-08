package campuseats.fabrica;

/** Una entrada de config/fuentes.json: {"tipo": "app_json", "ruta": "datos/pedidos_app.json"} */
public record ConfigFuente(String tipo, String ruta) {}
