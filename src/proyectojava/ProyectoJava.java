package proyectojava;

import javax.swing.JOptionPane;

public class ProyectoJava {

    
    public static void main(String[] args) {

        //Esto es la parte inicial del modulo de seleccion de modelo, donde se pide la matricula y el modelo del avion, para luego imprimir la matriz del modelo seleccionado.
        Avion a = new Avion();
        a.setMatricula(JOptionPane.showInputDialog("Ingrese la matricula: "));
        String modelo = JOptionPane.showInputDialog("Ingrese modelo del avión (A/B/C): ");
        a.setModelo(modelo);
        String[][] matriz = a.seleccionModelo();
        a.imprimirAvion(matriz);
    
    }
    
}
