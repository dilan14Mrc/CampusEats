package campuseats.metricas;

public interface Metrica {

    String nombre();

    Object calcular(DatosAnalitica datos);
}
