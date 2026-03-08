/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectojava;

/**
 *
 * @author mario
 */
public class ServicionsAdicionales {
    
    public class Servicios {
    private String nombre;
    private double precio;
    private boolean dutyFree;
    
    public Servicios(String nombre, double precio, boolean dutyFree){
        this.nombre = nombre;
        this.precio = precio;
        this.dutyFree = dutyFree;
        }
        
        public String getNombre() {
            return nombre;     
        }
        public double getPrecio() {
            return precio;     
        }
      public boolean isDutyFree() {
            return dutyFree;     
        }
    }
    
}
