package campuseats.limpieza;

public record RegistroPaso(String filtro, int antes, int despues) {
    public int descartadas() {
        return antes - despues;
    }
}