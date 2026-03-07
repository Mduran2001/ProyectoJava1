package proyectojava;

import javax.swing.JOptionPane;

public class ProyectoJava {

    
    public static void main(String[] args) {

        //Esto es la parte inicial del modulo de seleccion de modelo, donde se pide la matricula y el modelo del avion, para luego imprimir la matriz del modelo seleccionado.
        // No me he sentado a estudiar swing asi que de momento se veria un poco feo, pero lo importante es que funcione, y luego ya me sentare a estudiar como hacer que se vea mejor.
        Avion a = new Avion();
        a.setMatricula(JOptionPane.showInputDialog("Ingrese la matricula: "));
        String modelo = JOptionPane.showInputDialog("Ingrese modelo del avión (A/B/C): ");
        a.setModelo(modelo);
        String[][] matriz = a.seleccionModelo();
        a.imprimirAvion(matriz);
    
    }
    
}
