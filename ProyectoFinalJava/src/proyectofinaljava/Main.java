package proyectofinaljava;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {

        // Arranco con la referencia en null porque al inicio todavia no existe ningun vuelo creado
        Vuelo vuelo1 = null;

        // Aqui voy guardando la opcion del menu principal
        String opcion = "";

        // Mientras no se elija la opcion 11, el menu sigue apareciendo
        while (!opcion.equals("11")) {

            opcion = JOptionPane.showInputDialog(
                    "===== AEROLINEA GLOBAL JAVA =====\n"
                    + "1. Crear vuelo\n"
                    + "2. Ver informacion del vuelo\n"
                    + "3. Ver mapa de asientos\n"
                    + "4. Comprar tiquete\n"
                    + "5. Agregar comida especial\n"
                    + "6. Comprar productos a bordo\n"
                    + "7. Registrar equipaje\n"
                    + "8. Realizar check-in\n"
                    + "9. Cancelar reserva\n"
                    + "10. Ver reportes\n"
                    + "11. Salir"
            );

            // Si el usuario cierra la ventana del input, lo tomo como salida del programa
            if (opcion == null) {
                opcion = "11";
            }

            else if (opcion.equals("1")) {
                
                // Modulo 1 -> Administracion y flota

                // En esta opcion se crea el vuelo completo a partir de lo que ingresa el usuario
                String codigo = JOptionPane.showInputDialog("Digite el codigo del vuelo:");
                String origen = JOptionPane.showInputDialog("Digite el origen:");
                String destino = JOptionPane.showInputDialog("Digite el destino:");
                String fecha = JOptionPane.showInputDialog("Digite la fecha:");
                double precioBase = Double.parseDouble(JOptionPane.showInputDialog("Digite el precio base del tiquete:"));
                String matricula = JOptionPane.showInputDialog("Digite la matricula del avion:");

                String tipoAvion = JOptionPane.showInputDialog(
                        "Seleccione el tipo de avion:\n"
                        + "1. Avion Pequeno\n"
                        + "2. Avion Mediano\n"
                        + "3. Avion Grande"
                );

                // Primero se crea el avion y despues ese avion se le asigna al vuelo
                Avion avionNuevo = new Avion(matricula, tipoAvion);
                vuelo1 = new Vuelo(codigo, origen, destino, fecha, precioBase, avionNuevo);

                // Si ya dejo de ser null es porque el objeto fue creado
                if (vuelo1 != null) {
                    JOptionPane.showMessageDialog(null, "Vuelo creado correctamente.");
                }
            }

            else if (opcion.equals("2")) {
                
                // Modulo 1 -> Administracion y flota

                // Si aun no se creo el vuelo, no se puede mostrar informacion
                if (vuelo1 == null) {
                    JOptionPane.showMessageDialog(null, "Primero debe crear un vuelo.");
                } else {
                    JOptionPane.showMessageDialog(null, vuelo1.mostrarInformacionVuelo());
                }
            }

            else if (opcion.equals("3")) {
                
                //Modulo 1 -> Administracion y flota

                if (vuelo1 == null) {
                    JOptionPane.showMessageDialog(null, "Primero debe crear un vuelo.");
                } else {
                    JOptionPane.showMessageDialog(null, vuelo1.mostrarMapaAsientos());
                }
            }

            else if (opcion.equals("4")) {
                
                
                // Modulo 2 -> Venta de tiquetes y tarifas dinámicas

                if (vuelo1 == null) {
                    JOptionPane.showMessageDialog(null, "Primero debe crear un vuelo.");
                } else {
                    // Pido datos del pasajero y luego el asiento que desea comprar
                    String nombre = JOptionPane.showInputDialog("Digite el nombre del pasajero:");
                    String cedula = JOptionPane.showInputDialog("Digite la cedula del pasajero:");
                    String socio = JOptionPane.showInputDialog(
                            "Digite el nivel de socio:\n"
                            + "Platino\n"
                            + "Oro\n"
                            + "Regular"
                    );

                    // Muestro el mapa antes de pedir el asiento para que el usuario vea que hay disponible
                    JOptionPane.showMessageDialog(null, vuelo1.mostrarMapaAsientos());

                    // Se resta 1 porque el usuario escribe desde 1, pero la matriz empieza en 0
                    int fila = Integer.parseInt(JOptionPane.showInputDialog("Digite la fila del asiento:")) - 1;
                    int columna = Integer.parseInt(JOptionPane.showInputDialog("Digite el numero de columna:")) - 1;

                    JOptionPane.showMessageDialog(null, vuelo1.comprarTiquete(nombre, cedula, socio, fila, columna));
                }
            }

            else if (opcion.equals("5")) {
                
                // Modulo 3 -> Servicios adicionales

                if (vuelo1 == null) {
                    JOptionPane.showMessageDialog(null, "Primero debe crear un vuelo.");
                } else {
                    // Busco la reserva por cedula y cambio la comida especial
                    String cedula = JOptionPane.showInputDialog("Digite la cedula del pasajero:");
                    String comida = JOptionPane.showInputDialog(
                            "Seleccione la comida especial:\n"
                            + "Vegetariano\n"
                            + "Kosher\n"
                            + "Sin Gluten\n"
                            + "Estandar"
                    );

                    JOptionPane.showMessageDialog(null, vuelo1.agregarComidaEspecial(cedula, comida));
                }
            }

            else if (opcion.equals("6")) {
                
                // Modulo 3 -> Servicios adicionales

                if (vuelo1 == null) {
                    JOptionPane.showMessageDialog(null, "Primero debe crear un vuelo.");
                } else {
                    String cedula = JOptionPane.showInputDialog("Digite la cedula del pasajero:");

                    // El usuario escoge un producto y el programa decide subtotal, impuesto y total
                    String opcionProducto = JOptionPane.showInputDialog(
                            "Seleccione el producto:\n"
                            + "1. Perfume (Duty Free)\n"
                            + "2. Electronica (Duty Free)\n"
                            + "3. Bebida (+ impuesto)\n"
                            + "4. Snack (+ impuesto)"
                    );

                    String nombreProducto = "";
                    double precioBaseProducto = 0;
                    double impuesto = 0;
                    double precioFinalProducto = 0;

                    if (opcionProducto.equals("1")) {
                        nombreProducto = "Perfume";
                        precioBaseProducto = 20000;
                        impuesto = 0;
                    } else if (opcionProducto.equals("2")) {
                        nombreProducto = "Electronica";
                        precioBaseProducto = 35000;
                        impuesto = 0;
                    } else if (opcionProducto.equals("3")) {
                        nombreProducto = "Bebida";
                        precioBaseProducto = 2000;
                        impuesto = precioBaseProducto * 0.13;
                    } else if (opcionProducto.equals("4")) {
                        nombreProducto = "Snack";
                        precioBaseProducto = 1500;
                        impuesto = precioBaseProducto * 0.13;
                    } else {
                        JOptionPane.showMessageDialog(null, "Producto invalido.");
                    }

                    // Solo continúo si la opcion escrita fue una de las validas
                    if (opcionProducto.equals("1") || opcionProducto.equals("2") || opcionProducto.equals("3") || opcionProducto.equals("4")) {
                        precioFinalProducto = precioBaseProducto + impuesto;

                        JOptionPane.showMessageDialog(null,
                                "Producto: " + nombreProducto + "\n"
                                + "Subtotal: " + precioBaseProducto + "\n"
                                + "Impuesto: " + impuesto + "\n"
                                + "Total: " + precioFinalProducto);

                        JOptionPane.showMessageDialog(null,
                                vuelo1.agregarProductoAbordo(cedula, nombreProducto, precioFinalProducto));
                    }
                }
            }
            

            else if (opcion.equals("7")) { // Modulo Check - in y control de equipaje

                if (vuelo1 == null) {
                    JOptionPane.showMessageDialog(null, "Primero debe crear un vuelo.");
                } else {
                    String cedula = JOptionPane.showInputDialog("Digite la cedula del pasajero:");
                    int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Digite la cantidad de maletas:"));

                    // Creo un arreglo del tamano exacto para guardar los pesos que ingrese el usuario
                    double[] pesos = new double[cantidad];

                    for (int i = 0; i < cantidad; i++) {
                        pesos[i] = Double.parseDouble(
                                JOptionPane.showInputDialog("Digite el peso de la maleta " + (i + 1) + " en kg:")
                        );
                    }

                    JOptionPane.showMessageDialog(null, vuelo1.registrarEquipaje(cedula, cantidad, pesos));
                }
            }

            else if (opcion.equals("8")) { // Modulo 4 -> Ckeck - in y control de equipaje

                if (vuelo1 == null) {
                    JOptionPane.showMessageDialog(null, "Primero debe crear un vuelo.");
                } else {
                    // Cambia el estado de la reserva a Check-in Realizado
                    String cedula = JOptionPane.showInputDialog("Digite la cedula del pasajero:");
                    JOptionPane.showMessageDialog(null, vuelo1.realizarCheckIn(cedula));
                }
            }

            else if (opcion.equals("9")) {

                if (vuelo1 == null) {
                    
                    
                    JOptionPane.showMessageDialog(null, "Primero debe crear un vuelo.");
                } else {
                    // Busca la reserva, calcula reembolso y libera el asiento
                    String cedula = JOptionPane.showInputDialog("Digite la cedula del pasajero:");
                    JOptionPane.showMessageDialog(null, vuelo1.cancelarReserva(cedula));
                }
            }

            else if (opcion.equals("10")) { // Modulo 5: Cancelaciones y remobolsos

                if (vuelo1 == null) {
                    JOptionPane.showMessageDialog(null, "Primero debe crear un vuelo.");
                } else {
                    // Este submenu deja ver los reportes principales del sistema
                    String subopcion = JOptionPane.showInputDialog(
                            "===== REPORTES =====\n"
                            + "1. Reporte de ocupacion\n"
                            + "2. Manifiesto de comidas especiales\n"
                            + "3. Resumen financiero"
                    );

                    if (subopcion.equals("1")) {
                        JOptionPane.showMessageDialog(null, vuelo1.reporteOcupacion());
                    } else if (subopcion.equals("2")) {
                        JOptionPane.showMessageDialog(null, vuelo1.manifiestoComidasEspeciales());
                    } else if (subopcion.equals("3")) {
                        JOptionPane.showMessageDialog(null, vuelo1.resumenFinanciero());
                    } else {
                        JOptionPane.showMessageDialog(null, "Opcion invalida.");
                    }
                }
            }

            else if (!opcion.equals("11")) {
                JOptionPane.showMessageDialog(null, "Opcion invalida.");
            }
        }

        JOptionPane.showMessageDialog(null, "Programa finalizado.");
    }
}
