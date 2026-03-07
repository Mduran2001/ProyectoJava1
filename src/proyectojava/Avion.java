/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectojava;

import javax.swing.JOptionPane;

/**
 *
 * @author aleja
 */
public class Avion {
    private String matricula;
    private String modelo;

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setModelo(String modelo) {
        
        while (!"A".equals(modelo) 
                && !"B".equals(modelo) 
                && !"C".equals(modelo)){
            
            modelo = JOptionPane.showInputDialog("Ingrese un modelo válido (Modelo A, Modelo B, Modelo C): ").toUpperCase();
        }  
        
    this.modelo = modelo;
    }
    
    public String[][] seleccionModelo(){
        
        String[][] tipo = null;
        
        String[][] modeloA = {
            {"1A","1B","1C","1D"},
            {"2A","2B","2C","2D"},
            {"3A","3B","3C","3D"}
        };
        
        String[][] modeloB = {
            {"1A","1B","1C","1D","1E"},
            {"2A","2B","2C","2D","2E"},
            {"3A","3B","3C","3D","3E"},
            {"4A","4B","4C","4D","4E"}
        };
        
        String [][] modeloC = {
            {"1A","1B","1C","1D","1E","1F"},
            {"2A","2B","2C","2D","2E","2F"},
            {"3A","3B","3C","3D","3E","3F"},
            {"4A","4B","4C","4D","4E","4F"},
            {"5A","5B","5C","5D","5E","5F"}
        };
        
        if("A".equals(modelo)) {
            tipo = modeloA;
        }
        if ("B".equals(modelo)) {
            tipo = modeloB;
        }
        if ("C".equals(modelo)) {
            tipo = modeloC;   
        }
        
        return tipo;
    }
    
    public void imprimirAvion(String[][] avion){
        String resultado = "";
        
        for (String[] avion1 : avion) { // Al poner el ciclo de la forma que vimos en clase, el id me recomendo utilizar esta forma de ciclo, y me parecio mas facil de entender, ya que el programa se encarga de recorrer cada fila y cada columna de la matriz, sin necesidad de utilizar indices.
            for (String avion11 : avion1) { // Lo mismo aca, el programa se encarga de recorrer cada fila y cada columna de la matriz, sin necesidad de utilizar indices.
                // Le voy a consultar al profesor si se puede utilizar esta forma que el id Netbeans me recomienda.
                resultado += avion11 + " ";
            }
            resultado += "\n";
        }
        JOptionPane.showMessageDialog(null, resultado);
    }

    // Estoy corrigiendo lo que resta de este modulo, para que las matrices se imprimar por partes segun la clase en donde estan y que el usuario pueda escoger de forma mas facil la opcion deseada de asiento mas facil.
}