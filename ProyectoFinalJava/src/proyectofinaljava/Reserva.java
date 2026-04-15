package proyectofinaljava;

public class Reserva {

    // Datos del pasajero y de su reserva
    private String nombrePasajero;
    private String cedula;
    private String nivelSocio;
    private String comidaEspecial;
    private String estadoReserva;

    // Ubicacion del asiento dentro de la matriz del avion
    private int fila;
    private int columna;
    private String codigoAsiento;
    private String categoriaAsiento;

    // Datos de equipaje
    private int cantidadMaletas;
    private double[] pesosMaletas;
    private double penalizacionEquipaje;

    // Datos de compras a bordo
    private String[] productosComprados;
    private double[] preciosProductos;
    private int cantidadProductos;
    private double totalComprasAbordo;

    // Datos economicos de la reserva
    private double precioBase;
    private double precioFinalTiquete;
    private double totalPagado;
    private double reembolso;

    public Reserva(String nombrePasajero, String cedula, String nivelSocio,
                   int fila, int columna, String codigoAsiento, String categoriaAsiento,
                   double precioBase, double precioFinalTiquete) {

        // Guardo todos los datos principales del pasajero y del asiento que compro
        this.nombrePasajero = nombrePasajero;
        this.cedula = cedula;
        this.nivelSocio = nivelSocio;
        this.fila = fila;
        this.columna = columna;
        this.codigoAsiento = codigoAsiento;
        this.categoriaAsiento = categoriaAsiento;
        this.precioBase = precioBase;
        this.precioFinalTiquete = precioFinalTiquete;

        // Cuando la reserva nace, por defecto la comida es estandar y el estado queda reservado
        this.comidaEspecial = "Estandar";
        this.estadoReserva = "Reservado";

        // Dejo listo el arreglo de maletas, aunque al inicio no tenga ninguna
        this.cantidadMaletas = 0;
        this.pesosMaletas = new double[5];
        this.penalizacionEquipaje = 0;

        // Dejo listo el espacio para compras a bordo
        this.productosComprados = new String[10];
        this.preciosProductos = new double[10];
        this.cantidadProductos = 0;
        this.totalComprasAbordo = 0;

        // Al inicio el total pagado es el del tiquete nada mas
        this.totalPagado = precioFinalTiquete;
        this.reembolso = 0;
    }

    public void setComidaEspecial(String comidaEspecial) {
        // Cambia la comida que quedo guardada en la reserva
        this.comidaEspecial = comidaEspecial;
    }

    public void registrarMaletas(int cantidadMaletas, double cobroPorKiloExtra) {
        // Guardo cuantas maletas trae el pasajero
        this.cantidadMaletas = cantidadMaletas;
        this.penalizacionEquipaje = 0;

        // Reviso una por una para ver si pasan de 23 kg
        for (int i = 0; i < cantidadMaletas; i++) {
            if (pesosMaletas[i] > 23) {
                this.penalizacionEquipaje = this.penalizacionEquipaje + ((pesosMaletas[i] - 23) * cobroPorKiloExtra);
            }
        }

        // Si hubo penalizacion, se suma al total pagado
        this.totalPagado = this.totalPagado + this.penalizacionEquipaje;
    }

    public void setPesoMaleta(int posicion, double peso) {
        // Guardo el peso de una maleta en la posicion que corresponda
        if (posicion >= 0 && posicion < pesosMaletas.length) {
            pesosMaletas[posicion] = peso;
        }
    }

    public void agregarProducto(String nombreProducto, double precioProducto) {
        // Si todavia hay espacio en el arreglo, guardo el producto comprado
        if (cantidadProductos < productosComprados.length) {
            productosComprados[cantidadProductos] = nombreProducto;
            preciosProductos[cantidadProductos] = precioProducto;
            cantidadProductos++;

            // Tambien actualizo los totales economicos
            totalComprasAbordo = totalComprasAbordo + precioProducto;
            totalPagado = totalPagado + precioProducto;
        }
    }

    public void cambiarEstado(String nuevoEstado) {
        // Sirve para pasar de Reservado a Check-in Realizado o Abordado
        this.estadoReserva = nuevoEstado;
    }

    public void calcularReembolso() {
        // Platino recibe todo de vuelta; los demas reciben solo el 70%
        if (nivelSocio.equalsIgnoreCase("Platino")) {
            reembolso = totalPagado;
        } else {
            reembolso = totalPagado * 0.70;
        }
    }

    public double calcularPenalidadCancelacion() {
        double penalidad;

        // Platino no paga penalidad; los otros dejan retenido el 30%
        if (nivelSocio.equalsIgnoreCase("Platino")) {
            penalidad = 0;
        } else {
            penalidad = totalPagado * 0.30;
        }

        return penalidad;
    }

    public String mostrarProductosComprados() {
        String texto = "";

        // Si no ha comprado nada, lo informo de una vez
        if (cantidadProductos == 0) {
            texto = "No ha comprado productos a bordo.";
        } else {
            // Si si compro, recorro el arreglo y muestro todo
            for (int i = 0; i < cantidadProductos; i++) {
                texto += productosComprados[i] + " - " + preciosProductos[i] + "\n";
            }
            texto += "Total compras a bordo: " + totalComprasAbordo;
        }

        return texto;
    }

    public String mostrarMaletas() {
        String texto = "";
        texto += "Cantidad de maletas: " + cantidadMaletas + "\n";

        // Muestro el peso que se registro en cada maleta
        for (int i = 0; i < cantidadMaletas; i++) {
            texto += "Maleta " + (i + 1) + ": " + pesosMaletas[i] + " kg\n";
        }

        texto += "Penalizacion por equipaje: " + penalizacionEquipaje;
        return texto;
    }

    public String mostrarResumenReserva() {
        String texto = "";

        // Devuelvo toda la informacion importante de una reserva en un solo texto
        texto += "Pasajero: " + nombrePasajero + "\n";
        texto += "Cedula: " + cedula + "\n";
        texto += "Socio: " + nivelSocio + "\n";
        texto += "Asiento: " + codigoAsiento + "\n";
        texto += "Categoria: " + categoriaAsiento + "\n";
        texto += "Comida: " + comidaEspecial + "\n";
        texto += "Estado: " + estadoReserva + "\n";
        texto += "Precio tiquete: " + precioFinalTiquete + "\n";
        texto += "Compras a bordo: " + totalComprasAbordo + "\n";
        texto += "Penalizacion equipaje: " + penalizacionEquipaje + "\n";
        texto += "Total pagado: " + totalPagado + "\n";
        texto += "Reembolso: " + reembolso + "\n";

        return texto;
    }

    public String getNombrePasajero() {return nombrePasajero;}

    public void setNombrePasajero(String nombrePasajero) {this.nombrePasajero = nombrePasajero;}

    public String getCedula() {return cedula;}

    public void setCedula(String cedula) {this.cedula = cedula;}

    public String getNivelSocio() {return nivelSocio;}

    public void setNivelSocio(String nivelSocio) {this.nivelSocio = nivelSocio;}

    public String getComidaEspecial() {return comidaEspecial;}

    public String getEstadoReserva() {return estadoReserva;}

    public int getFila() {return fila;}

    public void setFila(int fila) {this.fila = fila;}

    public int getColumna() {return columna;}

    public void setColumna(int columna) {this.columna = columna;}

    public String getCodigoAsiento() {return codigoAsiento;}

    public void setCodigoAsiento(String codigoAsiento) {this.codigoAsiento = codigoAsiento;}

    public String getCategoriaAsiento() {return categoriaAsiento;}

    public void setCategoriaAsiento(String categoriaAsiento) {this.categoriaAsiento = categoriaAsiento;}

    public int getCantidadMaletas() {return cantidadMaletas;}

    public double[] getPesosMaletas() {return pesosMaletas;}

    public double getPenalizacionEquipaje() {return penalizacionEquipaje;}

    public String[] getProductosComprados() {return productosComprados;}

    public double[] getPreciosProductos() {return preciosProductos;}

    public int getCantidadProductos() {return cantidadProductos;}

    public double getTotalComprasAbordo() {return totalComprasAbordo;}

    public double getPrecioBase() {return precioBase;}

    public void setPrecioBase(double precioBase) {this.precioBase = precioBase;}

    public double getPrecioFinalTiquete() {return precioFinalTiquete;}

    public void setPrecioFinalTiquete(double precioFinalTiquete) {this.precioFinalTiquete = precioFinalTiquete;}

    public double getTotalPagado() {return totalPagado;}

    public void setTotalPagado(double totalPagado) {this.totalPagado = totalPagado;}

    public double getReembolso() {return reembolso;}

    public void setReembolso(double reembolso) {this.reembolso = reembolso;}
}
