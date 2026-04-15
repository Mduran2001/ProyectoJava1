package proyectofinaljava;

public class Avion {

    // Datos basicos del avion
    private String matricula;
    private String modelo;
    private String tipoAvion;

    // Esta matriz ya viene predefinida y me dice que categoria tiene cada asiento
    // Asi no tengo que calcularla despues
    private String[][] categoriasAsientos;

    // Estas letras solo las uso para mostrar asientos tipo 1A, 1B, 1C...
    private String[] letrasColumnas;

    public Avion(String matricula, String tipoAvion) {
        // Guardo lo que el usuario escribio al crear el avion
        this.matricula = matricula;
        this.tipoAvion = tipoAvion;

        // Apenas se crea el objeto, dejo listo su modelo y su distribucion de asientos
        configurarAvion();
    }

    public void configurarAvion() {

        // Si el usuario escoge el tipo 1, le asigno un avion pequeno
        if (tipoAvion.equals("1")) {
            this.modelo = "Avion Pequeno";

            // Cada posicion representa un asiento y su categoria
            categoriasAsientos = new String[][]{
                {"Primera", "Primera", "Primera"},
                {"Ejecutiva", "Ejecutiva", "Ejecutiva"},
                {"Economica", "Economica", "Economica"}
            };

            // Las letras me ayudan a mostrar mejor el codigo del asiento
            letrasColumnas = new String[]{"A", "B", "C"};
        }

        // Si escoge el tipo 2, se crea un avion mediano
        else if (tipoAvion.equals("2")) {
            this.modelo = "Avion Mediano";

            categoriasAsientos = new String[][]{
                {"Primera", "Primera", "Primera", "Primera"},
                {"Ejecutiva", "Ejecutiva", "Ejecutiva", "Ejecutiva"},
                {"Ejecutiva", "Ejecutiva", "Ejecutiva", "Ejecutiva"},
                {"Economica", "Economica", "Economica", "Economica"}
            };

            letrasColumnas = new String[]{"A", "B", "C", "D"};
        }

        // Si no es 1 ni 2, por descarte uso el tipo 3
        else {
            this.modelo = "Avion Grande";

            categoriasAsientos = new String[][]{
                {"Primera", "Primera", "Primera", "Primera", "Primera"},
                {"Primera", "Primera", "Primera", "Primera", "Primera"},
                {"Ejecutiva", "Ejecutiva", "Ejecutiva", "Ejecutiva", "Ejecutiva"},
                {"Ejecutiva", "Ejecutiva", "Ejecutiva", "Ejecutiva", "Ejecutiva"},
                {"Economica", "Economica", "Economica", "Economica", "Economica"}
            };

            letrasColumnas = new String[]{"A", "B", "C", "D", "E"};
        }
    }

    public String mostrarAvion() {
        String texto = "";

        // Primero muestro la informacion general del avion
        texto += "Modelo: " + modelo + "\n";
        texto += "Matricula: " + matricula + "\n\n";

        // Recorro toda la matriz para mostrar cada asiento con su categoria
        for (int i = 0; i < categoriasAsientos.length; i++) {
            for (int j = 0; j < categoriasAsientos[i].length; j++) {
                texto += (i + 1) + letrasColumnas[j] + " (" + categoriasAsientos[i][j] + ")   ";
            }
            texto += "\n";
        }

        return texto;
    }

    public String getCategoria(int fila, int columna) {
        // Devuelve la categoria exacta del asiento que se pida
        return categoriasAsientos[fila][columna];
    }

    public String[][] getCategoriasAsientos() {
        return categoriasAsientos;
    }

    public String[] getLetrasColumnas() {
        return letrasColumnas;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMatricula() {
        return matricula;
    }
}
