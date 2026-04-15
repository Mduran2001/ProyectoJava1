package proyectofinaljava;

public class Vuelo {

    // Datos generales del vuelo
    private String codigoVuelo;
    private String origen;
    private String destino;
    private String fecha;
    private double precioBase;

    // Cada vuelo tiene asignado un avion
    private Avion avion;

    // Esta matriz guarda las reservas reales del vuelo
    // Si una posicion esta en null, el asiento sigue libre
    private Reserva[][] reservas;

    // Acumuladores para el resumen financiero
    private double totalVentaTiquetes;
    private double totalVentasAbordo;
    private double totalPenalizacionEquipaje;
    private double totalPenalizacionCancelacion;

    public Vuelo(String codigoVuelo, String origen, String destino, String fecha, double precioBase, Avion avion) {
        this.codigoVuelo = codigoVuelo;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.precioBase = precioBase;
        this.avion = avion;

        // Creo una matriz de reservas del mismo tamano que el avion
        // Asi cada asiento del avion tiene un espacio equivalente aqui
        this.reservas = new Reserva[avion.getCategoriasAsientos().length][avion.getCategoriasAsientos()[0].length]; // El cero hace referencia a las columnas

        this.totalVentaTiquetes = 0;
        this.totalVentasAbordo = 0;
        this.totalPenalizacionEquipaje = 0;
        this.totalPenalizacionCancelacion = 0;
    }

    public String mostrarMapaAsientos() {
        String texto = "";
        String[] letras = avion.getLetrasColumnas();
        String[][] categorias = avion.getCategoriasAsientos();

        // Primero muestro informacion general del vuelo
        texto += "Vuelo: " + codigoVuelo + "\n";
        texto += "Ruta: " + origen + " - " + destino + "\n";
        texto += "Fecha: " + fecha + "\n\n";

        // Recorro toda la matriz de reservas para ver asiento por asiento
        for (int i = 0; i < reservas.length; i++) {
            for (int j = 0; j < reservas[i].length; j++) {
                // El asiento se muestra como 1A, 1B, 1C...
                texto += (i + 1) + letras[j] + " ";

                // Si en esa posicion no hay objeto Reserva, el asiento esta libre
                if (reservas[i][j] == null) {
                    texto += "[Disponible - " + categorias[i][j] + "]   ";
                } else {
                    texto += "[Ocupado - " + categorias[i][j] + "]   ";
                }
            }
            texto += "\n";
        }

        return texto;
    }

    public int contarAsientosOcupados() {
        int contador = 0;

        // Reviso toda la matriz y cuento solo las posiciones que si tienen una reserva
        for (int i = 0; i < reservas.length; i++) {
            for (int j = 0; j < reservas[i].length; j++) {
                if (reservas[i][j] != null) {
                    contador++;
                }
            }
        }

        return contador;
    }

    public int contarTotalAsientos() {
        int total = 0;

        // Voy fila por fila sumando cuantas columnas tiene cada una
        for (int i = 0; i < reservas.length; i++) {
            total = total + reservas[i].length;
        }

        return total;
    }

    public double calcularPorcentajeOcupacion() {
        // Saco el porcentaje con la formula normal: ocupados entre total por 100
        double ocupados = contarAsientosOcupados();
        double total = contarTotalAsientos();
        return (ocupados * 100) / total;
    }

    public double calcularPrecioSegunAsiento(int fila, int columna, String nivelSocio) {
        double precio = precioBase;
        String categoria = avion.getCategoria(fila, columna);

        // Primero ajusto el precio segun la categoria del asiento
        if (categoria.equalsIgnoreCase("Primera")) {
            precio = precioBase * 2.0;
        } else if (categoria.equalsIgnoreCase("Ejecutiva")) {
            precio = precioBase * 1.5;
        } else {
            precio = precioBase;
        }

        // Si el vuelo ya supera el 80% de ocupacion, aumento el precio en 20%
        if (calcularPorcentajeOcupacion() > 80) {
            precio = precio * 1.20;
        }

        // Al final aplico descuento segun el nivel de socio
        if (nivelSocio.equalsIgnoreCase("Platino")) {
            precio = precio - (precio * 0.10);
        } else if (nivelSocio.equalsIgnoreCase("Oro")) {
            precio = precio - (precio * 0.05);
        }

        return precio;
    }

    public String comprarTiquete(String nombrePasajero, String cedula, String nivelSocio, int fila, int columna) {
        String mensaje = "";
        String[] letras = avion.getLetrasColumnas();

        // Primero valido que la fila y la columna existan dentro de la matriz
        if (fila < 0 || fila >= reservas.length || columna < 0 || columna >= reservas[0].length) {
            mensaje = "Asiento invalido.";
        }
        // Si en esa posicion ya hay una reserva, no se puede vender otra vez
        else if (reservas[fila][columna] != null) {
            mensaje = "Ese asiento ya esta ocupado.";
        }
        else {
            // Armo el codigo visible del asiento, por ejemplo 1A o 2C
            String codigoAsiento = (fila + 1) + letras[columna];

            // Obtengo categoria y precio del asiento escogido
            String categoria = avion.getCategoria(fila, columna);
            double precioFinal = calcularPrecioSegunAsiento(fila, columna, nivelSocio);

            // Creo el objeto Reserva con todos los datos del pasajero y del asiento
            Reserva nuevaReserva = new Reserva(
                    nombrePasajero,
                    cedula,
                    nivelSocio,
                    fila,
                    columna,
                    codigoAsiento,
                    categoria,
                    precioBase,
                    precioFinal
            );

            // Guardo la reserva en la posicion exacta del asiento dentro de la matriz
            reservas[fila][columna] = nuevaReserva;

            // Actualizo el acumulado de venta de tiquetes
            totalVentaTiquetes = totalVentaTiquetes + precioFinal;

            mensaje = "Compra realizada con exito.\n";
            mensaje += "Pasajero: " + nombrePasajero + "\n";
            mensaje += "Asiento: " + codigoAsiento + "\n";
            mensaje += "Categoria: " + categoria + "\n";
            mensaje += "Total pagado: " + precioFinal;
        }

        return mensaje;
    }

    public Reserva buscarReservaPorCedula(String cedula) {
        Reserva encontrada = null;

        // Recorro toda la matriz hasta encontrar una reserva con la cedula pedida
        for (int i = 0; i < reservas.length; i++) {
            for (int j = 0; j < reservas[i].length; j++) {
                if (reservas[i][j] != null) {
                    if (reservas[i][j].getCedula().equalsIgnoreCase(cedula)) {
                        encontrada = reservas[i][j];
                    }
                }
            }
        }

        return encontrada;
    }

    public String agregarComidaEspecial(String cedula, String comida) {
        String mensaje = "";
        Reserva reserva = buscarReservaPorCedula(cedula);

        // Si no existe la reserva, no hay a quien modificarle la comida
        if (reserva == null) {
            mensaje = "No se encontro la reserva.";
        } else {
            reserva.setComidaEspecial(comida);
            mensaje = "Comida especial actualizada correctamente.";
        }

        return mensaje;
    }

    public String agregarProductoAbordo(String cedula, String nombreProducto, double precioProducto) {
        String mensaje = "";
        Reserva reserva = buscarReservaPorCedula(cedula);

        if (reserva == null) {
            mensaje = "No se encontro la reserva.";
        } else {
            // Guardo el producto dentro de la reserva del pasajero
            reserva.agregarProducto(nombreProducto, precioProducto);

            // Y tambien actualizo el acumulado general de ventas a bordo del vuelo
            totalVentasAbordo = totalVentasAbordo + precioProducto;
            mensaje = "Producto agregado correctamente.";
        }

        return mensaje;
    }

    public String registrarEquipaje(String cedula, int cantidadMaletas, double[] pesosMaletas) {
        String mensaje = "";
        Reserva reserva = buscarReservaPorCedula(cedula);

        if (reserva == null) {
            mensaje = "No se encontro la reserva.";
        } else {
            // Primero paso al objeto reserva los pesos de cada maleta
            for (int i = 0; i < cantidadMaletas; i++) {
                reserva.setPesoMaleta(i, pesosMaletas[i]);
            }

            // Luego calculo penalizacion usando 10 como cobro por kilo extra
            reserva.registrarMaletas(cantidadMaletas, 10);

            // Acumulo lo que se cobro de mas por equipaje en el resumen financiero
            totalPenalizacionEquipaje = totalPenalizacionEquipaje + reserva.getPenalizacionEquipaje();

            mensaje = "Equipaje registrado correctamente.\n";
            mensaje += reserva.mostrarMaletas();
        }

        return mensaje;
    }

    public String realizarCheckIn(String cedula) {
        String mensaje = "";
        Reserva reserva = buscarReservaPorCedula(cedula);

        if (reserva == null) {
            mensaje = "No se encontro la reserva.";
        } else {
            reserva.cambiarEstado("Check-in Realizado");
            mensaje = "Check-in realizado correctamente.";
        }

        return mensaje;
    }

    public String abordarPasajero(String cedula) {
        String mensaje = "";
        Reserva reserva = buscarReservaPorCedula(cedula);

        if (reserva == null) {
            mensaje = "No se encontro la reserva.";
        } else {
            reserva.cambiarEstado("Abordado");
            mensaje = "Pasajero marcado como abordado.";
        }

        return mensaje;
    }

    public String cancelarReserva(String cedula) {
        String mensaje = "";
        Reserva reserva = buscarReservaPorCedula(cedula);

        if (reserva == null) {
            mensaje = "No se encontro la reserva.";
        } else {
            // Antes de borrar la reserva calculo cuanto se devuelve y cuanto se retiene
            reserva.calcularReembolso();
            double penalidad = reserva.calcularPenalidadCancelacion();

            int fila = reserva.getFila();
            int columna = reserva.getColumna();

            // Libero el asiento volviendo esa posicion a null
            reservas[fila][columna] = null;

            // Ajusto el resumen financiero luego de la cancelacion
            ajustarFinanzasCancelacion(reserva);

            mensaje = "Reserva cancelada correctamente.\n";
            mensaje += "Asiento liberado: " + reserva.getCodigoAsiento() + "\n";
            mensaje += "Reembolso: " + reserva.getReembolso() + "\n";
            mensaje += "Penalidad retenida: " + penalidad;
        }

        return mensaje;
    }

    public String reporteOcupacion() {
        int primeraOcupados = 0;
        int ejecutivaOcupados = 0;
        int economicaOcupados = 0;

        int primeraTotal = 0;
        int ejecutivaTotal = 0;
        int economicaTotal = 0;

        String[][] categorias = avion.getCategoriasAsientos();

        // Recorro la matriz y separo el conteo por categoria
        for (int i = 0; i < reservas.length; i++) {
            for (int j = 0; j < reservas[i].length; j++) {
                if (categorias[i][j].equalsIgnoreCase("Primera")) {
                    primeraTotal++;
                    if (reservas[i][j] != null) {
                        primeraOcupados++;
                    }
                } else if (categorias[i][j].equalsIgnoreCase("Ejecutiva")) {
                    ejecutivaTotal++;
                    if (reservas[i][j] != null) {
                        ejecutivaOcupados++;
                    }
                } else {
                    economicaTotal++;
                    if (reservas[i][j] != null) {
                        economicaOcupados++;
                    }
                }
            }
        }

        String texto = "";
        texto += "REPORTE DE OCUPACION\n\n";
        texto += "Primera Clase: " + primeraOcupados + " de " + primeraTotal + "\n";
        texto += "Clase Ejecutiva: " + ejecutivaOcupados + " de " + ejecutivaTotal + "\n";
        texto += "Clase Economica: " + economicaOcupados + " de " + economicaTotal + "\n";
        texto += "Ocupacion total: " + calcularPorcentajeOcupacion() + "%";

        return texto;
    }

    public String manifiestoComidasEspeciales() {
        String texto = "";
        int contador = 0;

        texto += "MANIFIESTO DE COMIDAS ESPECIALES\n\n";

        // Solo muestro reservas cuya comida no sea estandar
        for (int i = 0; i < reservas.length; i++) {
            for (int j = 0; j < reservas[i].length; j++) {
                if (reservas[i][j] != null) {
                    if (!reservas[i][j].getComidaEspecial().equalsIgnoreCase("Estandar")) {
                        contador++;
                        texto += "Pasajero: " + reservas[i][j].getNombrePasajero() + "\n";
                        texto += "Cedula: " + reservas[i][j].getCedula() + "\n";
                        texto += "Asiento: " + reservas[i][j].getCodigoAsiento() + "\n";
                        texto += "Comida: " + reservas[i][j].getComidaEspecial() + "\n\n";
                    }
                }
            }
        }

        if (contador == 0) {
            texto += "No hay pasajeros con comida especial.";
        }

        return texto;
    }

    public String resumenFinanciero() {
        String texto = "";

        // Muestro todo lo que el vuelo ha generado por cada concepto
        texto += "RESUMEN FINANCIERO\n\n";
        texto += "Venta de tiquetes: " + totalVentaTiquetes + "\n";
        texto += "Ventas a bordo: " + totalVentasAbordo + "\n";
        texto += "Penalizaciones por equipaje: " + totalPenalizacionEquipaje + "\n";
        texto += "Penalizaciones por cancelacion: " + totalPenalizacionCancelacion + "\n";
        texto += "Total general: " + (totalVentaTiquetes + totalVentasAbordo + totalPenalizacionEquipaje + totalPenalizacionCancelacion);

        return texto;
    }

    public String mostrarReserva(String cedula) {
        String texto = "";
        Reserva reserva = buscarReservaPorCedula(cedula);

        if (reserva == null) {
            texto = "No se encontro la reserva.";
        } else {
            texto = reserva.mostrarResumenReserva();
        }

        return texto;
    }

    public String mostrarInformacionVuelo() {
        String texto = "";

        texto += "Codigo: " + codigoVuelo + "\n";
        texto += "Ruta: " + origen + " - " + destino + "\n";
        texto += "Fecha: " + fecha + "\n";
        texto += "Precio base: " + precioBase + "\n";
        texto += "Avion: " + avion.getModelo();

        return texto;
    }

    public void ajustarFinanzasCancelacion(Reserva reserva) {
        // Obtengo los valores necesarios para corregir los ingresos luego de cancelar
        double reembolso = reserva.getReembolso();
        double penalidad = reserva.calcularPenalidadCancelacion();

        // Lo que se devuelve ya no cuenta como ingreso real del vuelo
        totalVentaTiquetes = totalVentaTiquetes - reembolso;

        // La penalidad si queda como ingreso retenido
        totalPenalizacionCancelacion = totalPenalizacionCancelacion + penalidad;
    }

    public String getCodigoVuelo() {return codigoVuelo;}

    public void setCodigoVuelo(String codigoVuelo) {this.codigoVuelo = codigoVuelo;}

    public String getOrigen() {return origen;}

    public void setOrigen(String origen) {this.origen = origen;}

    public String getDestino() {return destino;}

    public void setDestino(String destino) {this.destino = destino;}

    public String getFecha() {return fecha;}

    public void setFecha(String fecha) {this.fecha = fecha;}

    public double getPrecioBase() {return precioBase;}

    public void setPrecioBase(double precioBase) {this.precioBase = precioBase;}

    public Avion getAvion() {return avion;}

    public void setAvion(Avion avion) {this.avion = avion;}

    public Reserva[][] getReservas() {return reservas;}

    public void setReservas(Reserva[][] reservas) {this.reservas = reservas;}

    public double getTotalVentaTiquetes() {return totalVentaTiquetes;}

    public double getTotalVentasAbordo() {return totalVentasAbordo;}

    public double getTotalPenalizacionEquipaje() {return totalPenalizacionEquipaje;}

    public double getTotalPenalizacionCancelacion() {return totalPenalizacionCancelacion;}
}
