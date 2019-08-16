package ontologia;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Usuario
* @author ontology bean generator
* @version 2019/08/15, 22:51:46
*/
public class Usuario implements Concept {

   /**
* Protege name: identificacion
   */
   private int identificacion;
   public void setIdentificacion(int value) { 
    this.identificacion=value;
   }
   public int getIdentificacion() {
     return this.identificacion;
   }

   /**
* Protege name: nombre
   */
   private String nombre;
   public void setNombre(String value) { 
    this.nombre=value;
   }
   public String getNombre() {
     return this.nombre;
   }

}
