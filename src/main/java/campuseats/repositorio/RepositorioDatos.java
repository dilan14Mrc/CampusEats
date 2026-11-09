package campuseats.repositorio;

import campuseats.modelo.LineaPedido;
import campuseats.modelo.Pago;
import campuseats.modelo.Plato;
import java.util.ArrayList;
import java.util.List;

public class RepositorioDatos {
    
    // Aquí se guardarán los datos ya limpios en memoria
    private List<LineaPedido> pedidos = new ArrayList<>();
    private List<Pago> pagos = new ArrayList<>();
    private List<Plato> menu = new ArrayList<>();

    // --- Métodos para GUARDAR los datos (los usará el Pipeline) ---
    public void guardarPedidos(List<LineaPedido> pedidosLimpios) {
        this.pedidos = pedidosLimpios;
    }

    public void guardarPagos(List<Pago> pagosLimpios) {
        this.pagos = pagosLimpios;
    }

    public void guardarMenu(List<Plato> menuLimpio) {
        this.menu = menuLimpio;
    }

    // --- Métodos para OBTENER los datos  ---
    public List<LineaPedido> obtenerPedidos() {
        return pedidos;
    }

    public List<Pago> obtenerPagos() {
        return pagos;
    }

    public List<Plato> obtenerMenu() {
        return menu;
    }
}