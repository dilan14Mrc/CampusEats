package campuseats;

import campuseats.servicio.ServicioAnalitica;

public class Main {
    
    public static void main(String[] args) {
        
        /* 
         * Gracias al patrón Facade, el Main queda súper limpio.
         * Quien ejecuta el código no necesita saber de archivos, fábricas, 
         * pipelines ni métricas. Solo llama al ServicioAnalitica.
         */
        
        ServicioAnalitica servicio = new ServicioAnalitica();
        servicio.ejecutarProcesoCompleto();
        
    }
}